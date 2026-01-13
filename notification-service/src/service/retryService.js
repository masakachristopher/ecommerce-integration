export function nextRetryTopic(attempt) {
    if (attempt === 1) return 'notification.email.retry.5s';
    if (attempt === 2) return 'notification.email.retry.1m';
    return 'notification.email.dlq';
}