import { kafka } from '../config/kafka.js';

export const producer = kafka.producer();
await producer.connect();
