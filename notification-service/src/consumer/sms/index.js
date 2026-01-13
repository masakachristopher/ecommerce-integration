// import { smsOrderConsumer, smsPaymentConsumer, smsShippingConsumer } from '../../config/kafka.js';
import { smsOrderConsumer  } from '../../config/kafka.js';
import { logger } from '../../logger.js';
import { sendSMS } from '../../service/smsService.js';
import { runConsumer } from '../consumerRunner.js';

export async function startSmsConsumers() {
  await runConsumer({
    consumer: smsOrderConsumer,
    channel: 'sms',
    topic: 'order.order-created',
    sendFn: sendSMS
  });

  // await runConsumer({
  //   consumer: smsPaymentConsumer,
  //   channel: 'sms',
  //   topic: 'payment-events',
  //   sendFn: sendSMS
  // });

  // await runConsumer({
  //   consumer: smsShippingConsumer,
  //   channel: 'sms',
  //   topic: 'shipping-events',
  //   sendFn: sendSMS
  // });

  logger.info('Kafka SMS consumers started');
}
