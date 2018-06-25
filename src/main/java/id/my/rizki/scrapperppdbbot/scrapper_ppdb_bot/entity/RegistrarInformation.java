package id.my.rizki.scrapperppdbbot.scrapper_ppdb_bot.entity;
/*
    Created by : Rizki Maulana Akbar, On 06 - 2018 ;
*/

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrarInformation {
    private double min;
    private double max;
    private int value;
    private String school;
    private double average;
    private List<RegistrarDetails> details;

    @Override
    public String toString() {
        StringBuilder details = new StringBuilder();
        this.details.stream().forEach(registrarDetails -> {
            details.append("NilaiUN "+registrarDetails.getRange()[0] +" - "+ registrarDetails.getRange()[1]+ "\t= " +registrarDetails.getValue()+" Siswa \n");
        });
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        DecimalFormat dc = new DecimalFormat("#.#");
        return "Data terupdate pada\t: " + dateFormat.format(date)+"\n"+
                "Nama Sekolah\t: "+ school+ "\n" +
                "Nilai Terendah\t: " + min + "\n" +
                "Nilai Tertinggi\t: " + max + "\n" +
                "Rerata\t\t: " + dc.format(average) + "\n" +
                "Jumlah Pendaftar\t: " + value + "\n" +
                "Detail Sebaran Nilai\t:\n" +
                details;
    }
}
