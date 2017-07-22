package me.itsrishi.bot.discord.coding.event;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author Rishi Raj on 13-05-2017.
 */
public class EventManager {

    private static List<Event> upcomingEvents;
    private static List<Event> ongoingEvents;
    public static int UPCOMING = 0;
    public static int ONGOING = 1;
    public static void main(String[] args) {
        System.out.print(printEvents(ONGOING));
    }

    public static void init() {
        upcomingEvents = new ArrayList<Event>(40);
        ongoingEvents = new ArrayList<Event>(10);
    }

    public static String printEvents(int type) {
        StringBuilder builder = new StringBuilder();
        init();
        refresh();
        if(type == ONGOING) {
            builder.append("__**Ongoing events**__\n");
            for (Event ongoingEvent: ongoingEvents) {
                String eventTxt = ongoingEvent.toString();
                builder.append(eventTxt);
                builder.append('\n');
            }
            System.out.println("\n\n\nLength \n" +builder.length()+'\n');
        }

        if(type == UPCOMING) {
            builder.append("__**Upcoming events**__\n");
            for (int i = 0; i < 8; i++) {
                String eventTxt = upcomingEvents.get(i).toString();
                builder.append(eventTxt);
                builder.append('\n');
            }
        }
        System.out.print(builder.length());
        if(builder.length()>1995)
            return builder.substring(0,1995)+"\n..";
        return builder.toString();
    }
    private static void refresh() {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
            URL eventsURL = new URL("https://www.hackerrank.com/calendar/feed.rss");
            HttpsURLConnection connection = (HttpsURLConnection) eventsURL.openConnection();

            Document document = Jsoup.parse(connection.getInputStream(),null,eventsURL.getPath());
            Elements elements = document.getElementsByTag("item");

            for (Element elem : elements) {
                Event event = new Event();
                event.setTitle(elem.getElementsByTag("title").get(0).text());
                event.setUrl(elem.getElementsByTag("url").get(0).text());
                event.setStartTime(dateFormat.parse(elem.getElementsByTag("startTime").get(0).text()));
                event.setEndTime(dateFormat.parse(elem.getElementsByTag("endTime").get(0).text()));
                upcomingEvents.add(event);
            }
            Collections.sort(upcomingEvents,new EventComparator());
            Date date = new Date();
            date.setTime(System.currentTimeMillis());
            for (int i = 0; i < upcomingEvents.size(); i++) {
                if(upcomingEvents.get(i).getStartTime().before(date)) {
                    if(upcomingEvents.get(i).getEndTime().after(date))
                        ongoingEvents.add(upcomingEvents.get(i));
                    upcomingEvents.remove(upcomingEvents.get(i));
                    i--;
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            System.out.print("Data cannot be parsed");
            e.printStackTrace();
        }
    }

}
