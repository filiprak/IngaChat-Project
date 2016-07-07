package pl.edu.pw.elka.frak1.proz.controller.strategies;

import java.io.IOException;

import pl.edu.pw.elka.frak1.proz.controller.Event;
import pl.edu.pw.elka.frak1.proz.controller.Strategy;
import pl.edu.pw.elka.frak1.proz.model.Message;
import pl.edu.pw.elka.frak1.proz.model.Model;
import pl.edu.pw.elka.frak1.proz.network.Network;
import pl.edu.pw.elka.frak1.proz.view.View;

public class DisconnectStrategy implements Strategy {

    @Override
    public void instructToDo(Event event, View view, Model model, Network network) {
	if (!network.isConnected()) {
	    view.enableConnectButton(false, true);
	    return;
	}

	Message sysMessage = new Message(model.getClientNickname(), "", "off", false, false);
	sysMessage.setSystemMessage(true);

	network.sendMessage(sysMessage);

	try {

	    Thread.sleep(500); // close message broadcast timeout
	    network.closeConnection();
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
	model.setUserHostStatus(model.getUserHostClient(), false);
	view.setUserStatus(model.getClientNickname(), false);
	view.enableConnectButton(false, true);

	model.clearUserList();
	view.clearUserList();
	view.setNickFieldDisabled(true);
    }

}
