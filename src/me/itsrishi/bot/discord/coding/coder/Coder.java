package me.itsrishi.bot.discord.coding.coder;

import java.io.Serializable;

/**
 * @author Rishi Raj on 12-05-2017.
 */
public class Coder implements Serializable {
    static final long serialVersionUID = 42L;
    private String codechefHandle;
    private String hacker_rankHandle;
    private String codeforceHandle;

    public String getCodechefHandle() {
        return codechefHandle;
    }

    public void setCodechefHandle(String codechefHandle) {
        this.codechefHandle = codechefHandle;
    }

    public String getHacker_rankHandle() {
        return hacker_rankHandle;
    }

    public void setHacker_rankHandle(String hacker_rankHandle) {
        this.hacker_rankHandle = hacker_rankHandle;
    }

    public String getCodeforceHandle() {
        return codeforceHandle;
    }

    public void setCodeforceHandle(String codeforceHandle) {
        this.codeforceHandle = codeforceHandle;
    }

    @Override
    public String toString() {
        return "Coder:\ncodechef "+codechefHandle + "\nhacker rank "+hacker_rankHandle+"\ncodeforces "+codeforceHandle;
    }
}
