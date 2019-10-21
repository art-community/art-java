package ru.art.generator.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotFoundPrefixException extends RuntimeException {

  public NotFoundPrefixException(String message) {
    super(message);
  }
}
