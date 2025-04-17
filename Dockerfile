# 1. Java 17 asosida image
FROM eclipse-temurin:17-jdk

# 2. Ishchi papkani belgilaymiz
WORKDIR /app

# 3. Jar faylni konteynerga ko‘chiramiz
#COPY target/your-project-name.jar app.jar

COPY target/demo-0.0.1-SNAPSHOT.jar app.jar




# 4. ffmpeg va yt-dlp ni o‘rnatamiz
RUN apt-get update && \
    apt-get install -y wget ffmpeg && \
    wget https://github.com/yt-dlp/yt-dlp/releases/latest/download/yt-dlp && \
    chmod +x yt-dlp

# 5. Port ochamiz (faqat kerak bo‘lsa)
EXPOSE 8080

# 6. Jar faylni ishga tushiramiz
CMD ["java", "-jar", "app.jar"]
