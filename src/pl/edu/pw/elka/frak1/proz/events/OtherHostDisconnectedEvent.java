package pl.edu.pw.elka.frak1.proz.events;

import pl.edu.pw.elka.frak1.proz.controller.Event;

public class OtherHostDisconnectedEvent extends Event {
    private String nickname;
    
    public OtherHostDisconnectedEvent(String nickname) {
	this.setNickname(nickname);
    }

    public String getNickname() {
	return nickname;
    }

    public void setNickname(String nickname) {
	this.nickname = nickname;
    }
}
