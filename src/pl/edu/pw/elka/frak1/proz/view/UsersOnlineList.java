package pl.edu.pw.elka.frak1.proz.view;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/** list of users on window */
public class UsersOnlineList {
    private ListView<GridPane> contactBox;
    private ObservableList<GridPane> contactItemList;
    private ArrayList<String> nickNamesList;

    /* constructor */
    public UsersOnlineList() {
	contactBox = new ListView<GridPane>();
	contactItemList = FXCollections.observableArrayList();
	nickNamesList = new ArrayList<String>();

	contactBox.setItems(contactItemList);

	String css = View.class.getResource("userlist.css").toExternalForm();
	contactBox.setPadding(new Insets(5));

	contactBox.getStylesheets().add(css);
	
    }

    public ListView<GridPane> getContactBox() {
	return contactBox;
    }

    public void addUser(final String userNick, boolean status) {
	if (nickNamesList.contains(userNick))
	    return;

	Circle statusCircle = new Circle(4, Color.GREEN);
	GridPane userCell = new GridPane();
	Label userNickname = new Label(userNick);
	userNickname.setPrefWidth(150);

	userCell.add(userNickname, 0, 0);
	if (status)
	    userCell.add(statusCircle, 1, 0);

	contactItemList.add(userCell);
	nickNamesList.add(userNick);
    }

    public void removeUser(final String userNick) {
	if (nickNamesList.contains(userNick) == false)
	    return;
	contactItemList.remove(nickNamesList.indexOf(userNick));
	nickNamesList.remove(userNick);
    }

    public void setUserStatus(final String userNick, final boolean status) {
	if (nickNamesList.contains(userNick) == false)
	    return;

	removeUser(userNick);
	addUser(userNick, status);

	contactBox.refresh();
    }

    public void renameUser(final String userNick, final String nickname) {
	if (nickNamesList.contains(userNick) == false)
	    return;
	
	removeUser(userNick);
	addUser(nickname, true);
    }

    public void clear() {
	contactItemList.clear();
	nickNamesList.clear();
	contactBox.refresh();
    }
}
