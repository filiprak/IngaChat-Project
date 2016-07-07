package pl.edu.pw.elka.frak1.proz.controller.strategies;

import pl.edu.pw.elka.frak1.proz.controller.Event;
import pl.edu.pw.elka.frak1.proz.controller.Strategy;
import pl.edu.pw.elka.frak1.proz.events.MessageReceiveEvent;
import pl.edu.pw.elka.frak1.proz.model.Message;
import pl.edu.pw.elka.frak1.proz.model.Model;
import pl.edu.pw.elka.frak1.proz.network.Network;
import pl.edu.pw.elka.frak1.proz.view.View;

public class MessageReceiveStrategy implements Strategy {

    @Override
    public void instructToDo(Event event, View view, Model model, Network network) {
	MessageReceiveEvent e = (MessageReceiveEvent) event;
	Message message = e.getMessage();
	message.setTime(model.getCurrentDateTime());

	if (!message.isSystemMessage())
	    network.sendMessageToAll(message);
	model.addNewMessage(e.getMessage());
	view.addNewMessage(e.getMessage());
    }

}
