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
	boolean reconnected = false;
	if (e.getNewNickname().equals(""))
	    reconnected = true;
	Message message = new Message(model.getClientNickname(), "", e.getNewNickname(), !reconnected, true);
	message.setSystemMessage(true);
	network.sendMessage(message);
    }

}
