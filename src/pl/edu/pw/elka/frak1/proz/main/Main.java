package pl.edu.pw.elka.frak1.proz.main;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import pl.edu.pw.elka.frak1.proz.controller.Controller;
import pl.edu.pw.elka.frak1.proz.controller.Event;
import pl.edu.pw.elka.frak1.proz.model.Model;
import pl.edu.pw.elka.frak1.proz.network.Network;
import pl.edu.pw.elka.frak1.proz.view.*;

/** Main class to init whole program */
public class Main {
    /**
     * main method of the program
     */
    public static void main(String[] args) throws InterruptedException {
	BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<Event>();

	/* MVC structure */
	Model model = new Model();
	View view = new View(eventQueue);
	Network network = new Network(eventQueue);
	Controller controller = new Controller(eventQueue, view, model, network);

	/* gui inits and controller starts rolling */
	try {
	    view.setArgs(args);
	    view.run();
	    controller.run();
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
    }
    /***************************************************************/
}