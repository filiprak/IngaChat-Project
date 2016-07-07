package pl.edu.pw.elka.frak1.proz.events;

import pl.edu.pw.elka.frak1.proz.controller.Event;

public class HostReconnectedEvent extends Event {
    private String nickname, defaultNick;

    public HostReconnectedEvent(String nickname, String defNick) {
	this.nickname = nickname;
	this.setDefaultNick(defNick);
    }
    
    public String getNickname() {
	return nickname;
    }

    public void setNickname(String nickname) {
	this.nickname = nickname;
    }

    public String getDefaultNick() {
	return defaultNick;
    }

    public void setDefaultNick(String defaultNick) {
	this.defaultNick = defaultNick;
    }
}
