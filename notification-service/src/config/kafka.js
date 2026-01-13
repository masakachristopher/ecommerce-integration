import { Kafka } from 'kafkajs';

export const kafka = new Kafka({
    clientId: 'notification-service',
    brokers: [process.env.KAFKA_BROKERS || 'localhost:9092']
});

 
export const emailOrderConsumer = kafka.consumer({ groupId: 'email-consumer-order' });
export const emailPaymentConsumer = kafka.consumer({ groupId: 'email-consumer-payment' });
export const emailShippingConsumer = kafka.consumer({ groupId: 'email-consumer-shipping' });

export const smsOrderConsumer = kafka.consumer({ groupId: 'sms-consumer-order' });
export const smsPaymentConsumer = kafka.consumer({ groupId: 'sms-consumer-payment' });
export const smsShippingConsumer = kafka.consumer({ groupId: 'sms-consumer-shipping' });