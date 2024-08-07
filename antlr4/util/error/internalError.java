package util.error;

import util.position;

public class internalError extends error {
    public internalError(String msg, position pos) {
        super("Internal Error: " + msg, pos);
    }
}
