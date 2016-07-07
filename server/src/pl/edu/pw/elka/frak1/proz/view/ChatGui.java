package pl.edu.pw.elka.frak1.proz.view;

import java.util.concurrent.BlockingQueue;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pl.edu.pw.elka.frak1.proz.controller.Event;
import pl.edu.pw.elka.frak1.proz.events.*;
import pl.edu.pw.elka.frak1.proz.model.Message;

/** class of java fx application - gui interface printed on screen */
public class ChatGui extends Application implements Runnable {
    private String[] args;

    /* error window */
    private static Stage errorWindow;
    private Button okButtonEW;
    private Scene sceneEW;
    private VBox vboxEW;
    private static Text errorTextLabel;

    /* pop-up window */
    private Stage settingsWindow;
    private Button confirmButtonSW, cancelButtonSW;
    private TextField portTextAreaSW;
    private Scene sceneSW;
    private GridPane gridPaneSW, controlButtonsSW;
    private Label portLabel;

    /* primary window */
    private Stage mainStage;
    private Button optionButton;

    private static MessageList messageBox;
    private static UsersOnlineList usersBox;

    private VBox layout;
    private HBox divList;
    private VBox divInput;
    private AnchorPane hostList, header;
    private GridPane inputField, smallButtons;

    private static BlockingQueue<Event> eventQueue;

    public void setEventQueue(final BlockingQueue<Event> eventQueue) {
	ChatGui.eventQueue = eventQueue;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

	// TODO Auto-generated method stub
	primaryStage.setTitle("IngaChat");
	primaryStage.setMaxWidth(650);
	primaryStage.setMaxHeight(450);
	mainStage = primaryStage;

	/* create objects */
	optionButton = new Button("Settings");

	header = new AnchorPane();
	inputField = new GridPane();
	hostList = new AnchorPane();

	layout = new VBox();
	divList = new HBox();
	divInput = new VBox();
	smallButtons = new GridPane();

	messageBox = new MessageList();
	usersBox = new UsersOnlineList();

	/* configure objects */
	layout.setSpacing(3);
	divInput.setSpacing(3);
	divList.setSpacing(3);
	divList.setPadding(new Insets(0, 3, 3, 3));

	/* make objects tree */
	layout.getChildren().add(header);
	layout.getChildren().add(divList);
	divList.getChildren().add(divInput);
	divList.getChildren().add(hostList);
	hostList.getChildren().add(usersBox.getContactBox());
	divInput.getChildren().add(messageBox.getTextChatBox());
	divInput.getChildren().add(inputField);

	inputField.add(smallButtons, 0, 0);
	smallButtons.add(optionButton, 0, 1);

	/* set objects style */
	layout.fillWidthProperty();
	divList.fillHeightProperty();
	divInput.fillWidthProperty();

	header.setPrefSize(450, 100);
	inputField.setPrefSize(441, 30);
	inputField.setPadding(new Insets(5));
	inputField.setHgap(5);

	optionButton.setPrefSize(70, 30);
	optionButton.setTooltip(new Tooltip("Click to go to settings"));
	inputField.setAlignment(Pos.CENTER);
	smallButtons.setAlignment(Pos.CENTER);
	messageBox.getTextChatBox().setPrefSize(441, 320);
	usersBox.getContactBox().setPrefSize(200, 350);

	/* css style add */
	String css = View.class.getResource("style.css").toExternalForm();

	optionButton.getStylesheets().add(css);

	layout.setStyle("-fx-background-color: #ff9700;");
	header.setStyle("-fx-background-color: #ff9700;");
	inputField.setStyle("-fx-background-color: #131316;");
	hostList.setStyle("-fx-background-color: #131316;");

	initSettingsWindow(css);
	initErrorWindow(css);

	/* set objects events */
	setEventHandlers();

	Scene scene = new Scene(layout, 650, 450);
	primaryStage.setScene(scene);
	primaryStage.show();

	/* signal to start controller */
	ViewReadySignalEvent e = new ViewReadySignalEvent();
	ChatGui.eventQueue.offer((Event) e);
	settingsWindow.showAndWait();
    }

    /** initializes error window components */
    private void initErrorWindow(String cssStyle) {
	errorWindow = new Stage();
	okButtonEW = new Button("OK");
	vboxEW = new VBox(30);
	errorTextLabel = new Text("");

	errorTextLabel.setTextAlignment(TextAlignment.CENTER);
	errorTextLabel.setFill(Color.LIGHTGRAY);
	errorTextLabel.setFontSmoothingType(FontSmoothingType.LCD);

	vboxEW.getChildren().add(errorTextLabel);
	vboxEW.getChildren().add(okButtonEW);
	vboxEW.setAlignment(Pos.CENTER);
	vboxEW.fillWidthProperty();
	vboxEW.setPadding(new Insets(10));
	okButtonEW.setPrefWidth(70);

	okButtonEW.setAlignment(Pos.BOTTOM_CENTER);

	vboxEW.setStyle("-fx-background-color: #131316;");

	sceneEW = new Scene(vboxEW, 250, 100);
	errorWindow = new Stage();
	errorWindow.setScene(sceneEW);
	errorWindow.initModality(Modality.APPLICATION_MODAL);
	errorWindow.setTitle("Error");

	sceneEW.getStylesheets().add(cssStyle);
	errorWindow.setResizable(false);
    }

