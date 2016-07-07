package pl.edu.pw.elka.frak1.proz.controller.strategies;

import pl.edu.pw.elka.frak1.proz.controller.Event;
import pl.edu.pw.elka.frak1.proz.controller.Strategy;
import pl.edu.pw.elka.frak1.proz.events.NewHostEvent;
import pl.edu.pw.elka.frak1.proz.model.Message;
import pl.edu.pw.elka.frak1.proz.model.Model;
import pl.edu.pw.elka.frak1.proz.model.UserHost;
import pl.edu.pw.elka.frak1.proz.network.Network;
import pl.edu.pw.elka.frak1.proz.view.View;

public class NewHostStrategy implements Strategy {

    @Override
    public void instructToDo(Event event, View view, Model model, Network network) {
	// call everybody that new cient is on
	NewHostEvent ev = (NewHostEvent) event;

	Message nickAnswerMessage = new Message(ev.getReconnectNick(), model.getCurrentDateTime(), ev.getNickname(),
		true, true);
	nickAnswerMessage.setSystemMessage(true);
	network.sendMessage(nickAnswerMessage, ev.getNickname());

	// copy user list to new
	for (UserHost user : model.getUserList()) {
	    String online = "offline";
	    if (user.getNickname().equals(ev.getNickname()))
		continue;
	    if (user.getIsOnline())
		online = "online";
	    Message sysMessage1 = new Message(user.getNickname(), "", online, false, false);
	    sysMessage1.setSystemMessage(true);
	    network.sendMessage(sysMessage1, ev.getNickname());
	}

	if (!model.containsUser(ev.getNickname())) {
	    model.addNewUser(new UserHost(ev.getNickname(), true));
	    view.addNewUser(ev.getNickname(), true);
	} else if (!model.getUserHost(ev.getNickname()).getIsOnline()) {
	    model.setUserHostStatus(model.getUserHost(ev.getNickname()), true);
	    view.setUserStatus(ev.getNickname(), true);
	}

	Message sysMessage = new Message(ev.getNickname(), model.getCurrentDateTime(), "on", false, false);
	sysMessage.setSystemMessage(true);
	network.sendMessageToAll(sysMessage);

    }

}
