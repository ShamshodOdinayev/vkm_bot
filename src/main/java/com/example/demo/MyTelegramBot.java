package com.example.demo;


import com.example.demo.service.YtDlpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MyTelegramBot extends TelegramLongPollingBot {
    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Autowired
    private YtDlpService ytDlpService;

    private final Map<Long, List<String>> searchCache = new HashMap<>();


    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                String text = update.getMessage().getText();
                Long chatId = update.getMessage().getChatId();

                if (text.startsWith("/start")) {
                    sendMsg(chatId, "Salom! Qaysi musiqani qidirmoqchisiz?");
                } else if (!text.matches("\\d+")) {
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(chatId);
                    sendMessage.setText("Search \uD83D\uDD0D");
                    execute(sendMessage);
                    List<String> results = ytDlpService.searchYoutube(text);
                    searchCache.put(chatId, results);
                    StringBuilder message = new StringBuilder("Topilganlar:\n");

                    int i = 1;
                    for (String line : results) {
                        message.append(i++).append(". ").append(line).append("\n");
                    }

                    message.append("\nTanlash uchun 1-10 raqam yuboring");
                    sendMsg(chatId, message.toString());
                } else {
                    int index = Integer.parseInt(text) - 1;
                    if (searchCache.containsKey(chatId)) {
                        List<String> results = searchCache.get(chatId);
                        if (index >= 0 && index < results.size()) {
                            String selected = results.get(index);
                            String videoId = selected.split("\\|")[2].trim();
                            sendMsg(chatId, "Yuklab olinmoqda...");

                            File mp3 = ytDlpService.downloadAudio(videoId);
                            if (mp3 != null) {
                                SendAudio audio = new SendAudio();
                                audio.setChatId(chatId.toString());
                                audio.setAudio(new InputFile(mp3));
                                execute(audio);
                                mp3.delete(); // tozalash
                            } else {
                                sendMsg(chatId, "Faylni yuklab bo‘lmadi.");
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMsg(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        try {
            execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


