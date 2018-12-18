
import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;

import javax.imageio.ImageIO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class main {

    public static void main(String[] args) throws IOException {

        //Trendyol();
        sinemalar("1");
        sinemalar("2");
        
        //filmIcerik("/title/tt0252487/fullcredits/?ref_=tt_ov_st_sm", "Hababam");
        //resimIndir(img_path, "deneme");
    }

    public static void sinemalar(String sayfa_no) {
        String filmUrl, filmOyuncuLink, film_adi;

        try {
            String URL = "https://www.imdb.com/list/ls056515174/?sort=list_order,asc&st_dt=&mode=detail&page="+sayfa_no;
            Document doc = (Document) Jsoup.connect(URL).get();
            Elements newsHeadlines = doc.select("div.lister-item-content > h3 > a");
            for (Element headline : newsHeadlines) {
                //System.out.println(headline.attr("href"));
                filmUrl = headline.attr("href");
                filmOyuncuLink = filmUrl.substring(0, 16) + "/fullcredits/?ref_=tt_ov_st_sm";
                film_adi = headline.text();
                //System.out.println(film_adi);
                filmIcerik(filmOyuncuLink, film_adi);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void filmIcerik(String url, String film_adi) {

        String r_url, r_adi;
        try {
            String URL = "https://www.imdb.com" + url;
            Document doc = (Document) Jsoup.connect(URL).get();
            Elements newsHeadlines = doc.select("tbody > tr > td > a > img");

            for (Element headline : newsHeadlines) {
                r_url = headline.attr("loadlate");
                r_adi = headline.attr("alt");
                if (r_url.length() != 0) {

                    resimIndir(r_url, r_adi, film_adi);
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public static void resimIndir(String resim_url, String isim, String film_adi) throws FileNotFoundException, IOException {

        System.out.println(film_adi + " > " + isim + " > " + resim_url);
        Image image = null;
        try {
            URL url = new URL(resim_url);
            image = ImageIO.read(url);
        } catch (IOException e) {
        }
        URL url = new URL(resim_url);
        InputStream is = url.openStream();
        OutputStream os;
        String path = "/home/muss/NetBeansProjects/Film_Oyunculari/"+film_adi;
        File f = new File(path);
        f.mkdir();
        os = new FileOutputStream(path + "/" + isim);

        byte[] b = new byte[2048];
        int length;

        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);
        }
        is.close();
        os.close();
    }

}
