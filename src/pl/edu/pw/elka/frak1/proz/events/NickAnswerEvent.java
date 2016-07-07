package pl.edu.pw.elka.frak1.proz.events;

import pl.edu.pw.elka.frak1.proz.controller.Event;

public class NickAnswerEvent extends Event {
    private boolean accepted;
    private String newNickname;
    private String oldNickname;
    
    public NickAnswerEvent(String oldNm, String newNm, boolean accepted) {
	this.newNickname = newNm;
	this.oldNickname = oldNm;
	this.accepted = accepted;
    }
    
    public boolean isAccepted() {
	return accepted;
    }
    public void setAccepted(boolean accepted) {
	this.accepted = accepted;
    }
    public String getNewNickname() {
	return newNickname;
    }
    public void setNewNickname(String newNickname) {
	this.newNickname = newNickname;
    }

    public String getOldNickname() {
	return oldNickname;
    }

    public void setOldNickname(String oldNickname) {
	this.oldNickname = oldNickname;
    }
    
    
}
