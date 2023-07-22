FROM node:18 AS base

FROM base AS deps
WORKDIR /app
COPY package.json ./
RUN npm install

FROM base AS builder
WORKDIR /app
COPY . ./
COPY --from=deps /app/node_modules ./node_modules
RUN npm run build

FROM nginx:1.19.0
WORKDIR /usr/share/nginx/html

RUN rm -rf ./*
COPY --from=builder /app/build .
EXPOSE 80
EXPOSE 443

ENTRYPOINT [ "nginx", "-g", "daemon off;" ]