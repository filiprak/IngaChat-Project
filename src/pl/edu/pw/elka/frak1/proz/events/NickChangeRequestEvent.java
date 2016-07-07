package pl.edu.pw.elka.frak1.proz.events;

import pl.edu.pw.elka.frak1.proz.controller.Event;

public class NickChangeRequestEvent extends Event {
    private String newNickname;

    public NickChangeRequestEvent(String newNm) {
	this.setNewNickname(newNm);
    }

    public String getNewNickname() {
	return newNickname;
    }

    public void setNewNickname(String newNickname) {
	this.newNickname = newNickname;
    }
}
