package pl.edu.pw.elka.frak1.proz.controller.strategies;

import pl.edu.pw.elka.frak1.proz.controller.Event;
import pl.edu.pw.elka.frak1.proz.controller.Strategy;
import pl.edu.pw.elka.frak1.proz.events.HostReconnectedEvent;
import pl.edu.pw.elka.frak1.proz.events.NewHostEvent;
import pl.edu.pw.elka.frak1.proz.model.Message;
import pl.edu.pw.elka.frak1.proz.model.Model;
import pl.edu.pw.elka.frak1.proz.model.UserHost;
import pl.edu.pw.elka.frak1.proz.network.Network;
import pl.edu.pw.elka.frak1.proz.view.View;

public class HostReconnectedStrategy implements Strategy {

    @Override
    public void instructToDo(Event event, View view, Model model, Network network) {
	HostReconnectedEvent e = (HostReconnectedEvent) event;

	if (model.containsUser(e.getNickname()))
	    if (!model.getUserHost(e.getNickname()).getIsOnline()) {

		model.setUserHostStatus(model.getUserHost(e.getNickname()), true);
		view.setUserStatus(e.getNickname(), true);

		Message sysMessage = new Message(e.getNickname(), "", "on", false, false);
		sysMessage.setSystemMessage(true);

		network.sendMessageToAll(sysMessage);
		network.renameConnection(e.getDefaultNick(), e.getNickname());

		// copy user list to new
		for (UserHost user : model.getUserList()) {
		    String online = "offline";
		    if (user.getNickname().equals(e.getNickname()))
			continue;
		    if (user.getIsOnline())
			online = "online";
		    Message sysMessage1 = new Message(user.getNickname(), "", online, false, false);
		    sysMessage1.setSystemMessage(true);
		    network.sendMessage(sysMessage1, e.getNickname());
		}
		return;
	    }

	// if hostname is already used by sb
	NewHostEvent ev = new NewHostEvent(e.getDefaultNick(), e.getNickname());
	network.getEventQueue().offer((Event) ev);

    }

}
