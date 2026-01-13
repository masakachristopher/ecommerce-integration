// import { emailOrderConsumer, emailPaymentConsumer, emailShippingConsumer } from '../../config/kafka.js';
import { emailOrderConsumer } from '../../config/kafka.js';
import { logger } from '../../logger.js';
import { sendEmail } from '../../service/emailService.js';
import { runConsumer } from '../consumerRunner.js';


export async function startEmailConsumers() {
  await runConsumer({
    consumer: emailOrderConsumer,
    channel: 'email',
    topic: 'order.order-created',
    sendFn: sendEmail
  });

//   await runConsumer({
//     consumer: emailPaymentConsumer,
//     channel: 'email',
//     topic: 'payment-events',
//     sendFn: sendEmail
//   });

//   await runConsumer({
//     consumer: emailShippingConsumer,
//     channel: 'email',
//     topic: 'shipping-events',
//     sendFn: sendEmail
//   });

  logger.info('Kafka Email consumers started');
}
