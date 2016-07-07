package pl.edu.pw.elka.frak1.proz.controller.strategies;

import java.io.IOException;

import pl.edu.pw.elka.frak1.proz.controller.Event;
import pl.edu.pw.elka.frak1.proz.controller.Strategy;
import pl.edu.pw.elka.frak1.proz.events.PortSetEvent;
import pl.edu.pw.elka.frak1.proz.model.Model;
import pl.edu.pw.elka.frak1.proz.network.Network;
import pl.edu.pw.elka.frak1.proz.view.View;

public class PortSetStrategy implements Strategy {

    @Override
    public void instructToDo(Event event, View view, Model model, Network network) {
	PortSetEvent ev = (PortSetEvent) event;
	if (network.isListening())
	    try {
		network.closeNetwork();
		model.clearMessageList();
		model.clearUserList();
		view.clearAll();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	network.init();
	network.setPort(ev.getPortnr());
	network.startListening();
    }

}
