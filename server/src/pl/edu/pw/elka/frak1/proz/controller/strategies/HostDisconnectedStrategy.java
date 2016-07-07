package pl.edu.pw.elka.frak1.proz.controller.strategies;

import java.io.IOException;

import pl.edu.pw.elka.frak1.proz.controller.Event;
import pl.edu.pw.elka.frak1.proz.controller.Strategy;
import pl.edu.pw.elka.frak1.proz.events.HostDisconnectedEvent;
import pl.edu.pw.elka.frak1.proz.model.Message;
import pl.edu.pw.elka.frak1.proz.model.Model;
import pl.edu.pw.elka.frak1.proz.network.Network;
import pl.edu.pw.elka.frak1.proz.view.View;

public class HostDisconnectedStrategy implements Strategy {

    @Override
    public void instructToDo(Event event, View view, Model model, Network network) {
	HostDisconnectedEvent e = (HostDisconnectedEvent) event;

	Message sysMessage = new Message(e.getNickname(), "", "off", false, false);
	sysMessage.setSystemMessage(true);

	network.sendMessageToAll(sysMessage);

	try {
	    network.closeConnection(e.getNickname());
	} catch (IOException e1) {
	    e1.printStackTrace();
	}

	model.setUserHostStatus(model.getUserHost(e.getNickname()), false);
	view.setUserStatus(e.getNickname(), false);
    }
}
