import { kafka } from '../config/kafka.js';
import { producer } from '../producers/producer.js';

const consumer = kafka.consumer({ groupId: 'notification-retry-5s' });

export async function startRetry5sConsumer() {
    await consumer.connect();
    await consumer.subscribe({ topic: 'notification.email.retry.5s' });

    await consumer.run({
        eachMessage: async ({ message }) => {
            await new Promise(r => setTimeout(r, 5000)); // 5s delay
            await producer.send({ topic: 'order.events', messages: [message] });
        }
    });
}
