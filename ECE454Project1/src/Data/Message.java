package Data;

import java.io.Serializable;

public class Message implements Serializable {
    private int    senderID;
    private String messageText;

    public Message(int id, String text) {
        senderID    = id;
        messageText = text;
    }
    public String getIntention() {
        return messageText;
    }
}
