package pl.edu.pw.elka.frak1.proz.view;

import java.util.concurrent.BlockingQueue;

import pl.edu.pw.elka.frak1.proz.controller.Event;
import pl.edu.pw.elka.frak1.proz.model.Message;

/**
 * GUI interface of the program: View in MVC architecture, starts new thread of
 * ChatGui runnable object which is java fx application
 */

public class View {
    private Thread viewThread;
    private ChatGui gui;

    public View(final BlockingQueue<Event> eventQueue) {
	gui = new ChatGui();
	gui.setEventQueue(eventQueue);

	viewThread = new Thread(gui);
	viewThread.setName("GUI-Thread");
    }

    public void setArgs(String[] args) {
	gui.setArgs(args);
    }

    public void run() {
	viewThread.start(); /* GUI init */
    }

    /*
     * interface for controller methods called from outside
     */
    public void addNewMessage(final Message message) {
	gui.receiveMessage(message);
    }

    public void addNewUser(final String userNick, boolean status) {
	gui.addNewUser(userNick, status);
    }

    public void setUserStatus(final String userNick, boolean status) {
	gui.setUserStatus(userNick, status);
    }

    public void renameUser(final String userNick, final String nickname) {
	gui.renameUser(userNick, nickname);
    }

    public void showErrorWindow(final String errorMessage) {
	gui.showErrorWindow(errorMessage);
    }

    public void clearAll() {
	gui.clearAll();
    }
}