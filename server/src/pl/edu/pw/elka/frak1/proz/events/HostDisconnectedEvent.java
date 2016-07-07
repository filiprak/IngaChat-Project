package pl.edu.pw.elka.frak1.proz.events;

import pl.edu.pw.elka.frak1.proz.controller.Event;

public class HostDisconnectedEvent extends Event {
    private String nickname;

    public HostDisconnectedEvent(String nickname) {
	this.nickname = nickname;
    }

    public String getNickname() {
	return nickname;
    }

    public void setNickname(String nickname) {
	this.nickname = nickname;
    }
}
