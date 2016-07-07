package pl.edu.pw.elka.frak1.proz.controller;

import pl.edu.pw.elka.frak1.proz.model.Model;
import pl.edu.pw.elka.frak1.proz.network.Network;
import pl.edu.pw.elka.frak1.proz.view.View;

/**
 * base class of algorithm implemented in instructToDo() function is implemented
 * by particular strategies one of parameter is fired event which is passed in
 * event queue
 */
public interface Strategy {
    public void instructToDo(final Event event, final View view, final Model model, final Network network);
}
