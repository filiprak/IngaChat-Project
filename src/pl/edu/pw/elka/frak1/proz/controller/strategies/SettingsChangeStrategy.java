package pl.edu.pw.elka.frak1.proz.controller.strategies;

import pl.edu.pw.elka.frak1.proz.controller.Event;
import pl.edu.pw.elka.frak1.proz.controller.Strategy;
import pl.edu.pw.elka.frak1.proz.events.SettingsChangeEvent;
import pl.edu.pw.elka.frak1.proz.model.Model;
import pl.edu.pw.elka.frak1.proz.network.Network;
import pl.edu.pw.elka.frak1.proz.view.View;

public class SettingsChangeStrategy implements Strategy {

    @Override
    public void instructToDo(Event event, View view, Model model, Network network) {
	SettingsChangeEvent e = (SettingsChangeEvent) event;
	Network.setServerAddress(e.getNewIpAddress());
	Network.setServerPortNr(e.getNewPortNr());
	System.out.println("address changed: " + e.getNewIpAddress() + ":" + e.getNewPortNr());
    }

}
