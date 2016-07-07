package pl.edu.pw.elka.frak1.proz.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Model in MVC architecture contains userlist and message list
 */
public class Model {

    /** date format for messages */
    private DateFormat dateFormat;
    /** error messages displayed in error window */
    private ErrorMessage errorMessages;

    /** message list of all users */
    private ArrayList<Message> receivedMessagesList;
    /** user list, currently connected or offline */
    private ArrayList<UserHost> userList;

    public Model() {
	receivedMessagesList = new ArrayList<Message>();
	userList = new ArrayList<UserHost>();
	dateFormat = new SimpleDateFormat("MM-dd HH:mm");
	errorMessages = new ErrorMessage();
    }

    public ArrayList<UserHost> getUserList() {
	return userList;
    }

    public void addNewMessage(Message message) {
	receivedMessagesList.add(message);
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

    /** @return userhost reference of given nickname */
    public UserHost getUserHost(final String nickname) {
	for (UserHost host : userList)
	    if (host.getNickname().equals(nickname))
		return host;
	return null;
    }

    public void setUserHostStatus(final UserHost userhost, final boolean status) {
	userhost.setIsOnline(status);
    }

    public void setNewClientNickname(String oldNm, String newNm) {
	getUserHost(oldNm).setNickname(newNm);
    }

    public String getErrorMessage(final String errorType) {
	return errorMessages.getErrorMessage(errorType);
    }

    public boolean containsUser(String nickname) {
	if (getUserHost(nickname) == null)
	    return false;
	return userList.contains(getUserHost(nickname));
    }

    public String getCurrentDateTime() {
	return dateFormat.format(System.currentTimeMillis());
    }

    public void printModelState() {
	for (UserHost host : userList)
	    System.out.println(host.getNickname() + " : " + host.getIsOnline());
    }
}
