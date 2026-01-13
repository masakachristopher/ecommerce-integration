import { kafka } from '../config/kafka.js';
import { logger } from '../logger.js';

const consumer = kafka.consumer({ groupId: 'notification-dlq' });

export async function startDLQConsumer() {
    await consumer.connect();
    await consumer.subscribe({ topic: 'notification.email.dlq' });

    await consumer.run({
        eachMessage: async ({ message }) => {
            logger.error('DLQ Event', { value: message.value.toString() });
        }
    });
}
