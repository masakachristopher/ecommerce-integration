import Redis from 'ioredis';

// export const redisClient = new Redis({
//   host: process.env.REDIS_HOST || 'localhost',
//   port: process.env.REDIS_PORT || 6379
// });

const host = process.env.REDIS_HOST || 'localhost'
const port = process.env.REDIS_PORT || 6379

export const redisClient = new Redis(`redis://${host}:${port}`);