package me.itsrishi.bot.discord.coding.event;

import java.util.Comparator;

/**
 * @author Rishi Raj on 13-05-2017.
 */ public class EventComparator implements Comparator<Event> {

    public int compare(Event o1, Event o2) {
        return o1.getStartTime().compareTo(o2.getStartTime());
    }
    public boolean equals(Object obj) {
        return false;
    }
}
