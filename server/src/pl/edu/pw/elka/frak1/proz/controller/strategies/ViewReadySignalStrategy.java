package pl.edu.pw.elka.frak1.proz.controller.strategies;

import pl.edu.pw.elka.frak1.proz.controller.Event;
import pl.edu.pw.elka.frak1.proz.controller.Strategy;
import pl.edu.pw.elka.frak1.proz.model.Model;
import pl.edu.pw.elka.frak1.proz.network.Network;
import pl.edu.pw.elka.frak1.proz.view.View;

public class ViewReadySignalStrategy implements Strategy {

    @Override
    public void instructToDo(Event event, View view, Model model, Network network) {
	// TODO Auto-generated method stub
	System.out.println("View is ready");
	network.init();
    }

}
