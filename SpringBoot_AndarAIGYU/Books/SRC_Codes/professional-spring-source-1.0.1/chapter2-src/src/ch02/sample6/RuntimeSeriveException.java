package ch02.sample6;

/**
 * Represents root of Unchecked (non-recoverable) service exception hierarchy
 */
public class RuntimeSeriveException extends RuntimeException {

  public RuntimeSeriveException() {
    super();
  }

  public RuntimeSeriveException(String message) {
    super(message);
  }

  public RuntimeSeriveException(Throwable cause) {
    super(cause);
  }

  public RuntimeSeriveException(String message, Throwable cause) {
    super(message, cause);
  }
}
