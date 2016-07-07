package pl.edu.pw.elka.frak1.proz.controller.strategies;

import pl.edu.pw.elka.frak1.proz.controller.Event;
import pl.edu.pw.elka.frak1.proz.controller.Strategy;
import pl.edu.pw.elka.frak1.proz.model.Message;
import pl.edu.pw.elka.frak1.proz.model.Model;
import pl.edu.pw.elka.frak1.proz.network.Network;
import pl.edu.pw.elka.frak1.proz.view.View;

public class ConnectStrategy implements Strategy {

    @Override
    public void instructToDo(Event event, View view, Model model, Network network) {
	if (network.isConnected()) {
	    view.enableConnectButton(false, false);
	    return;
	}

	view.showErrorWindow(model.getErrorMessage("ConnectingProcess"));
	boolean isConnected = network.tryToConnect();
	if (isConnected) {
	    if (model.getClientNickname().equals("")) {
		Message initMessage = new Message(model.getClientNickname(), "", model.getClientNickname(), true, true);
		initMessage.setSystemMessage(true);
		network.sendMessage(initMessage);
	    } else {
		Message reconnectMessage = new Message(model.getClientNickname(), "", model.getClientNickname(), false,
			true);
		reconnectMessage.setSystemMessage(true);
		network.sendMessage(reconnectMessage);
		model.setUserHostStatus(model.getUserHostClient(), true);
		view.setUserStatus(model.getClientNickname(), true);
	    }
	    view.setNickFieldDisabled(false);
	}

	view.closeErrorWindow();
	view.enableConnectButton(false, !isConnected);
    }
}
