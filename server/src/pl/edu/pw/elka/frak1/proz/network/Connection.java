package pl.edu.pw.elka.frak1.proz.network;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import pl.edu.pw.elka.frak1.proz.controller.Event;
import pl.edu.pw.elka.frak1.proz.events.HostDisconnectedEvent;
import pl.edu.pw.elka.frak1.proz.model.Message;

/**
 * Class is used to comunicate with client by socket for every connected host
 * there is one object of Connection type which enables net connection all
 * connections are held in connection list of ConnectionListener object
 * 
 */
public class Connection implements Runnable {

    private String nickname;
    private Socket socket;

    private BlockingQueue<Event> eventQueue;
    /** used to queue messages requested to send */
    private BlockingQueue<Message> messagesToSend;

    private MessageListener messageListener;
    private Thread messageListening;
    /** stream of writing output messages */
    private ObjectOutputStream outStream;
    private JSONMessageFactory jsonFactory;

    private boolean isOpened = true;

    public Connection(Socket socket, final BlockingQueue<Event> eventQueue, String nickname) {
	this.socket = socket;
	this.nickname = nickname;
	this.eventQueue = eventQueue;
	try {
	    outStream = new ObjectOutputStream(socket.getOutputStream());
	} catch (IOException e) {
	    e.printStackTrace();
	}

	jsonFactory = new JSONMessageFactory();
	messagesToSend = new LinkedBlockingQueue<Message>();
	messageListener = new MessageListener(eventQueue, messagesToSend, socket, nickname);
	messageListening = new Thread(messageListener);
	messageListening.setName("MessageListeningThread");
    }

    @Override
    public void run() {
	// first run listening of incomimg messages
	messageListening.start();

	while (isOpened) {
	    try {
		Message message = messagesToSend.take();
		if (isOpened) {
		    outStream.reset();
		    outStream.writeObject(jsonFactory.createJSONObj(message).toString());
		}

	    } catch (SocketException e) {
		HostDisconnectedEvent ev = new HostDisconnectedEvent(nickname);
		eventQueue.offer((Event) ev);
		return;
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
    }

    public void sendMessage(final Message message) {
	messagesToSend.offer(message);
    }

    /** closes this connection and it is not reconnectable anymore 
     * @throws IOException*/
    public void close() throws IOException {
	messageListener.setListening(false);
	isOpened = false;
	messagesToSend.offer(new Message("", "", "exit", true, true));
	if (!socket.isClosed())
	    socket.close();

    }

    public boolean isOpened() {
	return isOpened;
    }

    public String getNickname() {
	return nickname;
    }

    public void setNickname(String nickname) {
	this.nickname = nickname;
	messageListener.setNickname(nickname);
    }
}
