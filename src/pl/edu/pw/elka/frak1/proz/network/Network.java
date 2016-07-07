package pl.edu.pw.elka.frak1.proz.network;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import pl.edu.pw.elka.frak1.proz.controller.Event;
import pl.edu.pw.elka.frak1.proz.model.Message;

/** Module used to deal with net service overall */
public class Network {
    /** current net connection of client */
    private Connection connection;
    private Thread connectionThread;
    private BlockingQueue<Event> eventQueue;
    
    public Network(final BlockingQueue<Event> eventQueue) {
	this.eventQueue = eventQueue;
    }
    
    public boolean isConnected() {
	if (connection == null) return false;
	return connection.isConnected();
    }
    
    public void sendMessage(final Message message) {
	connection.sendMessage(message);
    }
    
    public void closeConnection() throws IOException {
	connection.close();
    }
    
    public static String getServerAddress() {
        return Connection.getServerAddress();
    }

    public static void setServerAddress(String serverAddress) {
        Connection.setServerAddress(serverAddress);
    }

    public static int getServerPortNr() {
        return Connection.getServerPortNr();
    }

    public static void setServerPortNr(int serverPortNr) {
        Connection.setServerPortNr(serverPortNr);
    }
    
    public boolean tryToConnect() {
	connection = new Connection(eventQueue);
	connectionThread = new Thread(connection);
	connectionThread.setName("ConnectionThread");
	connectionThread.start();
	while(connectionThread.isAlive())
	    if(connection.isConnected()) return true;
	return false;
    }
}
