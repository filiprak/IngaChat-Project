package pl.edu.pw.elka.frak1.proz.events;

import pl.edu.pw.elka.frak1.proz.controller.Event;
import pl.edu.pw.elka.frak1.proz.model.Message;

/** message receive event */
public class MessageReceiveEvent extends Event {
    private final Message message;

    public MessageReceiveEvent(final Message messageToSend) {
	this.message = messageToSend;
    }

    public Message getMessage() {
	return message;
    }
}
