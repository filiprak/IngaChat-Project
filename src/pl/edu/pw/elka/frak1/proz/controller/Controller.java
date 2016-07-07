package pl.edu.pw.elka.frak1.proz.controller;

import pl.edu.pw.elka.frak1.proz.controller.strategies.*;
import pl.edu.pw.elka.frak1.proz.events.CannotConnectEvent;
import pl.edu.pw.elka.frak1.proz.events.ConnectEvent;
import pl.edu.pw.elka.frak1.proz.events.DisconnectEvent;
import pl.edu.pw.elka.frak1.proz.events.MessageReceiveEvent;
import pl.edu.pw.elka.frak1.proz.events.MessageSendEvent;
import pl.edu.pw.elka.frak1.proz.events.NewHostEvent;
import pl.edu.pw.elka.frak1.proz.events.NickAnswerEvent;
import pl.edu.pw.elka.frak1.proz.events.NickChangeRequestEvent;
import pl.edu.pw.elka.frak1.proz.events.OtherHostDisconnectedEvent;
import pl.edu.pw.elka.frak1.proz.events.SettingsChangeEvent;
import pl.edu.pw.elka.frak1.proz.events.ViewClosedEvent;
import pl.edu.pw.elka.frak1.proz.events.ViewReadySignalEvent;
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

	ConnectEvent ev = new ConnectEvent();
	eventQueue.offer((Event) ev);
	/* then start normal working */
	while (true) {
	    Event event = eventQueue.take();
	    System.out.println(event.getClass());
	    algorithmMap.get(event.getClass()).instructToDo(event, view, model, network);
	    if (event.getClass() == ViewClosedEvent.class)
		return;
	}
    }

    private void fillAlgorithmMap() {
	algorithmMap.put(MessageReceiveEvent.class, new MessageReceiveStrategy());
	algorithmMap.put(MessageSendEvent.class, new MessageSendStrategy());
	algorithmMap.put(ViewClosedEvent.class, new ViewClosedStrategy());
	algorithmMap.put(ViewReadySignalEvent.class, new ViewReadySignalStrategy());
	algorithmMap.put(ConnectEvent.class, new ConnectStrategy());
	algorithmMap.put(DisconnectEvent.class, new DisconnectStrategy());
	algorithmMap.put(CannotConnectEvent.class, new CannotConnectStrategy());
	algorithmMap.put(NewHostEvent.class, new NewHostStrategy());
	algorithmMap.put(NickAnswerEvent.class, new NickAnswerStrategy());
	algorithmMap.put(NickChangeRequestEvent.class, new NickChangeRequestStrategy());
	algorithmMap.put(OtherHostDisconnectedEvent.class, new OtherHostDisconnectedStrategy());
	algorithmMap.put(SettingsChangeEvent.class, new SettingsChangeStrategy());
    }
}
