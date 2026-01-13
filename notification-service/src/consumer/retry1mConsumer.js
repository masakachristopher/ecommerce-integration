import { kafka } from '../config/kafka.js';
import { producer } from '../producer/producer.js';

const consumer = kafka.consumer({ groupId: 'notification-retry-1m' });

export async function startRetry1mConsumer() {
    await consumer.connect();
    await consumer.subscribe({ topic: 'notification.email.retry.1m' });

    await consumer.run({
        eachMessage: async ({ message }) => {
            await new Promise(r => setTimeout(r, 60000)); // 1m delay
            await producer.send({ topic: 'order.events', messages: [message] });
        }
    });
}
