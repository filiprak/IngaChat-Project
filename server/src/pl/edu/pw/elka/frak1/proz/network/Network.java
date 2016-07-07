package pl.edu.pw.elka.frak1.proz.network;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import pl.edu.pw.elka.frak1.proz.controller.Event;
import pl.edu.pw.elka.frak1.proz.model.Message;

/**
 * Module used to deal with net connection contains connection listener and
 * reference on eventqueue
 */
public class Network {
    private ConnectionListener connectionListener;
    private Thread connectionListenerThread;
    private BlockingQueue<Event> eventQueue;

    public Network(final BlockingQueue<Event> eventQueue) {
	this.eventQueue = eventQueue;
    }

    /** creates new connection listener and its thread */
    public void init() {
	connectionListener = new ConnectionListener();
	connectionListenerThread = new Thread(connectionListener);
	connectionListenerThread.setName("ConnectionListenerThread");
	connectionListener.setEventQueue(eventQueue);
    }

    /** starts listening thread */
    public void startListening() {
	connectionListenerThread.start();
    }

    /** closes all connections currently running 
     * @throws IOException*/
    public void closeNetwork() throws IOException {
	if (connectionListener == null)
	    return;

	for (Connection current : connectionListener.getConnectionList())
	    current.close();
	connectionListener.exit();
    }

    public void closeConnection(final String nickname) throws IOException {
	connectionListener.closeConnection(nickname);
    }

    public ConnectionListener getConnectionListener() {
	return connectionListener;
    }

    public void sendMessageToAll(final Message message) {
	for (Connection current : connectionListener.getConnectionList())
	    current.sendMessage(message);
    }

    public void sendMessageToAllExcept(final Message message, String nickname) {
	for (Connection current : connectionListener.getConnectionList())
	    if (!current.getNickname().equals(nickname))
		current.sendMessage(message);
    }

    public void sendMessage(final Message message, String nickname) {
	for (Connection current : connectionListener.getConnectionList())
	    if (current.getNickname().equals(nickname)) {
		current.sendMessage(message);
		return;
	    }
    }

    public void renameConnection(String oldNm, String newNm) {
	for (Connection current : connectionListener.getConnectionList())
	    if (current.getNickname().equals(oldNm)) {
		current.setNickname(newNm);
		return;
	    }
    }

    public BlockingQueue<Event> getEventQueue() {
	return eventQueue;
    }

    public void setPort(Integer portnr) {
	connectionListener.setPort(portnr);
    }

    public boolean isListening() {
	return connectionListener.isListening();
    }
}
