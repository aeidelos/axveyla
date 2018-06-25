package id.my.rizki.scrapperppdbbot.scrapper_ppdb_bot.entity;
/*
    Created by : Rizki Maulana Akbar, On 06 - 2018 ;
*/

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrarDetails {
    private int range[] = new int[2];
    private int value;
}
