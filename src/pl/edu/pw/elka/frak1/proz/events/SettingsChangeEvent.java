package pl.edu.pw.elka.frak1.proz.events;

import pl.edu.pw.elka.frak1.proz.controller.Event;

public class SettingsChangeEvent extends Event {
    private String newIpAddress;
    private Integer newPortNr;
    
    public SettingsChangeEvent(String ip, Integer port) {
	this.newIpAddress = ip;
	this.newPortNr = port;
    }
    
    public String getNewIpAddress() {
	return newIpAddress;
    }
    public void setNewIpAddress(String newIpAddress) {
	this.newIpAddress = newIpAddress;
    }
    public Integer getNewPortNr() {
	return newPortNr;
    }
    public void setNewPortNr(Integer newPortNr) {
	this.newPortNr = newPortNr;
    }
}
