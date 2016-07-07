package pl.edu.pw.elka.frak1.proz.events;

import pl.edu.pw.elka.frak1.proz.controller.Event;

public class PortSetEvent extends Event {
    private Integer portnr;
    
    public PortSetEvent(Integer portnr) {
	this.setPortnr(portnr);
    }

    public Integer getPortnr() {
	return portnr;
    }

    public void setPortnr(Integer portnr) {
	this.portnr = portnr;
    }
    
}
