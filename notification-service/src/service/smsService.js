import { logger } from '../logger.js';
import { twilioClient } from '../config/twilio.js';

export async function sendSMS(event) {
    // if (!event.phoneNumber) {
    //     logger.warn('No phone number provided for SMS', { orderId: event.orderNumber.slice(1) });
    //     return;
    // }
    if (!event) {
        logger.warn('No event payload for SMS');
        return;
    }

    try {
         await twilioClient.messages.create({
            to: event.phoneNumber,
            from: process.env.TWILIO_FROM,
            body: `Your order ${event.orderNumber} is confirmed. Total: ${event.data.order.total}`
        });
        logger.info('SMS sent', { orderId: event.correlationId });

    } catch (error) {
        logger.info('SMS not sent', { orderId: event.correlationId, error });
    }
   

}
