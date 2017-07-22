package me.itsrishi.bot.discord.coding;

import me.itsrishi.bot.discord.coding.coder.CoderManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        CoderManager coderManager = new CoderManager();
        coderManager.init();
        String botToken = "",serverID = "",adminID = "";

        System.out.println("You can provide arguments like: <bot token> <server id> <admin role id>");
        System.out.println("Or alternatively save these configs in that format in config.dat file");
        System.out.println("If you have problems knowing what are those parameters, head on to www.itsrishi.me/codingbot");

        try {
            if (new File("config.dat").exists())
            {
                FileInputStream fis = new FileInputStream("config.dat");
                Scanner sc = new Scanner(fis);
                botToken = sc.next();
                serverID = sc.next();
                adminID = sc.next();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if(botToken == "") {
            botToken = args[0];
            serverID = args[0];
            adminID = args[0];
        }
        Bot bot = new Bot(botToken,serverID,adminID);
        bot.setCoderManager(coderManager);
        RanklistGetter.setCoderManager(coderManager);
        bot.init();
    }
}
