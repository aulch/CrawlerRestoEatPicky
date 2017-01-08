import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Aulia Chairunisa on 10-Dec-16.
 */
public class resto_crawler {
    public static FileWriter file ;
    public static void main(String[] args) throws IOException{
        FileReader fileLink = new FileReader("D:\\urls.txt");
        BufferedReader br = new BufferedReader(fileLink);

        file = new FileWriter("D:\\restoran.json");
        String sCurrentLine;
        while ((sCurrentLine = br.readLine()) != null) {
            System.out.println("ini link: " + sCurrentLine);
            //System.out.println("still working....");
            getJson(sCurrentLine);
        }

        file.close();
    }

    private static void getJson(String sCurrentLine) throws IOException {
        Document doc = (Document) Jsoup.connect(sCurrentLine).timeout(0).get();

        String title = "";
        Elements namaResto = doc.select("h1[itemprop=name]");
        Pattern patternNamaResto = Pattern.compile("a.*>(.*)<\\/a>");
        //System.out.println("ini pattern nama resto: " + namaResto);
        Matcher matcherNamaResto = patternNamaResto.matcher(namaResto.toString());
        if (matcherNamaResto.find()) {
            title = matcherNamaResto.group(1);
        }

        String rate = "";
        Elements rating = doc.select("img.big_rating_score_cooked");
        rate = rating.attr("title");

        String location = "";
        /*Elements loc = doc.select("meta[itemprop=addressRegion]");*/
        Elements loc = doc.select("span[itemprop=address]");
        //System.out.println("ini span : " + loc);
        Pattern patternLoc = Pattern.compile("<span>(.*)<\\/span>");
        Matcher matcherLoc = patternLoc.matcher(loc.toString());
        if (matcherLoc.find()) {
            location = matcherLoc.group(1);
        }

        JSONObject obj = new JSONObject();
        obj.put("Nama Restoran", title);
        obj.put("Rating", rate);
        obj.put("Lokasi", location);

        try {

            file.write(obj.toJSONString());
            file.write(",");
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
