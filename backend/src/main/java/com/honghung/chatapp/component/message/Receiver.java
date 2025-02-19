package com.honghung.chatapp.component.message;

import java.util.concurrent.CountDownLatch;

import org.springframework.stereotype.Component;

/**
 * The Receiver is a POJO that defines a method for receiving messages.
 * Note: 
    For convenience, this POJO also has a CountDownLatch. This lets it signal that the message has been received. This is something you are not likely to implement in a production application.
 */

@Component
public class Receiver {
    private CountDownLatch latch = new CountDownLatch(1);

  public void receiveMessage(String message) {
    System.out.println("Received <" + message + ">");
    latch.countDown();
  }

  public CountDownLatch getLatch() {
    return latch;
  }

}
