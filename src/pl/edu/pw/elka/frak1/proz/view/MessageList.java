package pl.edu.pw.elka.frak1.proz.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import pl.edu.pw.elka.frak1.proz.model.Message;

/** area with messages on window */
public class MessageList {
    private ListView<VBox> textChatBox;
    private ObservableList<VBox> messageItemList;

    /* constructor */
    public MessageList() {
	textChatBox = new ListView<VBox>();
	messageItemList = FXCollections.observableArrayList();
	    
	textChatBox.setEditable(true);
	textChatBox.setItems(messageItemList);

	String css = View.class.getResource("style.css").toExternalForm();

	textChatBox.getStylesheets().clear();
	textChatBox.getStylesheets().add(css);
    }

    public ListView<VBox> getTextChatBox() {
	return textChatBox;
    }

    public void addMessage(Message message) {
	String cssMessageStyle;
	int msgPadding = 65;
	
	if(message.isOwn() == true) {
	    cssMessageStyle = "-fx-background-color: #333333; " //message bckg color
		    + "-fx-border-radius: 15; "
		    + "-fx-background-radius: 15; "
		    + "-fx-padding: 10px 10px 10px 40px;"
		    + "-fx-background-insets: 1px 10px 1px 30px, 1px ;";
	    msgPadding = 90;
	}
	else cssMessageStyle = "-fx-background-color: #69532E; "
		+ "-fx-border-radius: 15; "
		+ "-fx-background-radius: 15; "
		+ "-fx-padding: 10px 10px 10px 15px;"
		+ "-fx-background-insets: 1px 10px 1px 5px, 1px ;";
	    
	if(message.isWithHeader() == false) {
	    VBox mess = new VBox();
	    
	    Text contents = new Text(message.getContents());
	    contents.setStyle("-fx-fill: #dddddd; -fx-font-size: 13px;");
	    contents.setWrappingWidth(textChatBox.getPrefWidth() - msgPadding-20);
	    contents.setTextAlignment(TextAlignment.LEFT);
	    
	    mess.getChildren().add(contents);
	    mess.setPadding(new Insets(0,0,0,0));
	    mess.fillWidthProperty();
	    mess.setStyle(cssMessageStyle);
	    messageItemList.add(mess);
	    textChatBox.scrollTo(mess);
	    return;
	}
	
	Label name = new Label(message.getAuthor());
	name.setStyle("-fx-font-weight: bold; -fx-font-size: 10px; -fx-text-fill: #aaaaaa;");
	name.setAlignment(Pos.BASELINE_LEFT);
	name.setPrefWidth((textChatBox.getPrefWidth()-msgPadding)/2);
	
	Label time = new Label(message.getTime());
	time.setStyle("-fx-font-weight: bold; -fx-font-size: 10px; -fx-text-fill: #aaaaaa;");
	time.setPrefWidth((textChatBox.getPrefWidth()-msgPadding)/2);
	time.setAlignment(Pos.BASELINE_RIGHT);

	Text contents = new Text(message.getContents());
	contents.setStyle("-fx-fill: #dddddd; -fx-font-size: 13px;");
	contents.setWrappingWidth(textChatBox.getPrefWidth() - msgPadding);
	contents.setTextAlignment(TextAlignment.LEFT);	

	VBox mess = new VBox();
	HBox head = new HBox();
	
	head.setAlignment(Pos.BASELINE_LEFT);
	mess.getChildren().add(head);
	mess.getChildren().add(contents);
	head.getChildren().add(name);
	head.getChildren().add(time);
	
	mess.fillWidthProperty();
	head.fillHeightProperty();

	head.setAlignment(Pos.BASELINE_LEFT);
	mess.setPadding(new Insets(0,0,0,10));
	
	mess.setStyle(cssMessageStyle);
	messageItemList.add(mess);
	textChatBox.scrollTo(mess);
    }
}
