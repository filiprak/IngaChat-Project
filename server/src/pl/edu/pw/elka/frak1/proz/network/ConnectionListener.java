package pl.edu.pw.elka.frak1.proz.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import pl.edu.pw.elka.frak1.proz.controller.Event;
import pl.edu.pw.elka.frak1.proz.events.ServerSocketOpenErrorEvent;

/**
 * class enables adding new coming host connections it listens constantly for
 * new connections and creates new Connection object if some new host comes it
 * contains list of currently running connections
 */
public class ConnectionListener implements Runnable {
    /** index of new users default nicknames */
    static int index = 0;
    /** port number of server listening */
    static int serverPortNr = 55544;

    private ServerSocket serverSocket = null;
    private Socket socket = null;

    private BlockingQueue<Event> eventQueue;
    private boolean isListening = false;

    private ArrayList<Connection> connectionList = new ArrayList<Connection>();

    @Override
    public void run() {
	try {
	    // listen to new connection requests
	    serverSocket = new ServerSocket(serverPortNr);
	    isListening = true;
	} catch (IOException e) {
	    isListening = false;
	    ServerSocketOpenErrorEvent event = new ServerSocketOpenErrorEvent();
	    eventQueue.offer((Event) event);
	    return;
	}

	while (isListening) {
	    try {
		socket = serverSocket.accept();
	    } catch (IOException e) {
	    }
	    if (!isListening)
		break;

	    // new thread for a client
	    String nickname = getDefaultNick();

	    Connection connection = new Connection(socket, eventQueue, nickname);
	    new Thread(connection).start();
	    connectionList.add(connection);
	}
    }

    public BlockingQueue<Event> getEventQueue() {
	return eventQueue;
    }

    public void setEventQueue(BlockingQueue<Event> eventQueue) {
	this.eventQueue = eventQueue;
    }

    /** stops listening and closes socket 
     * @throws IOException*/
    public void exit() throws IOException {
	isListening = false;
	if (serverSocket != null)
	    if (!serverSocket.isClosed())
		serverSocket.close();
	connectionList.clear();
    }

    public String getDefaultNick() {
	String userDefName = "user00" + index;
	index++;
	return userDefName;
    }

    public ArrayList<Connection> getConnectionList() {
	return connectionList;
    }

    /** closes one connection of given nickname user 
     * @throws IOException*/
    public boolean closeConnection(final String nickname) throws IOException {
	for (Connection c : connectionList)
	    if (c.getNickname().equals(nickname)) {
		c.close();
		connectionList.remove(c);
		return true;
	    }
	return false;
    }

    public void setPort(Integer portnr) {
	serverPortNr = portnr.intValue();
    }

    public boolean isListening() {
	return isListening;
    }
}
