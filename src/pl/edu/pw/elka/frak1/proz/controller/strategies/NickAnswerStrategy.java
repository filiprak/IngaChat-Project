package pl.edu.pw.elka.frak1.proz.controller.strategies;

import pl.edu.pw.elka.frak1.proz.controller.Event;
import pl.edu.pw.elka.frak1.proz.controller.Strategy;
import pl.edu.pw.elka.frak1.proz.events.NickAnswerEvent;
import pl.edu.pw.elka.frak1.proz.model.Model;
import pl.edu.pw.elka.frak1.proz.network.Network;
import pl.edu.pw.elka.frak1.proz.view.View;

public class NickAnswerStrategy implements Strategy {

    @Override
    public void instructToDo(Event event, View view, Model model, Network network) {
	NickAnswerEvent e = (NickAnswerEvent) event;

	if (e.isAccepted()) {
	    if (e.getOldNickname().equals(model.getClientNickname())) {
		model.setClientNickname(e.getNewNickname());
		view.renameUser(e.getOldNickname(), e.getNewNickname());
	    } else {
		if (model.containsUser(e.getOldNickname())) {
		    model.getUserHost(e.getOldNickname()).setNickname(e.getNewNickname());
		    view.renameUser(e.getOldNickname(), e.getNewNickname());
		}
	    }
	} else
	    view.showErrorWindow(model.getErrorMessage("WrongNicknameError"));
    }

}
