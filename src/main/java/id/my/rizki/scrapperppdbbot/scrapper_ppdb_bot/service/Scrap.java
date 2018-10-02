package id.my.rizki.scrapperppdbbot.scrapper_ppdb_bot.service;
/*
    Created by : Rizki Maulana Akbar, On 06 - 2018 ;
*/

import id.my.rizki.scrapperppdbbot.scrapper_ppdb_bot.entity.RegistrarDetails;
import id.my.rizki.scrapperppdbbot.scrapper_ppdb_bot.entity.RegistrarInformation;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class Scrap {


    private double min = 400;
    private double max = 0;

    public RegistrarInformation getSchoolStatistic (String idClassroom) {

        Document doc = getWebpages(idClassroom);

        Elements elements = doc.select("tbody");
        List<RegistrarDetails>registrarDetails = new ArrayList<>();
        Elements elementsScName = doc.select("strong");
        Element elementScName = elementsScName.first();
        String school = elementScName.text();
        int value = 0;
        double average = 0;
        for(Element element : elements) {
            Elements trs = element.select("tr");
            String[][] trtd = splitToTrTd(trs);
            value = trs.size();
            int sum = 0;

            RegistrarDetails u20 = new RegistrarDetails(new int[]{00, 200}, 0);
            RegistrarDetails u25 = new RegistrarDetails(new int[]{200, 250}, 0);
            RegistrarDetails u27 = new RegistrarDetails(new int[]{250, 270}, 0);
            RegistrarDetails u30 = new RegistrarDetails(new int[]{270, 300}, 0);
            RegistrarDetails u40 = new RegistrarDetails(new int[]{300, 400}, 0);
            for(int i = 0; i<trs.size(); i++) {
                int temp = Integer.valueOf((int) Double.parseDouble(trtd[i][5]));
                sum += temp;
                if(temp <= 200) {
                    u20.setValue(u20.getValue() + 1);
                } else if (temp >200 && temp <= 250) {
                    u25.setValue(u25.getValue() + 1);
                } else if (temp >250 && temp <= 270) {
                    u27.setValue(u27.getValue() + 1);
                } else if (temp >270 && temp <= 300) {
                    u30.setValue(u30.getValue() + 1);
                } else {
                    u40.setValue(u40.getValue() + 1);
                }
                if (temp > max) {
                    max = temp;
                }
                if (temp < min) {
                    min = temp;
                }
            }
            registrarDetails.add(u20);
            registrarDetails.add(u25);
            registrarDetails.add(u27);
            registrarDetails.add(u30);
            registrarDetails.add(u40);
            average = (double) sum / trs.size();
        }
        return RegistrarInformation.builder().min(min).max(max).details(registrarDetails).
                value(value).average(average).school(school).build();
    }

    private String[][] splitToTrTd(Elements trs) {
        String[][] trtd = new String[trs.size()][];
        for (int i = 0; i < trs.size(); i++) {
            Elements tds = trs.get(i).select("td");
            trtd[i] = new String[tds.size()];
            for (int j = 0; j < tds.size(); j++) {
                trtd[i][j] = tds.get(j).text();
            }
        }
        return trtd;
    }

    private Document getWebpages(String idClassroom) {
        try {
            return Jsoup.connect("https://13.ppdbjatim.net/pengumuman/pengumuman_penerimaan/sma/sekolah/"
                    + String.valueOf(idClassroom)).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
