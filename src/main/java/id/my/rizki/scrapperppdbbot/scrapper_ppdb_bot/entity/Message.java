package id.my.rizki.scrapperppdbbot.scrapper_ppdb_bot.entity;
/*
    Created by : Rizki Maulana Akbar, On 06 - 2018 ;
*/

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    private String chatId;
    private String message;
}
