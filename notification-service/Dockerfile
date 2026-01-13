FROM node:20-alpine
WORKDIR /app
COPY ./src/package.json ./
RUN npm install
COPY src ./src
EXPOSE 3050
CMD ["npm", "start"]

# FROM node:20-alpine

# WORKDIR /app

# COPY package.json ./
# RUN npm install --only=production

# COPY src ./src

# EXPOSE 3000

# CMD ["node", "src/index.js"]