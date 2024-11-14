package org.skvdb.exception;

public class KeyNotFoundException extends Exception {
    public KeyNotFoundException(String message) {
        super(message);
    }
    public KeyNotFoundException() {
    super();
  }
}
