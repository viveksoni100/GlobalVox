package ch03.sample1;

import java.util.TimerTask;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

/**
 */
public class HeartbeatTask extends TimerTask implements ApplicationEventPublisherAware {
  
  private ApplicationEventPublisher eventPublisher;

  public void run() {
    HeartbeatEvent event = new HeartbeatEvent(this);
    eventPublisher.publishEvent(event);
  }

  public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
  }
}
