package pl.edu.pw.elka.frak1.proz.network;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;
import pl.edu.pw.elka.frak1.proz.controller.Event;
import pl.edu.pw.elka.frak1.proz.events.DisconnectEvent;
import pl.edu.pw.elka.frak1.proz.events.MessageReceiveEvent;
import pl.edu.pw.elka.frak1.proz.events.MessageSendEvent;
import pl.edu.pw.elka.frak1.proz.events.NewHostEvent;
import pl.edu.pw.elka.frak1.proz.events.NickAnswerEvent;
import pl.edu.pw.elka.frak1.proz.events.OtherHostDisconnectedEvent;
import pl.edu.pw.elka.frak1.proz.model.Message;

/**
 * class used to listen for new messages incoming in its thread system messages
 * are resolved here and proper events are fired then
 */
public class MessageListener implements Runnable {

    private static long heartbeatSendPeriod = 20;
    private static long heartbeatWaitPeriod = 40;

    private JSONMessageFactory jsonFactory;
    private BlockingQueue<Event> eventQueue;

    private Socket socket;
    /** incoming messages stream */
    private ObjectInputStream inStream;

    private HeartbeatTimer timer = null;
    private boolean isListening = true;

    public MessageListener(final BlockingQueue<Event> eventQueue, Socket socket) {
	this.eventQueue = eventQueue;
	this.socket = socket;
	this.jsonFactory = new JSONMessageFactory();
	this.timer = new HeartbeatTimer(heartbeatWaitPeriod * 1000) {

	    @Override
	    public void execute() {
		DisconnectEvent ev = new DisconnectEvent();
		eventQueue.offer((Event) ev);
		timer.stop();
	    }
	};
	try {
	    inStream = new ObjectInputStream(socket.getInputStream());
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    @Override
    public void run() {
	initHeartbeat();
	timer.start();
	while (isListening) {
	    try {
		if (socket.isClosed())
		    return;

		String buff = (String) inStream.readObject();
		Message message = jsonFactory.createMessage(buff);

		serviceMessage(message);

	    } catch (SocketException e) {
	    } catch (EOFException e) {
	    } catch (ClassNotFoundException e) {
		e.printStackTrace();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	} // while
	timer.stop();
    }

    public boolean isListening() {
	return isListening;
    }

    public void setListening(boolean isListening) {
	this.isListening = isListening;
    }

    /** checks message flags and fires proper event */
    public void serviceMessage(final Message message) {
	if (message.isSystemMessage()) {
	    /* system message flag set */
	    boolean accepted, flag;
	    accepted = message.isOwn();
	    flag = message.isWithHeader();

	    if (flag) {
		NickAnswerEvent ev = new NickAnswerEvent(message.getAuthor(), message.getContents(), accepted);
		eventQueue.offer(ev);
	    } else {
		// flag - false
		if (accepted) {
		    timer.reset();
		} else { // accepted true
		    if (message.getContents().equals("on")) {
			System.out.println("HERE !");
			NewHostEvent ev = new NewHostEvent(message.getAuthor(), true);
			eventQueue.offer((Event) ev);
		    } else if (message.getContents().equals("off")) {
			OtherHostDisconnectedEvent ev = new OtherHostDisconnectedEvent(message.getAuthor());
			eventQueue.offer((Event) ev);
		    } else if (message.getContents().equals("online")) {
			NewHostEvent ev = new NewHostEvent(message.getAuthor(), true);
			eventQueue.offer((Event) ev);
		    } else if (message.getContents().equals("offline")) {
			NewHostEvent ev = new NewHostEvent(message.getAuthor(), false);
			eventQueue.offer((Event) ev);
		    }
		}
	    }
	} else {
	    // ordinary message received
	    MessageReceiveEvent event = new MessageReceiveEvent(message);
	    eventQueue.offer((Event) event);
	}
	return;
    } // service message

    /**
     * starts a new thread for sending heartbeats heartbeats are system messages
     * contenting "heartbeat" string
     */
    public void initHeartbeat() {
	Thread heartThread = new Thread(new Runnable() {

	    @Override
	    public void run() {
		while (isListening) {
		    try {
			Message m = new Message("", "", "heartbeat", true, false);
			m.setSystemMessage(true);
			MessageSendEvent e = new MessageSendEvent(m);
			eventQueue.offer((Event) e);
			Thread.sleep(heartbeatSendPeriod * 1000);
		    } catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		    }
		}
	    }
	});
	heartThread.start();
    }
}
