package user;

import behaviors.interfaces.ReadCodeBehavior;

import java.util.List;

public class Account {

    private String socialMediaType;

    private String name;

    private List<String> userCodes;

    private ReadCodeBehavior readCodeBehavior;

    public void setReadCodeBehavior(ReadCodeBehavior newBehavior) {
        this.readCodeBehavior = newBehavior;
    }

    public void addCodes() {
        this.userCodes.addAll(readCodeBehavior.readCodes());
    }

    public String getName() {
        return name;
    }

    public String getSocialMediaType() {
        return socialMediaType;
    }

    public void clearUserCodes() {
        this.userCodes.clear();
    }
}
