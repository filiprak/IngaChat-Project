package pl.edu.pw.elka.frak1.proz.view;

import java.util.concurrent.BlockingQueue;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
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

    /* settings window */
    private Stage settingsWindow;
    private Button confirmButtonSW, cancelButtonSW;
    private static TextField ipAdressTextAreaSW, nickTextAreaSW, portNrTextAreaSW;
    private Scene sceneSW;
    private GridPane gridPaneSW, controlButtonsSW, layoutSW;
    private Label ipLabel, nickLabel, portLabel;
    private CheckBox checkBox;

    /* primary window */
    private static Stage mainStage;
    private static Scene mainScene;
    private Button sendButton, optionButton;
    private static ToggleButton connectTButton;

    private TextArea textInput;
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

	primaryStage.setTitle("IngaChat");
	primaryStage.setMaxWidth(650);
	primaryStage.setMinHeight(450);
	mainStage = primaryStage;

	/* create objects */
	sendButton = new Button("Send");
	connectTButton = new ToggleButton();
	optionButton = new Button("\u26ed");

	textInput = new TextArea();

	header = new AnchorPane();
	inputField = new GridPane();
	hostList = new AnchorPane();

	layout = new VBox();
	divList = new HBox();
	divInput = new VBox();
	smallButtons = new GridPane();

	messageBox = new MessageList();
	usersBox = new UsersOnlineList();

	mainScene = new Scene(layout, 650, 450);
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

	inputField.add(textInput, 0, 0);
	inputField.add(sendButton, 1, 0);
	inputField.add(smallButtons, 2, 0);
	smallButtons.add(connectTButton, 0, 0);
	smallButtons.add(optionButton, 0, 1);

	/* set objects style */
	layout.fillWidthProperty();
	divList.fillHeightProperty();
	divInput.fillWidthProperty();

	header.setPrefSize(650, 66);
	inputField.setPrefSize(441, 71);

	inputField.setPadding(new Insets(5));
	inputField.setHgap(5);
	smallButtons.setVgap(5);

	textInput.setPrefSize(336, 111);
	textInput.setWrapText(true);
	textInput.setPromptText("write something...");
	sendButton.setPrefSize(50, 71);
	connectTButton.setPrefSize(50, 30);
	connectTButton.setText("on");
	connectTButton.setTooltip(new Tooltip("Click to change your status"));
	optionButton.setPrefSize(50, 30);
	optionButton.setTooltip(new Tooltip("Click to go to settings"));
	inputField.setAlignment(Pos.BOTTOM_RIGHT);
	messageBox.getTextChatBox().setPrefSize(441, 300);
	usersBox.getContactBox().setPrefSize(200, 373);

	/* css style add */
	String css = View.class.getResource("style.css").toExternalForm();

	textInput.getStylesheets().clear();
	textInput.getStylesheets().add(css);
	sendButton.getStylesheets().add(css);
	optionButton.getStylesheets().add(css);
	connectTButton.getStylesheets().add(css);

	layout.setStyle("-fx-background-color: #3E3E45;");
	header.setStyle("-fx-background-color: #3E3E45;");
	inputField.setStyle("-fx-background-color: #131316;");
	hostList.setStyle("-fx-background-color: #131316;");
	textInput.setStyle("text-area-background: #131316; -fx-text-fill: #bbbbbb;");
	optionButton.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	connectTButton.setStyle("-fx-text-fill: #000000; -fx-font-weight: bold;");

	drawHeader();
	initSettingsWindow(css);
	initErrorWindow(css);

	/* set objects events */
	setEventHandlers();

	mainScene.heightProperty().addListener((ChangeListener<? super Number>) (observable, oldValue, newValue) -> {
	    Double height = (Double) newValue;
	    Double oldheight = (Double) oldValue;
	    messageBox.getTextChatBox().setPrefHeight(messageBox.getTextChatBox().getHeight() + height - oldheight);
	    usersBox.getContactBox().setPrefHeight(usersBox.getContactBox().getHeight() + height - oldheight);
	});

	primaryStage.setScene(mainScene);
	primaryStage.show();

	/* signal to start controller */
	ViewReadySignalEvent e = new ViewReadySignalEvent();
	ChatGui.eventQueue.offer((Event) e);
    }

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
	okButtonEW.setDefaultButton(true);

	vboxEW.setStyle("-fx-background-color: #131316;");

	sceneEW = new Scene(vboxEW, 250, 100);
	errorWindow = new Stage();
	errorWindow.setScene(sceneEW);
	errorWindow.initModality(Modality.APPLICATION_MODAL);
	errorWindow.setTitle("Error");

	sceneEW.getStylesheets().add(cssStyle);
	errorWindow.setResizable(false);
    }

    private void initSettingsWindow(String cssStyle) {
	ipAdressTextAreaSW = new TextField();
	nickTextAreaSW = new TextField();
	portNrTextAreaSW = new TextField();
	confirmButtonSW = new Button("Apply");
	cancelButtonSW = new Button("Cancel");
	gridPaneSW = new GridPane();
	layoutSW = new GridPane();
	ipLabel = new Label("Server IP");
	nickLabel = new Label("Nick");
	portLabel = new Label("Port nr");
	controlButtonsSW = new GridPane();
	checkBox = new CheckBox("Set server address");

	cancelButtonSW.setPrefWidth(70);
	confirmButtonSW.setPrefWidth(70);
	ipAdressTextAreaSW.setPrefSize(150, 10);
	nickTextAreaSW.setPrefSize(150, 10);
	portNrTextAreaSW.setPrefSize(150, 10);

	confirmButtonSW.setDefaultButton(true);
	cancelButtonSW.setCancelButton(true);

	gridPaneSW.setAlignment(Pos.BASELINE_CENTER);
	layoutSW.setPadding(new Insets(20));
	gridPaneSW.setVgap(5);
	gridPaneSW.setHgap(15);
	layoutSW.setVgap(10);

	controlButtonsSW.add(cancelButtonSW, 0, 0);
	controlButtonsSW.add(confirmButtonSW, 1, 0);
	controlButtonsSW.setHgap(10);

	layoutSW.add(checkBox, 0, 0);
	layoutSW.add(gridPaneSW, 0, 1);
	layoutSW.add(controlButtonsSW, 0, 2);
	controlButtonsSW.setAlignment(Pos.BASELINE_RIGHT);

	gridPaneSW.add(ipLabel, 0, 1);
	gridPaneSW.add(nickLabel, 0, 3);
	gridPaneSW.add(portLabel, 0, 2);

	gridPaneSW.add(ipAdressTextAreaSW, 1, 1);
	gridPaneSW.add(portNrTextAreaSW, 1, 2);
	gridPaneSW.add(nickTextAreaSW, 1, 3);

	layoutSW.setStyle("-fx-background-color: #131316;");

	sceneSW = new Scene(layoutSW, 250, 180);
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
	sendButton.setOnMouseClicked(clickEvent -> {
	    sendMessageFromTextInput();
	    textInput.clear();
	});
	okButtonEW.setOnMouseClicked(clickEvent -> {
	    errorWindow.close();
	});
	connectTButton.setOnMouseClicked(clickEvent -> {
	    if (connectTButton.isSelected() == false) {
		ChatGui.eventQueue.offer(new ConnectEvent());
		connectTButton.setDisable(true);
		connectTButton.setText("on");
	    } else {
		ChatGui.eventQueue.offer(new DisconnectEvent());
		connectTButton.setDisable(true);
		connectTButton.setText("off");
	    }
	});
	optionButton.setOnMouseClicked(clickEvent -> {
	    settingsWindow.showAndWait();
	    clickEvent.consume();
	});
	checkBox.setOnMouseClicked(clickEvent -> {
	    boolean isSelected = checkBox.isSelected();
	    ipAdressTextAreaSW.setDisable(!isSelected);
	    portNrTextAreaSW.setDisable(!isSelected);
	});
	confirmButtonSW.setOnMouseClicked(clickEvent -> {
	    String newNick = nickTextAreaSW.getText();
	    if (!newNick.equals("")) {
		if (newNick.length() > 24)
		    newNick = newNick.substring(0, 23);
		NickChangeRequestEvent e = new NickChangeRequestEvent(newNick);
		ChatGui.eventQueue.offer((Event) e);
		nickTextAreaSW.setText(newNick);
	    }

	    Integer portNr;
	    try {
		portNr = new Integer(portNrTextAreaSW.getText());
		if (portNr < 0 || portNr > 65535)
		    throw new NumberFormatException();
	    } catch (NumberFormatException e1) {
		errorTextLabel.setText("Incorrect port number !");
		errorWindow.showAndWait();
		return;
	    }

	    String ipAddress = ipAdressTextAreaSW.getText();
	    SettingsChangeEvent e = new SettingsChangeEvent(ipAddress, portNr);
	    ChatGui.eventQueue.offer((Event) e);

	    if (checkBox.isSelected()) {
		checkBox.setSelected(false);
		ipAdressTextAreaSW.setDisable(true);
		portNrTextAreaSW.setDisable(true);
	    }
	    settingsWindow.close();
	});
	cancelButtonSW.setOnMouseClicked(clickEvent -> {
	    settingsWindow.close();
	});
	textInput.setOnKeyPressed(keyEvent -> {
	    if (keyEvent.getCode() != KeyCode.ENTER)
		return;
	    sendMessageFromTextInput();
	    keyEvent.consume();
	});
	mainStage.setOnCloseRequest(closeEvent -> {
	    ViewClosedEvent e = new ViewClosedEvent();
	    ChatGui.eventQueue.offer(e);
	});
    }

    private void sendMessageFromTextInput() {
	if (textInput.getText().trim().length() == 0)
	    return;
	MessageSendEvent e = new MessageSendEvent(new Message("", "", textInput.getText(), true, true));
	ChatGui.eventQueue.offer((Event) e);
	textInput.clear();
    }

    private void drawHeader() {
	Canvas canvas = new Canvas(650, 66);
	GraphicsContext gc = canvas.getGraphicsContext2D();
	gc.setFill(Color.ORANGE);
	gc.fillText("PROZ project - conference network chat in Java by Filip Rak", 310, 58);
	header.getChildren().add(canvas);
    }

    /* interface for view */
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
		if (errorWindow.isShowing()) {
		    try {
			Thread.sleep(1000);
			errorWindow.close();
		    } catch (InterruptedException e) {
			e.printStackTrace();
		    }
		}
		errorTextLabel.setText(errorMessage);
		errorWindow.showAndWait();
	    }
	});
    }

    public void closeErrorWindow() {
	Platform.runLater(new Runnable() {

	    @Override
	    public void run() {
		if (errorWindow.isShowing())
		    errorWindow.close();
	    }
	});
    }

    public void refreshErrorWindow(final String newErrorMessage) {
	Platform.runLater(new Runnable() {

	    @Override
	    public void run() {
		if (errorWindow.isShowing()) {
		    errorTextLabel.setText(newErrorMessage);
		}

	    }
	});
    }

    public void enableConnectButton(boolean disabled, boolean selected) {
	Platform.runLater(new Runnable() {

	    @Override
	    public void run() {
		connectTButton.setSelected(selected);
		connectTButton.setDisable(disabled);
		if (selected)
		    connectTButton.setText("off");
		else
		    connectTButton.setText("on");
	    }
	});
    }

    public void setConnectButtonSelected(boolean selected) {
	Platform.runLater(new Runnable() {

	    @Override
	    public void run() {
		connectTButton.setSelected(selected);
	    }
	});
    }

    public void clearUserList() {
	Platform.runLater(new Runnable() {

	    @Override
	    public void run() {
		usersBox.clear();
	    }
	});
    }

    public void setAddressDefault(String ipAddr, int portNr) {
	Platform.runLater(new Runnable() {

	    @Override
	    public void run() {
		ipAdressTextAreaSW.setText(ipAddr);
		portNrTextAreaSW.setText(portNr + "");
		ipAdressTextAreaSW.setDisable(true);
		portNrTextAreaSW.setDisable(true);
	    }
	});
    }

    public void setNickFieldDisabled(boolean disabled) {
	Platform.runLater(new Runnable() {

	    @Override
	    public void run() {
		nickTextAreaSW.setDisable(disabled);
	    }
	});
    }

    public void grabUserAttention() {
	Platform.runLater(new Runnable() {

	    @Override
	    public void run() {
		mainStage.toFront();
	    }
	});
    }
}
