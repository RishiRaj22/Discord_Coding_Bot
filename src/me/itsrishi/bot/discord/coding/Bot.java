package me.itsrishi.bot.discord.coding;

import com.google.common.util.concurrent.FutureCallback;
import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.Javacord;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.listener.message.MessageCreateListener;
import me.itsrishi.bot.discord.coding.coder.Coder;
import me.itsrishi.bot.discord.coding.coder.CoderManager;
import me.itsrishi.bot.discord.coding.event.EventManager;

import java.text.DecimalFormat;

/**
 * @author Rishi Raj on 12-05-2017.
 */
public class Bot {

    private String token;
    private String mServer;
    private String admin;

    private String tRegister = "!register";
    private String tRanklist = "!ranklist";
    private String tEvents = "!events";
    private String tCommands = "!commands";
    private String tDeregister = "!deregister";
    private String tList = "!list";
    private String chef = "cchef";
    private String hrank = "hrank";
    private String cforce = "cforce";
    private String tUpcoming = "upcoming";
    private String tOngoing = "ongoing";
    private CoderManager coderManager;

    private String commands = "**Commands**:\n" +
            "1. " + tRanklist + " - to get ranklist\n" +
            "2. " + tEvents + " <" + tUpcoming+"> <" + tOngoing + "> - to get list of upcoming and/or  events\n" +
            "3. " + tCommands + " - to get a list of commands\n" +
            "4. "+ tRegister + " " + chef + " <code chef id> " + hrank + " <hacker rank id>" + cforce + " <codeforces id>\n" +
            "5. " + tDeregister + " - to deregister yourself\n"
            +"6. " + tList + " - to show list of users\n"
            +"For __**Superuser commands**__, add sudo. e.g.: sudo "+tDeregister+ " <name>\n";

    public Bot(String token, String server, String admin) {
        this.token = token;
        this.mServer = server;
        this.admin = admin;
    }

    public CoderManager getCoderManager() {
        return coderManager;
    }

    public void setCoderManager(CoderManager coderManager) {
        this.coderManager = coderManager;
    }

    public void init() {
        DiscordAPI discordAPI;
        discordAPI = Javacord.getApi(token,true);
        discordAPI.connect(new FutureCallback<DiscordAPI>() {
            public void onSuccess(final DiscordAPI discordAPI) {
                System.out.println("Connected to mServer");
                discordAPI.registerListener(new MessageCreateListener() {
                    public void onMessageCreate(DiscordAPI api, final Message message) {
                        if(message.getChannelReceiver() == null)
                            return;
                        if(message.getContent().startsWith("sudo")) {
                            System.out.println("List " +message.getAuthor().getRoles(discordAPI.getServerById(mServer)));
                            System.out.print("Req " + discordAPI.getServerById(mServer).getRoleById(admin));
                            if (message.getAuthor().getRoles(discordAPI.getServerById(mServer)).contains(discordAPI.getServerById(mServer).getRoleById(admin))) {
                                if (message.getContent().contains(tDeregister)) {
                                    message.reply("Deregister status " + Boolean.toString(coderManager.removeCoder(message.getContent().split(" ")[2])));
                                }
                                if (message.getContent().contains(tRegister)) {
                                    String[] arr = message.getContent().split(" ");
                                    Coder coder = new Coder();
                                    for (int i = 3; i < arr.length; i++) {
                                        getCoderProperty(coder, arr, i);
                                    }
                                    message.reply("Register status " + Boolean.toString(coderManager.addCoder(arr[2], coder)));
                                }
                            }
                            else {
                                message.reply("You are not a superuser");
                            }
                        }
                        if (message.getContent().startsWith(tEvents)) {
                            final long time = System.currentTimeMillis();
                            if(message.getContent().contains(tUpcoming)) {
                                message.reply(EventManager.printEvents(EventManager.UPCOMING), new FutureCallback<Message>() {
                                    public void onSuccess(Message message) {
                                        long delta = System.currentTimeMillis() - time;
                                        DecimalFormat formatter = new DecimalFormat("#,###.00");
                                        message.reply("Responded in " + formatter.format(delta) + "ms");
                                    }

                                    public void onFailure(Throwable throwable) {
                                        throwable.printStackTrace();
                                        message.reply("WIP");
                                    }
                                });
                            }
                            if(message.getContent().contains(tOngoing)) {
                                message.reply(EventManager.printEvents(EventManager.ONGOING), new FutureCallback<Message>() {
                                    public void onSuccess(Message message) {
                                        long delta = System.currentTimeMillis() - time;
                                        DecimalFormat formatter = new DecimalFormat("#,###.00");
                                        message.reply("Responded in " + formatter.format(delta) + "ms");
                                    }

                                    public void onFailure(Throwable throwable) {
                                        throwable.printStackTrace();
                                        message.reply("WIP");
                                    }
                                });
                            }
                        }
                        if(message.getContent().equalsIgnoreCase(tRanklist)) {
                            final long time = System.currentTimeMillis();
                            message.reply(RanklistGetter.getRank(), new FutureCallback<Message>() {
                                public void onSuccess(Message message) {
                                    long delta = System.currentTimeMillis() - time;
                                    DecimalFormat formatter = new DecimalFormat("#,###.00");
                                    message.reply("Responded in " + formatter.format(delta) + "ms");
                                }

                                public void onFailure(Throwable throwable) {
                                    throwable.printStackTrace();
                                    message.reply("Error getting result");
                                }
                            });
                        }
                        if(message.getContent().equalsIgnoreCase(tList)) {
                            for (Coder coder: coderManager.getListCoders().values()) {
                                message.reply(coder.toString());
                            }
                        }
                        if(message.getContent().equalsIgnoreCase(tRegister)) {
                            message.reply("You can register by typing in as follows:\n" +tRegister +
                                    " " + chef + " <code chef id> "+hrank+" <hacker rank id> " +
                                    cforce+" <codeforces id>", new FutureCallback<Message>() {
                                public void onSuccess(Message message) {
                                    System.out.println("sent");
                                }

                                public void onFailure(Throwable throwable) {
                                    throwable.printStackTrace(); }
                            });
                        }
                        if(message.getContent().startsWith(tRegister)) {
                            Coder coder = new Coder();
                            String com = message.getContent().toLowerCase();
                            String[] arr = com.split(" ");
                            for (int i = 0; i< arr.length; i++) {

                                getCoderProperty(coder, arr, i);
                            }
                            System.out.println(message.getAuthor().getName() + " named " + coder + " adding");
                            if(coderManager.addCoder(message.getAuthor().getName(),coder))
                                message.reply(coder+" registered successfully");
                        }
                        if(message.getContent().equalsIgnoreCase(tDeregister)) {
                            if(coderManager.removeCoder(message.getAuthor().getName()))
                                message.reply(message.getAuthor().getName() + "deregistered successfully");
                        }
                        if(message.getContent().equalsIgnoreCase(tCommands)) {
                            message.reply(commands);
                        }
                    }
                });
            }

            public void onFailure(Throwable throwable) {
                System.out.println("Error");
                throwable.printStackTrace();
            }
        });
    }

    private void getCoderProperty(Coder coder, String[] arr, int i) {
        if(arr[i].equals(chef)) {
            coder.setCodechefHandle(arr[i+1]);
        }
        if(arr[i].equals(cforce)) {
            coder.setCodeforceHandle(arr[i+1]);
        }
        if(arr[i].equals(hrank)) {
            coder.setHacker_rankHandle(arr[i+1]);
        }
    }

}
