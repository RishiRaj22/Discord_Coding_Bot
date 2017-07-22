package me.itsrishi.bot.discord.coding;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import me.itsrishi.bot.discord.coding.coder.Coder;
import me.itsrishi.bot.discord.coding.coder.CoderManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Rishi Raj on 12-05-2017.
 */
public class RanklistGetter {
    private static CoderManager coderManager;
    public static void setCoderManager(CoderManager coderManager) {
        RanklistGetter.coderManager = coderManager;
    }

    public static String getRank() {
        StringBuilder res = new StringBuilder("**Rankings**:\n\n");
        for(Coder coder: coderManager.getListCoders().values()) {
            if (coder.getCodechefHandle() != null) {
                res.append("[CodeChef] " + coder.getCodechefHandle() + ":\n");
                res.append(getCodeChefRank(coder) + "\n\n");
            }
            if (coder.getCodeforceHandle() != null) {
                res.append("[CodeForces] " + coder.getCodeforceHandle() + ":\n");
                res.append(getCodeForcesRank(coder) + "\n\n");
            }
        }
        return res.toString();
    }


    private static String getCodeChefRank(Coder coder) {
            try {
                String url = "https://www.codechef.com/users/"+coder.getCodechefHandle();
                String str;
                StringBuilder builder = new StringBuilder(10000);
                URL codechefLink = new URL(url);
                HttpsURLConnection connection = (HttpsURLConnection) codechefLink.openConnection();
                Document document = Jsoup.parse(connection.getInputStream(),null,url);
                Elements elements = document.getElementsByClass("rating-ranks");
                Elements lists = elements.get(0).children();
                connection.disconnect();
                return lists.get(0).text();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return null;
    }
    private static String getCodeForcesRank(Coder coder) {
        String str,url = "http://codeforces.com/api/user.info?handles="+coder.getCodeforceHandle();
        StringBuilder builder = new StringBuilder(10000);
        try {
            URL codeForceLink = new URL(url);
            String returnStr = "Rating: ";
            HttpURLConnection connection = (HttpURLConnection) codeForceLink.openConnection();
            JsonObject value = (JsonObject)Json.parse(new InputStreamReader(connection.getInputStream()));
            System.out.println(value.get("status"));
            if(value.getString("status","OK").equals("OK"))
                returnStr += value.get("result").asArray().get(0).asObject().getInt("rating",0);
            connection.disconnect();
            return returnStr;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "Error";
        }
        catch (IOException e) {
            e.printStackTrace();
            return "Error";
        }
    }
}
