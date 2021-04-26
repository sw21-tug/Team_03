FROM node:14

WORKDIR /backend

COPY /backend/package.json .

COPY /backend .

RUN npm install -g nodemon

RUN npm install

EXPOSE 3000

CMD ["nodemon", "/backend/index.js"]