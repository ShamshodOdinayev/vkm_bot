package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;

@Service
public class YtDlpService {

    private static final String YTDLP_PATH = "yt-dlp/yt-dlp.exe"; // yoki "C:\\tools\\yt-dlp.exe"

    public List<String> searchYoutube(String query) throws IOException {
        List<String> results = new ArrayList<>();
        ProcessBuilder pb = new ProcessBuilder(
                YTDLP_PATH, "ytsearch10:" + query,
                "--ffmpeg-location", "yt-dlp/ffmpeg-7.1.1-essentials_build/bin\\ffmpeg.exe",
                "--print", "%(title)s | %(duration_string)s | %(id)s"
        );

        pb.redirectErrorStream(true); // xatoliklarni chiqishga birlashtirish
        Process process = pb.start();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                results.add(line);
            }
        }
        return results;
    }

    public File downloadAudio(String videoId) throws IOException, InterruptedException {
        String url = "https://www.youtube.com/watch?v=" + videoId;
        String outputTemplate = "downloads/%(title)s.%(ext)s";

        ProcessBuilder pb = new ProcessBuilder(
                YTDLP_PATH, "-f", "bestaudio", "-x",
                "--audio-format", "mp3",
                "--ffmpeg-location", "yt-dlp/ffmpeg-7.1.1-essentials_build/bin\\ffmpeg.exe",
                "-o", outputTemplate,
                url
        );

        pb.redirectErrorStream(true);
        Process process = pb.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line); // log yozib boring
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            System.err.println("yt-dlp process failed with exit code " + exitCode);
            return null;
        }

        // Yuklangan faylni topamiz
        File downloadDir = new File("downloads");
        File[] files = downloadDir.listFiles((dir, name) -> name.endsWith(".mp3"));
        if (files != null && files.length > 0) {
            return files[0];
        }
        return null;
    }

}
