package pl.edu.pw.elka.frak1.proz.main;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import pl.edu.pw.elka.frak1.proz.controller.Controller;
import pl.edu.pw.elka.frak1.proz.controller.Event;
import pl.edu.pw.elka.frak1.proz.model.Model;
import pl.edu.pw.elka.frak1.proz.network.Network;
import pl.edu.pw.elka.frak1.proz.view.*;

/**
 * Main class to start whole program initializes all modules of MVC structure
 * and runs controller in self thread, starts also thread of view
 */
public class Main {
    /* main method of the program */
    public static void main(String[] args) {
	/** event queue of the program */
	BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<Event>();

	/* MVC structure initialize modules */
	Model model = new Model();
	View view = new View(eventQueue);
	Network network = new Network(eventQueue);
	Controller controller = new Controller(eventQueue, view, model, network);

	/* gui runs and controller starts rolling */
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