package nju.lemon.exceptions;

import sun.plugin2.message.Message;

public class MessageTooLongException extends RuntimeException {
    public MessageTooLongException() {
        super();
    }

    public MessageTooLongException(String message) {
        super(message);
    }
}
