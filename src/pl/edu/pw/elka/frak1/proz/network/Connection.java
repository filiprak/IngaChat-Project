package pl.edu.pw.elka.frak1.proz.network;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import pl.edu.pw.elka.frak1.proz.controller.Event;
import pl.edu.pw.elka.frak1.proz.events.CannotConnectEvent;
import pl.edu.pw.elka.frak1.proz.model.Message;

/**
 * class is used to service disposable connection with server it is created only
 * one time for one session, if user disconnects new object of this class must
 * be created to reconnect with server
 */
public class Connection implements Runnable {

    /** parametres of connection */
    private static String serverAddress = "127.0.0.1";
    private static int serverPortNr = 55544;

    private JSONMessageFactory jsonFactory;
    private Socket clientSocket;

    /** stream to write users messages */
    private ObjectOutputStream outStream;
    /** used to queue messages requested to send */
    private BlockingQueue<Message> messagesToSend;
    private BlockingQueue<Event> eventQueue;

    private static MessageListener messageListener;
    private Thread messageListeningThread;

    private boolean isConnected = false;

    public Connection(final BlockingQueue<Event> eventQueue) {
	this.eventQueue = eventQueue;
	jsonFactory = new JSONMessageFactory();
	messagesToSend = new LinkedBlockingQueue<Message>();
    }

    @Override
    public void run() {
	try {
	    clientSocket = new Socket();
	    clientSocket.connect(new InetSocketAddress(serverAddress, serverPortNr), 20000);

	    if (clientSocket != null) {
		isConnected = true;

		messageListener = new MessageListener(eventQueue, clientSocket);

		messageListeningThread = new Thread(messageListener);
		messageListeningThread.start();

		outStream = new ObjectOutputStream(clientSocket.getOutputStream());
	    }

	} catch (UnknownHostException e) {
	    CannotConnectEvent event = new CannotConnectEvent();
	    isConnected = false;
	    eventQueue.offer(event);
	    return;

	} catch (ConnectException | SocketTimeoutException e) {
	    CannotConnectEvent event = new CannotConnectEvent();
	    isConnected = false;
	    eventQueue.offer(event);
	    return;

	} catch (IOException e) {
	    isConnected = false;
	    e.printStackTrace();
	    return;
	}

	while (isConnected) {
	    try {
		Message message = messagesToSend.take();

		outStream.reset();
		outStream.writeObject(jsonFactory.createJSONObj(message).toString());

		System.out.println("sended:" + jsonFactory.createJSONObj(message).toString());
	    } catch (SocketException e) {
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
	if (!clientSocket.isClosed())
	    try {
		clientSocket.close();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
    }

    public void sendMessage(final Message message) {
	messagesToSend.offer(message);
    }

    public void close() {
	// wait until all messages will be send then push poison pill (empty
	// message)
	while (!messagesToSend.isEmpty())
	    ;

	if (messageListener != null)
	    messageListener.setListening(false);
	isConnected = false;
	// poison pill
	messagesToSend.offer(new Message("", "poison_pill", "", false, false));
    }

    public void setConnected(boolean isConnected) {
	this.isConnected = isConnected;
    }

    public boolean isConnected() {
	return isConnected;
    }

    public static String getServerAddress() {
	return serverAddress;
    }

    public static void setServerAddress(String serverAddress) {
	Connection.serverAddress = serverAddress;
    }

    public static int getServerPortNr() {
	return serverPortNr;
    }

    public static void setServerPortNr(int serverPortNr) {
	Connection.serverPortNr = serverPortNr;
    }
}
