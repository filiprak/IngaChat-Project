package pl.edu.pw.elka.frak1.proz.model;

import java.util.ArrayList;

/**
 * Model in MVC architecture contains userlist and message list
 */
public class Model {

    /*
     * nickname of this client and lastMsgAuthor nick used to determine message
     * format (with header or not)
     */
    private String clientNickname, lastMsgAuthor;

    /** this client reference on userHost object */
    private UserHost client;

    /* lists of data */
    private ArrayList<Message> receivedMessagesList;
    private ArrayList<UserHost> userList;

    private ErrorMessage errorMessages;

    public Model() {
	receivedMessagesList = new ArrayList<Message>();
	userList = new ArrayList<UserHost>();
	clientNickname = ""; // initial name of client if never connected
	errorMessages = new ErrorMessage();
	lastMsgAuthor = "";
    }

    public boolean isLastMessageSameAuthor(final String author) {
	boolean value = false;
	if (receivedMessagesList.isEmpty())
	    return value;
	else if (lastMsgAuthor.equals(author))
	    return true;
	else
	    return false;
    }

    public UserHost getUserHostClient() {
	return client;
    }

    public void setUserHostClient(UserHost client) {
	this.client = client;
    }

    public void addNewMessage(Message message) {
	receivedMessagesList.add(message);
	lastMsgAuthor = message.getAuthor();
    }

    public void removeMessage(Message message) {
	if (receivedMessagesList.contains(message) == false)
	    return;
	receivedMessagesList.remove(message);
    }

    public void addNewUser(final UserHost userhost) {
	if (userList.contains(userhost))
	    return;
	userList.add(userhost);
    }

    public void removeUser(final UserHost userhost) {
	if (userList.contains(userhost))
	    return;
	userList.remove(userhost);
    }

    public void clearMessageList() {
	receivedMessagesList.clear();
    }

    public void clearUserList() {
	userList.clear();
    }

    public String getClientNickname() {
	return clientNickname;
    }

    public boolean containsUser(String nickname) {
	return userList.contains(getUserHost(nickname));
    }

    public UserHost getUserHost(final String nickname) {
	for (UserHost host : userList)
	    if (host.getNickname().equals(nickname))
		return host;
	return null;
    }

    public void setUserHostStatus(final UserHost userhost, final boolean status) {
	userhost.setIsOnline(status);
    }

    public void setClientNickname(String clientNickname) {
	this.clientNickname = clientNickname;
    }

    public String getErrorMessage(final String errorType) {
	return errorMessages.getErrorMessage(errorType);
    }

    public void printModelState() {
	for (UserHost host : userList)
	    System.out.println(host.getNickname() + " : " + host.getIsOnline());
    }
}
