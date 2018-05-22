package nju.lemon.exceptions;

public class UnexpectedPacketFormatException extends Exception {
    public UnexpectedPacketFormatException() {
        super();
    }

    public UnexpectedPacketFormatException(String msg) {
        super(msg);
    }
}
