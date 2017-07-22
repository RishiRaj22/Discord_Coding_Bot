package me.itsrishi.bot.discord.coding.coder;

import java.io.*;
import java.util.HashMap;

/**
 * @author Rishi Raj on 12-05-2017.
 */
public class CoderManager {

    private HashMap<String,Coder> listCoders;
    public boolean addCoder(String name, Coder coder) {
        listCoders.put(name,coder);
        return write();
    }
    public boolean removeCoder(String name) {
        if(listCoders.containsKey(name)) {
            listCoders.remove(name);
            return write();
        }
        return false;
    }
    public void init() {
        read();
    }
    private boolean read() {
        try {
            if(new File("users.dat").exists()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream("users.dat"));
                listCoders = (HashMap<String, Coder>) ois.readObject();
                ois.close();
            }
            else {
                listCoders = new HashMap<String, Coder>(2000);
                System.out.println("File created");
            }
            return true;
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return false;
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    private boolean write() {
        try {
            OutputStream os = new FileOutputStream("users.dat");
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(listCoders);
            System.out.println("list of coders written");
            oos.flush();
            oos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public HashMap<String, Coder> getListCoders() {
        return listCoders;
    }

}
