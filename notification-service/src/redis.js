import { redisClient } from "./config/redis";

/**
 * Silent duplicate check.
 * Stores the key in Redis if it doesn't exist.
 * Returns true if duplicate, false if first time.
 *
 * @param {string} key - Unique identifier for the event
 * @param {number} ttlSeconds - Expiration time in seconds (default: 1 hour)
 * @returns {boolean} - true if duplicate, false if first time
 */
export async function isDuplicate(key, ttlSeconds = 3600) {
  const exists = await redisClient.get(key);
  if (exists) return true;

  await redisClient.set(key, '1', 'EX', ttlSeconds);
  return false;
}