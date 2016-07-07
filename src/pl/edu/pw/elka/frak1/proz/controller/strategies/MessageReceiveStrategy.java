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
	MessageReceiveEvent ev = (MessageReceiveEvent) event;

	Message message = ev.getMessage();

	if (message.getAuthor().equals(model.getClientNickname())) {
	    message.setIsOwn(true);
	} else {
	    message.setIsOwn(false);
	}
	message.setWithHeader(!model.isLastMessageSameAuthor(message.getAuthor()));

	model.addNewMessage(message);
	view.addNewMessage(message);
	view.grabUserAttention();
    }

}
