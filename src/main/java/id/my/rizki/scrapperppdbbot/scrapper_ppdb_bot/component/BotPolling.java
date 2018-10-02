package id.my.rizki.scrapperppdbbot.scrapper_ppdb_bot.component;
/*
    Created by : Rizki Maulana Akbar, On 06 - 2018 ;
*/
import id.my.rizki.scrapperppdbbot.scrapper_ppdb_bot.ScrapperPpdbBotApplication;
import id.my.rizki.scrapperppdbbot.scrapper_ppdb_bot.entity.RegistrarInformation;
import id.my.rizki.scrapperppdbbot.scrapper_ppdb_bot.service.Scrap;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
public class BotPolling extends TelegramLongPollingBot {

    @Autowired
    private Scrap scrap;

    private static final Logger logger = LoggerFactory.getLogger(BotPolling.class);

    private String token = "";

    private String username = "";

    @Override
    public void onUpdateReceived(Update update) {
        logger.info("Receive a new message");
        if(update.hasMessage()) {
            Message message = update.getMessage();
            SendMessage response = new SendMessage();
            Long chatId = message.getChatId();
            response.setChatId(chatId);
            String text = message.getText();
            response.setText(selectedItems(text, chatId));
            try {
                sendMessage(response);
                logger.info("Sent message \"{}\" to {}", text, chatId);
            } catch (TelegramApiException e) {
                logger.error("Failed to send message \"{}\" to {} due to error: {}", text, chatId, e.getMessage());
            }
        }
    }


    private String selectedItems (String command, Long chatId) {
        String cmdLowerCase = command.toLowerCase();
        if(cmdLowerCase.contains("help") || cmdLowerCase.equalsIgnoreCase("help")) {
            String result = "Bot Axveyla - Automatisasi Pengecekan PPDB SMA Jombang\n" +
                    "1. Fitur Cek : ketik cek<spasi>inisial_sekolah (Contoh: cek smada)\n\n" +
                    "Daftar Kode Sekolah yang didukung: \n" +
                    "1. smada = SMAN 2 Jombang\n" +
                    "2. smaga = SMAN 3 Jombang\n" +
                    "3. smansa = SMAN 1 Jombang\n" +
                    "\nUntuk dukungan sekolah/kota lain bisa request ke telegram @rizkimaulanaakbar";
            return result;
        }
        else if(cmdLowerCase.contains("cek")) {
            String[] parser = command.split(" ");
            if(!parser[0].equalsIgnoreCase("cek")) {
                return "Operasi salah";
            }
            else if(parser.length<=1) {
                return "Silahkan masukkan kode sekolah";
            }
            else if(parser.length>2) {
                return "Silahkan masukkan format dengan benar";
            }
            else {
                String code = "0";
                switch (parser[1]) {
                    case "smada" : {
                        code = "103";
                    }
                    break;
                    case "smaga" : {
                        code = "105";
                    }
                    break;
                    case "smansa" : {
                        code = "102";
                    }
                    break;
                }
                RegistrarInformation registrarInformation = scrap.getSchoolStatistic(code);
                return registrarInformation.toString();
            }
        } else {
            return "Belum mendukung untuk operasi ini, silahkan ketik '/help' untuk melihat daftar menu tersedia";
        }
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
