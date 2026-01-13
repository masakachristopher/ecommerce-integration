import Handlebars from 'handlebars';

Handlebars.registerHelper('formatAmount', value =>
    typeof value === 'number' ? value.toLocaleString('en-US') : value
);

Handlebars.registerHelper('multiply', (a, b) => a * b);

Handlebars.registerHelper('year', () => new Date().getFullYear());
