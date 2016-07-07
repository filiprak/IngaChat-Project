package pl.edu.pw.elka.frak1.proz.network;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;
import pl.edu.pw.elka.frak1.proz.controller.Event;
import pl.edu.pw.elka.frak1.proz.events.HostDisconnectedEvent;
import pl.edu.pw.elka.frak1.proz.events.HostReconnectedEvent;
import pl.edu.pw.elka.frak1.proz.events.MessageReceiveEvent;
import pl.edu.pw.elka.frak1.proz.events.NewHostEvent;
import pl.edu.pw.elka.frak1.proz.events.NickChangeRequestEvent;
import pl.edu.pw.elka.frak1.proz.model.Message;

/**
 * class used to listen for new messages incoming in its thread system messages
 * are resolved here and proper events are fired then
 */
public class MessageListener implements Runnable {

    private static long heartbeatSendPeriod = 20;
    private static long heartbeatWaitPeriod = 40;
    private String nickname;
    private Socket socket;

    private BlockingQueue<Event> eventQueue;

    /** queue of messages requested to send */
    private BlockingQueue<Message> messagesToSend;

    private JSONMessageFactory jsonFactory;
    private HeartbeatTimer timer;
    /** stream of incoming messages */
    private ObjectInputStream inStream;
    private boolean isListening = false;

    public MessageListener(final BlockingQueue<Event> eventQueue, final BlockingQueue<Message> messToSend,
	    Socket socket, String nickname) {
	this.eventQueue = eventQueue;
	this.messagesToSend = messToSend;
	this.socket = socket;
	this.nickname = nickname;
	this.timer = new HeartbeatTimer(heartbeatWaitPeriod * 1000) {

	    @Override
	    public void execute() {
		HostDisconnectedEvent ev = new HostDisconnectedEvent(nickname);
		eventQueue.offer((Event) ev);
		timer.stop();
	    }
	};

	jsonFactory = new JSONMessageFactory();
	try {
	    inStream = new ObjectInputStream(socket.getInputStream());
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    @Override
    public void run() {
	// first init heartbeat
	initHeartbeat();
	timer.start();
	isListening = true;
	while (isListening) {
	    try {
		if (socket.isClosed())
		    return;

		Message message = jsonFactory.createMessage((String) inStream.readObject());

		serviceMessage(message);

	    } catch (SocketException e) {
	    } catch (ClassNotFoundException e) {
		e.printStackTrace();
	    } catch (EOFException e) {
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
	timer.stop();
	System.out.println("mess listener end");
    }

    public boolean isListening() {
	return isListening;
    }

    public void setListening(boolean isListening) {
	this.isListening = isListening;
    }

    public String getNickname() {
	return nickname;
    }

    public void setNickname(String nickname) {
	this.nickname = nickname;
    }

    /** checks message flags and fires proper event */
    private void serviceMessage(final Message message) {
	if (message.isSystemMessage()) {
	    /* system message flag set */
	    boolean accepted, flag;
	    accepted = message.isOwn();
	    flag = message.isWithHeader();
	    if (flag) {
		if (accepted) { // true true
		    if (message.getAuthor().equals("")) {
			NewHostEvent ev = new NewHostEvent(nickname, "");
			eventQueue.offer(ev);
		    } else {
			NickChangeRequestEvent ev = new NickChangeRequestEvent(message.getAuthor(),
				message.getContents());
			eventQueue.offer((Event) ev);
		    }
		} else { // false true
		    HostReconnectedEvent ev = new HostReconnectedEvent(message.getAuthor(), nickname);
		    eventQueue.offer((Event) ev);
		}
	    } else {

		if (accepted) { // true false
		    timer.reset();
		} else { // false false
		    HostDisconnectedEvent ev1 = new HostDisconnectedEvent(message.getAuthor());
		    eventQueue.offer(ev1);
		}
	    }
	} else { // ordinary message
	    MessageReceiveEvent event = new MessageReceiveEvent(message);
	    eventQueue.offer((Event) event);
	}
	return;
    }

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
			messagesToSend.offer(m);
			Thread.sleep(heartbeatSendPeriod * 1000);
		    } catch (InterruptedException e1) {
			e1.printStackTrace();
		    }
		}
	    }
	});
	heartThread.start();
    }

}
