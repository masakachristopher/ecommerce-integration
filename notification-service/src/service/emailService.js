import { transporter } from '../config/mailer.js';
import Handlebars from 'handlebars';
import { logger } from '../logger.js';
import fs from 'fs';
import path from 'path';

export async function sendEmail(event) {
    if (!event) {
        logger.warn('No event payload for Email');
        return;
    }

    let email = event.data.order.customerEmail;
    if (!email) {
        logger.warn('Missing customer email', { orderNumber: event.data.order.orderNumber });
        email = "masakachristopher@yahoo.com";
        // return;
    }

    let subject = "";
    let template = "";
    if (event.eventType === "order-created") {
        subject = `${toReadableEventName(event.eventType)} #${event.data.order.externalOrderNumber}`;
        template = `${event.eventType}`;
    }
    await sendTemplateMail({
        from: process.env.SMTP_FROM,
        to: email,
        subject: subject,
        template: template,
        data: event.data.order
    });

    logger.info(`Email sent for ${type}`, {
        orderNumber: event.data.order.orderNumber,
        email
    });
}

async function sendTemplateMail({ from, to, subject, template, data }) {
    const templatePath = path.resolve(`src/mail/templates/${template}.hbs`);
    const templateSource = fs.readFileSync(templatePath, 'utf8');

    const html = Handlebars.compile(templateSource)(data);

    return transporter.sendMail({
        from,
        to,
        subject,
        html
    });
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