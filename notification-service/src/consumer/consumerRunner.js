import { kafka } from "../config/kafka.js";
import { logger } from "../logger.js";
import { producer } from "../producer/producer.js";
import { isDuplicate } from "../service/idempotencyService.js";
// import { kafka } from "../kafka.js"; // ← import the shared kafka instance!
export async function runConsumer({ consumer, channel, topic, sendFn }) {
    try {
        await consumer.connect();
        await consumer.subscribe({ topic: topic, fromBeginning: true });

        await consumer.run({
            eachMessage: async function ({ message }) {
           
                const event = JSON.parse(message.value.toString());
                console.log("BODY", event);
                const key = `${channel}-${event.eventType}-${event.eventId}`;

                const duplicate = await isDuplicate(key);
                if (duplicate) return;

                try {
                    await sendFn(event);
                } catch (err) {
                    console.log("Sending action failed....", err)
                    logger.error(`${channel} ${event.eventType} failed, sending to retry`, err);
                    await producer.send({
                        topic: `${event.eventType}.${channel}.retry`,
                         message: { key: event.correlationId, value: JSON.stringify(event) }
//                        messages: [{ key: event.correlationId, value: JSON.stringify(event) }]
                    });
                }
            }
        });
    } catch (error) {
        logger.error(`Failed to start consumer for topic ${topic} - channel ${channel}`, error);
    }

    /**
     * Converts kebab-case string to readable title case
     * @param {string} kebabEventName - kebab-case event name (e.g. "order-created")
     * @returns {string} Title Case version (e.g. "Order Created")
     */
    function toReadableEventName(kebabEventName) {
        if (!kebabEventName || kebabEventName.trim() === '') {
            return '';
        }

        return kebabEventName
            .split('-')
            .map(word => word.charAt(0).toUpperCase() + word.slice(1).toLowerCase()).join(' ');
    }
}


async function waitForTopicReady(topic, timeoutMs = 60000) {
    const admin = kafka.admin();
    const start = Date.now();

    await admin.connect();

    try {
        while (Date.now() - start < timeoutMs) {
            try {
                const metadata = await admin.fetchTopicMetadata({ topics: [topic] });
                const topicMeta = metadata.topics.find(t => t.name === topic);

                if (!topicMeta) {
                    logger.info(`Topic ${topic} not visible yet... waiting`);
                    await new Promise(r => setTimeout(r, 2000));
                    continue;
                }

                const allReady = topicMeta.partitions.every(
                    p => p.leader !== -1 && !p.error
                );

                if (allReady) {
                    logger.info(`Topic ${topic} is ready — leaders elected`);
                    return;
                }

                logger.info(`Topic ${topic} - waiting for all leaders...`);
            } catch (e) {
                // Ignore expected startup errors
                if (e.type === 'UNKNOWN_TOPIC_OR_PARTITION' ||
                    e.message?.includes('does not host this topic-partition')) {
                    // Topic still propagating
                } else {
                    throw e;
                }
            }

            await new Promise(r => setTimeout(r, 2000));
        }

        throw new Error(`Timeout waiting for topic ${topic} (${timeoutMs / 1000}s)`);
    } finally {
        await admin.disconnect().catch(() => { });
    }
}