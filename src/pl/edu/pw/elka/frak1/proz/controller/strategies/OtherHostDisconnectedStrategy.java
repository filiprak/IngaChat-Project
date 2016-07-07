package pl.edu.pw.elka.frak1.proz.controller.strategies;

import pl.edu.pw.elka.frak1.proz.controller.Event;
import pl.edu.pw.elka.frak1.proz.controller.Strategy;
import pl.edu.pw.elka.frak1.proz.events.OtherHostDisconnectedEvent;
import pl.edu.pw.elka.frak1.proz.model.Model;
import pl.edu.pw.elka.frak1.proz.network.Network;
import pl.edu.pw.elka.frak1.proz.view.View;

public class OtherHostDisconnectedStrategy implements Strategy {

    @Override
    public void instructToDo(Event event, View view, Model model, Network network) {
	OtherHostDisconnectedEvent e = (OtherHostDisconnectedEvent) event;

	if (model.getUserHost(e.getNickname()) == null)
	    return;

	model.setUserHostStatus(model.getUserHost(e.getNickname()), false);
	view.setUserStatus(e.getNickname(), false);
    }

}
