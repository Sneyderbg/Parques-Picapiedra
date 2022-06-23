package parchis;

public class Message {

    enum Type {
        SUCCESS,
        ERROR,
    }

    Type type;

    String info;

    public Message(Type type, String info) {
        this.type = type;
        this.info = info;
    }

    public String getMessage() {
        return String.format("%s: %s.", this.type, this.info);
    }
    
}
