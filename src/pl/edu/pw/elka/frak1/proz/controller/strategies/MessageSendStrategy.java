package pl.edu.pw.elka.frak1.proz.controller.strategies;

import pl.edu.pw.elka.frak1.proz.controller.Event;
import pl.edu.pw.elka.frak1.proz.controller.Strategy;
import pl.edu.pw.elka.frak1.proz.events.MessageSendEvent;
import pl.edu.pw.elka.frak1.proz.model.Message;
import pl.edu.pw.elka.frak1.proz.model.Model;
import pl.edu.pw.elka.frak1.proz.network.Network;
import pl.edu.pw.elka.frak1.proz.view.View;

public class MessageSendStrategy implements Strategy {

    @Override
    public void instructToDo(Event event, View view, Model model, Network network) {
	if (!network.isConnected()) {
	    view.showErrorWindow(model.getErrorMessage("NoConnection"));
	    return;
	}
	MessageSendEvent e = (MessageSendEvent) event;
	Message message = e.getMessage();
	message.setAuthor(model.getClientNickname());
	network.sendMessage(message);
    }

}
