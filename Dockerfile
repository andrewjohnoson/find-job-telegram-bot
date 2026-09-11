FROM mcr.microsoft.com/playwright/java:v1.62.0-noble

WORKDIR /app

COPY . .

RUN chmod +x gradlew

CMD ["./gradlew", "run", "--no-daemon"]