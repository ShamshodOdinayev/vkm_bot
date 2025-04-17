# 1. Build stage
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# 2. Runtime stage
FROM eclipse-temurin:17-jdk
WORKDIR /app

# yt-dlp va ffmpeg o‘rnatish
RUN apt-get update && \
    apt-get install -y wget ffmpeg && \
    wget https://github.com/yt-dlp/yt-dlp/releases/latest/download/yt-dlp && \
    chmod +x yt-dlp

# Jar faylni build stage'dan olish
COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
