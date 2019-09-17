package ru.art.generator.exception;

public class NotFoundPrefixException extends Exception {

  public NotFoundPrefixException() {
  }

  public NotFoundPrefixException(String message) {
    super(message);
  }

  public NotFoundPrefixException(String message, Throwable cause) {
    super(message, cause);
  }

  public NotFoundPrefixException(Throwable cause) {
    super(cause);
  }

  public NotFoundPrefixException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
