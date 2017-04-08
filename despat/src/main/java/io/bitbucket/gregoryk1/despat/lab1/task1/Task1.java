package io.bitbucket.gregoryk1.despat.lab1.task1;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.text.DateFormat;
import java.util.Date;

interface MessageDrawer {
    void drawMessage(ListCell<Message> cell, Message msg, boolean empty);
}

class Message {
    final String text;
    final Date sentAt;

    public Message(String text, Date sentAt) {
        this.text = text;
        this.sentAt = sentAt;
    }
}

class BasicMessageDrawer implements MessageDrawer {
    private final Font preferredFont = new Font("Times New Roman", 14);
    private final Color preferredColor = Color.BLACK;

    @Override
    public void drawMessage(ListCell<Message> cell, Message msg, boolean empty) {
        if (!empty) {
            cell.setFont(preferredFont);
            cell.setTextFill(preferredColor);
            cell.setText(msg.text);
        } else {
            cell.setText(null);
        }
    }
}

abstract class MessageDrawerDecorator implements MessageDrawer {

    private final MessageDrawer messageDrawer;

    public MessageDrawerDecorator(MessageDrawer messageDrawer) {
        this.messageDrawer = messageDrawer;
    }

    @Override
    public void drawMessage(ListCell<Message> cell, Message msg, boolean empty) {
        messageDrawer.drawMessage(cell, msg, empty);
    }
}

class ColorDecorator extends MessageDrawerDecorator {
    private final Color color;

    public ColorDecorator(MessageDrawer messageDrawer, Color color) {
        super(messageDrawer);
        this.color = color;
    }

    @Override
    public void drawMessage(ListCell<Message> cell, Message msg, boolean empty) {
        super.drawMessage(cell, msg, empty);
        cell.setTextFill(color);
    }
}

class FontSizeDecorator extends MessageDrawerDecorator {
    private final double size;

    public FontSizeDecorator(MessageDrawer messageDrawer, double size) {
        super(messageDrawer);
        this.size = size;
    }

    @Override
    public void drawMessage(ListCell<Message> cell, Message msg, boolean empty) {
        super.drawMessage(cell, msg, empty);
        cell.setFont(new Font(cell.getFont().getName(), size));
    }
}

class FontTypeDecorator extends MessageDrawerDecorator {
    private final String fontName;

    public FontTypeDecorator(MessageDrawer messageDrawer, String fontName) {
        super(messageDrawer);
        this.fontName = fontName;
    }

    @Override
    public void drawMessage(ListCell<Message> cell, Message msg, boolean empty) {
        super.drawMessage(cell, msg, empty);
        cell.setFont(new Font(fontName, cell.getFont().getSize()));
    }
}

class DateAdderDecorator extends MessageDrawerDecorator {
    public DateAdderDecorator(MessageDrawer messageDrawer) {
        super(messageDrawer);
    }

    @Override
    public void drawMessage(ListCell<Message> cell, Message msg, boolean empty) {
        super.drawMessage(cell, msg, empty);
        if (!empty) {
            String date = DateFormat.getDateInstance(DateFormat.SHORT).format(msg.sentAt);
            String time = DateFormat.getTimeInstance(DateFormat.MEDIUM).format(msg.sentAt);
            VBox dateTime = new VBox(new Text(time), new Text(date));
            dateTime.setAlignment(Pos.CENTER);
            cell.setGraphic(dateTime);
        } else {
            cell.setGraphic(null);
        }
    }
}


public class Task1 extends Application {
    private final VBox view = new VBox(5);
    private final HBox messageAdder = new HBox(5);
    private final TextField messageInput = new TextField("Enter your message");
    private final Button messageSendButton = new Button("Send!");
    private ObservableList<Message> messages = FXCollections.observableArrayList();
    private final ListView<Message> messagesView = new ListView<>(messages);

    @Override
    public void start(Stage primaryStage) {
        final MessageDrawer messageDrawer = getMessageDrawer();

        messageSendButton.setOnAction(ev -> {
            messages.add(new Message(messageInput.getText(), new Date()));
        });

        messagesView.setCellFactory(view -> {
            return new ListCell<Message>() {
                @Override
                protected void updateItem(Message msg, boolean empty) {
                    super.updateItem(msg, empty);
                    messageDrawer.drawMessage(this, msg, empty);
                }
            };
        });

        view.getChildren().add(messagesView);
        view.getChildren().add(messageAdder);
        messageAdder.getChildren().add(messageInput);
        messageAdder.getChildren().add(messageSendButton);

        messageAdder.setAlignment(Pos.CENTER);
        VBox.setVgrow(messagesView, Priority.ALWAYS);

        primaryStage.setScene(new Scene(view));
        primaryStage.show();
    }

    private MessageDrawer getMessageDrawer() {
        MessageDrawer messageDrawer = new BasicMessageDrawer();
        messageDrawer = new FontSizeDecorator(messageDrawer, 16);
        messageDrawer = new FontTypeDecorator(messageDrawer, "Verdana");
        messageDrawer = new ColorDecorator(messageDrawer, Color.web("#001b7b"));
        messageDrawer = new DateAdderDecorator(messageDrawer);
        return messageDrawer;
    }
}