package pl.edu.pw.elka.frak1.proz.events;

import pl.edu.pw.elka.frak1.proz.controller.Event;
import pl.edu.pw.elka.frak1.proz.model.Message;

/** message send from client event */
public class MessageSendEvent extends Event {
    private Message message;

    public MessageSendEvent(Message message) {
	this.message = message;
    }

    public Message getMessage() {
	return message;
    }
}
