package pl.edu.pw.elka.frak1.proz.events;

import pl.edu.pw.elka.frak1.proz.controller.Event;

public class NickChangeRequestEvent extends Event {
    private String oldNickname;
    private String newNickname;
    
    public NickChangeRequestEvent(String oldNm, String newNm) {
	this.setOldNickname(oldNm);
	this.setNewNickname(newNm);
    }

    public String getOldNickname() {
	return oldNickname;
    }

    public void setOldNickname(String oldNickname) {
	this.oldNickname = oldNickname;
    }

    public String getNewNickname() {
	return newNickname;
    }

    public void setNewNickname(String newNickname) {
	this.newNickname = newNickname;
    }
}