    /** initializes settings window components */
    private void initSettingsWindow(String cssStyle) {
	portTextAreaSW = new TextField();
	confirmButtonSW = new Button("Apply");
	cancelButtonSW = new Button("Cancel");
	gridPaneSW = new GridPane();
	portLabel = new Label("Port nr:");
	controlButtonsSW = new GridPane();

	cancelButtonSW.setPrefWidth(70);
	confirmButtonSW.setPrefWidth(70);
	portTextAreaSW.setPrefSize(150, 10);
	portTextAreaSW.setText("44444");

	gridPaneSW.setAlignment(Pos.BASELINE_CENTER);
	gridPaneSW.setPadding(new Insets(20));
	gridPaneSW.setVgap(10);
	gridPaneSW.setHgap(10);

	controlButtonsSW.add(cancelButtonSW, 0, 0);
	controlButtonsSW.add(confirmButtonSW, 1, 0);
	controlButtonsSW.setHgap(10);

	gridPaneSW.add(portLabel, 0, 0);
	gridPaneSW.add(portTextAreaSW, 1, 0);
	gridPaneSW.add(controlButtonsSW, 1, 1);

	gridPaneSW.setStyle("-fx-background-color: #131316;");

	sceneSW = new Scene(gridPaneSW, 250, 90);
	settingsWindow = new Stage();
	settingsWindow.setScene(sceneSW);
	settingsWindow.initModality(Modality.APPLICATION_MODAL);
	settingsWindow.setTitle("Settings");
	sceneSW.getStylesheets().add(cssStyle);
	settingsWindow.setResizable(false);
    }

    public void setArgs(String[] args) {
	this.args = args;
    }

    @Override
    public void run() {
	launch(args);
    }

    private void setEventHandlers() {
	okButtonEW.setOnMouseClicked(clickEvent -> {
	    errorWindow.close();
	});
	optionButton.setOnMouseClicked(clickEvent -> {
	    settingsWindow.showAndWait();
	    clickEvent.consume();
	});
	confirmButtonSW.setOnMouseClicked(clickEvent -> {
	    Integer portNr;
	    try {
		portNr = new Integer(portTextAreaSW.getText());
		if (portNr < 0 || portNr > 65535)
		    throw new NumberFormatException();
	    } catch (NumberFormatException e1) {
		errorTextLabel.setText("Incorrect port number !");
		errorWindow.showAndWait();
		return;
	    }
	    PortSetEvent ev = new PortSetEvent(portNr);
	    eventQueue.offer((Event) ev);
	    settingsWindow.close();
	});
	cancelButtonSW.setOnMouseClicked(clickEvent -> {
	    settingsWindow.close();
	});
	mainStage.setOnCloseRequest(closeEvent -> {
	    ViewClosedEvent e = new ViewClosedEvent();
	    ChatGui.eventQueue.offer(e);
	});
    }

    /*
     * interface for view, this methods are designed to be called from outside
     */
    public void receiveMessage(final Message message) {
	Platform.runLater(new Runnable() {

	    @Override
	    public void run() {
		messageBox.addMessage(message);
	    }
	});
    }

    public void addNewUser(final String userNick, boolean status) {
	Platform.runLater(new Runnable() {

	    @Override
	    public void run() {
		usersBox.addUser(userNick, status);
	    }
	});
    }

    public void renameUser(final String userNick, final String nickname) {
	Platform.runLater(new Runnable() {

	    @Override
	    public void run() {
		usersBox.renameUser(userNick, nickname);
	    }
	});
    }

    public void setUserStatus(final String userNick, final boolean status) {
	Platform.runLater(new Runnable() {

	    @Override
	    public void run() {
		usersBox.setUserStatus(userNick, status);
	    }
	});
    }

    public void showErrorWindow(final String errorMessage) {
	Platform.runLater(new Runnable() {

	    @Override
	    public void run() {
		errorTextLabel.setText(errorMessage);
		errorWindow.showAndWait();
	    }
	});
    }

    public void clearAll() {
	Platform.runLater(new Runnable() {

	    @Override
	    public void run() {
		usersBox.clear();
		messageBox.clear();
	    }
	});
    }
}
