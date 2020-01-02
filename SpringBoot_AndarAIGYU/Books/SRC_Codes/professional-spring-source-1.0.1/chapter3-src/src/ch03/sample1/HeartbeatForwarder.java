package ch03.sample1;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

public class HeartbeatForwarder implements ApplicationListener {

  public void onApplicationEvent(ApplicationEvent event) {
    
    if (event instanceof HeartbeatEvent) {
      System.out.println("Received heartbeat event: " + event.getTimestamp());
	
	  // now tell the remote monitoring agent that we're alive
	  // ...
    }
  }
}
