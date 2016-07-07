package pl.edu.pw.elka.frak1.proz.controller.strategies;

import pl.edu.pw.elka.frak1.proz.controller.Event;
import pl.edu.pw.elka.frak1.proz.controller.Strategy;
import pl.edu.pw.elka.frak1.proz.events.NewHostEvent;
import pl.edu.pw.elka.frak1.proz.model.Model;
import pl.edu.pw.elka.frak1.proz.model.UserHost;
import pl.edu.pw.elka.frak1.proz.network.Network;
import pl.edu.pw.elka.frak1.proz.view.View;

public class NewHostStrategy implements Strategy {

    @Override
    public void instructToDo(Event event, View view, Model model, Network network) {
	NewHostEvent ev = (NewHostEvent) event;

	if (ev.getNickname().equals(model.getClientNickname())) {
	    if (!model.containsUser(ev.getNickname())) {
		UserHost thisClient = new UserHost(ev.getNickname(), ev.isOnline());
		model.addNewUser(thisClient);
		view.addNewUser(ev.getNickname(), ev.isOnline());
		model.setUserHostClient(thisClient);
	    } else {
		model.getUserHostClient().setIsOnline(true);
		view.setUserStatus(ev.getNickname(), true);
	    }
	    return;
	}
	if (model.containsUser(ev.getNickname())) {
	    if (!model.getUserHost(ev.getNickname()).getIsOnline()) {
		model.setUserHostStatus(model.getUserHost(ev.getNickname()), ev.isOnline());
		view.setUserStatus(ev.getNickname(), ev.isOnline());
		return;
	    }
	} else {
	    model.addNewUser(new UserHost(ev.getNickname(), ev.isOnline()));
	    view.addNewUser(ev.getNickname(), ev.isOnline());
	}
    }

}
