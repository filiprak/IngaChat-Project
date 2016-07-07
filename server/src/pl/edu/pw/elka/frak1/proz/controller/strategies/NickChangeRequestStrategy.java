package pl.edu.pw.elka.frak1.proz.controller.strategies;

import pl.edu.pw.elka.frak1.proz.controller.Event;
import pl.edu.pw.elka.frak1.proz.controller.Strategy;
import pl.edu.pw.elka.frak1.proz.events.NickChangeRequestEvent;
import pl.edu.pw.elka.frak1.proz.model.Message;
import pl.edu.pw.elka.frak1.proz.model.Model;
import pl.edu.pw.elka.frak1.proz.network.Network;
import pl.edu.pw.elka.frak1.proz.view.View;

public class NickChangeRequestStrategy implements Strategy {

    @Override
    public void instructToDo(Event event, View view, Model model, Network network) {
	NickChangeRequestEvent e = (NickChangeRequestEvent) event;
	boolean accepted = true;
	if (model.containsUser(e.getNewNickname()))
	    accepted = false;
	Message message = new Message(e.getOldNickname(), model.getCurrentDateTime(), e.getNewNickname(), accepted,
		true);
	message.setSystemMessage(true);

	if (accepted) {
	    model.setNewClientNickname(e.getOldNickname(), e.getNewNickname());
	    view.renameUser(e.getOldNickname(), e.getNewNickname());
	    network.renameConnection(e.getOldNickname(), e.getNewNickname());
	    network.sendMessageToAll(message);
	} else {
	    network.sendMessage(message, e.getOldNickname());
	}
    }

}
