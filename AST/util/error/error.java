package util.error;

import util.position;

public class error extends RuntimeException {
    private position pos;
    private String message;
    private String type;

    public error(String tp, String msg, position pos) {
        this.type = tp;
        this.pos = pos;
        this.message = msg;
    }

    public String toString() {
        return type + message + ": " + pos.toString();
    }

    public String getMessage() {
        return message;
    }
}
