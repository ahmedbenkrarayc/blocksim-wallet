FROM openjdk:8-jdk-alpine

WORKDIR /app

COPY . .

RUN chmod +x build-run.sh

CMD ["./build-run.sh"]
