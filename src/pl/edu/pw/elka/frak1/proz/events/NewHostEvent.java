package pl.edu.pw.elka.frak1.proz.events;

import pl.edu.pw.elka.frak1.proz.controller.Event;

public class NewHostEvent extends Event {
    private String nickname;
    private boolean online;

    public NewHostEvent(String nickname, boolean online) {
	this.nickname = nickname;
	this.online = online;
    }

    public String getNickname() {
	return nickname;
    }

    public void setNickname(String nickname) {
	this.nickname = nickname;
    }

    public boolean isOnline() {
	return online;
    }

    public void setOnline(boolean online) {
	this.online = online;
    }
}
