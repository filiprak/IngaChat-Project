package pl.edu.pw.elka.frak1.proz.events;

import pl.edu.pw.elka.frak1.proz.controller.Event;

public class NewHostEvent extends Event {
    private String nickname;
    private String reconnectNick;

    public NewHostEvent(String nickname, String recNick) {
	this.nickname = nickname;
	this.reconnectNick = recNick;
    }
    
    public String getNickname() {
	return nickname;
    }

    public void setNickname(String nickname) {
	this.nickname = nickname;
    }

    public String getReconnectNick() {
	return reconnectNick;
    }

    public void setReconnectNick(String reconnectNick) {
	this.reconnectNick = reconnectNick;
    }
}
