// import { kafka } from '../config/kafka.js';
// import { producer } from '../producers/producer.js';
// import { isDuplicate } from '../services/idempotencyService.js';
// import { sendOrderEmail } from '../services/emailService.js';
// import { nextRetryTopic } from '../services/retryService.js';
// import { logger } from '../logger.js';

// const consumer = kafka.consumer({ groupId: 'notification-main' });

// export async function startOrderConsumer() {
//     await consumer.connect();
//     // await consumer.subscribe({ topic: 'order.events' });
//     await consumer.subscribe({ topic: 'order-events' });

//     await consumer.run({
//         eachMessage: async ({ message }) => {
//             const event = JSON.parse(message.value.toString());

//             if (await isDuplicate(event.eventId)) {
//                 logger.info('Duplicate event skipped', { eventId: event.eventId });
//                 return;
//             }

//             try {
//                 await sendOrderEmail(event);
//                 logger.info('Email sent', { orderId: event.orderId });
//             } catch (err) {
//                 const attempt = (event.attempt || 0) + 1;
//                 const topic = nextRetryTopic(attempt);
//                 logger.warn(`Retrying event on ${topic}`, { orderId: event.orderId, attempt });

//                 await producer.send({
//                     topic,
//                     messages: [{ key: event.orderId, value: JSON.stringify({ ...event, attempt }) }]
//                 });
//             }
//         }
//     });
// }

// // import { kafka } from '../config/kafka.js';
// // import { producer } from '../producers/producer.js';
// // import { isDuplicate } from '../services/idempotencyService.js';
// // import { sendOrderEmail } from '../services/emailService.js';
// // import { nextRetryTopic } from '../services/retryService.js';

// // const consumer = kafka.consumer({ groupId: 'notification-main' });

// // export async function startOrderConsumer() {
// //     await consumer.connect();
// //     await consumer.subscribe({ topic: 'order.events' });

// //     await consumer.run({
// //         eachMessage: async ({ message }) => {
// //             const event = JSON.parse(message.value.toString());

// //             if (await isDuplicate(event.eventId)) return;

// //             try {
// //                 await sendOrderEmail(event);
// //             } catch (err) {
// //                 const attempt = (event.attempt || 0) + 1;
// //                 const topic = nextRetryTopic(attempt);

// //                 await producer.send({
// //                     topic,
// //                     messages: [{ key: event.orderId, value: JSON.stringify({ ...event, attempt }) }]
// //                 });
// //                 throw err;
// //             }
// //         }
// //     });
// // }
