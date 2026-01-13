import Fastify from 'fastify';
// import { startRetry5sConsumer } from './consumer/retry5sConsumer.js';
// import { startRetry1mConsumer } from './consumer/retry1mConsumer.js';
// import { startDLQConsumer } from './consumer/dlqConsumer.js';
import { logger } from './logger.js';
import '../src/handlebarsHelper.js';
import { startEmailConsumers } from './consumer/email/index.js';
import { startSmsConsumers } from './consumer/sms/index.js';

const app = Fastify({ logger: true });

app.get('/health', async () => ({ status: 'UP' }));
app.get('/ready', async () => ({ status: 'READY' }));

app.listen({ port: 3050 }, async (err) => {
    if (err) {
        logger.error(err);
        process.exit(1);
    }
    logger.info('Fastify server running on port 3050');

    // Start Kafka consumers
    await Promise.all([
      startEmailConsumers(),
      startSmsConsumers()
    ]);

    logger.info('All Kafka consumers started');
});

// ====== KAFKA independent start version ======
// import { startRetry5sConsumer } from './consumers/retry5sConsumer.js';
// import { startRetry1mConsumer } from './consumers/retry1mConsumer.js';
// import { startDLQConsumer } from './consumers/dlqConsumer.js';

// console.log('Starting Notification Service...');

// await startRetry5sConsumer();
// await startRetry1mConsumer();
// await startDLQConsumer();

// console.log('All consumers started successfully');
