package pl.edu.pw.elka.frak1.proz.controller;

import pl.edu.pw.elka.frak1.proz.controller.strategies.*;
import pl.edu.pw.elka.frak1.proz.events.*;
import pl.edu.pw.elka.frak1.proz.model.Model;
import pl.edu.pw.elka.frak1.proz.network.Network;
import pl.edu.pw.elka.frak1.proz.view.View;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

/**
 * Controller in MVC architecture is responsible for dealing with events by
 * doing strategies instruction
 */
public class Controller {
    // components
    private final View view;
    private final Model model;
    private final Network network;
    private final BlockingQueue<Event> eventQueue;

    /** strategy hashmap contains pairs of class type and its strategy */
    private final HashMap<Class<? extends Event>, Strategy> algorithmMap;

    public Controller(final BlockingQueue<Event> eventQueue, View view, Model model, Network network) {
	this.view = view;
	this.model = model;
	this.network = network;
	this.eventQueue = eventQueue;
	this.algorithmMap = new HashMap<Class<? extends Event>, Strategy>();
	fillAlgorithmMap();
    }

    public void run() throws InterruptedException {
	/* first wait for view to init */
	while (true) {
	    Event event = eventQueue.take();
	    if (event.getClass() == ViewReadySignalEvent.class) {
		algorithmMap.get(event.getClass()).instructToDo(event, view, model, network);
		break;
	    }
	}
	/* then start normal working */
	while (true) {
	    Event event = eventQueue.take();
	    System.out.println(event.getClass()); //log check
	    model.printModelState();
	    algorithmMap.get(event.getClass()).instructToDo(event, view, model, network);
	    if (event.getClass() == ViewClosedEvent.class)
		return;
	}
    }

    private void fillAlgorithmMap() {
	algorithmMap.put(MessageReceiveEvent.class, new MessageReceiveStrategy());
	algorithmMap.put(ViewClosedEvent.class, new ViewClosedStrategy());
	algorithmMap.put(ViewReadySignalEvent.class, new ViewReadySignalStrategy());
	algorithmMap.put(ServerSocketOpenErrorEvent.class, new ServerSocketOpenErrorStrategy());
	algorithmMap.put(NewHostEvent.class, new NewHostStrategy());
	algorithmMap.put(HostDisconnectedEvent.class, new HostDisconnectedStrategy());
	algorithmMap.put(NickChangeRequestEvent.class, new NickChangeRequestStrategy());
	algorithmMap.put(HostReconnectedEvent.class, new HostReconnectedStrategy());
	algorithmMap.put(PortSetEvent.class, new PortSetStrategy());
    }
}
