package ch03.sample1;

import org.springframework.context.ApplicationEvent;

/**
 * Represents a heartbeat event
 */
public class HeartbeatEvent extends ApplicationEvent {

  /**
   * Create a new HeartbeatEvent.
   * 
   * @param source the component that published the event
   */
  public HeartbeatEvent(Object source) {
    super(source);
  }
}
