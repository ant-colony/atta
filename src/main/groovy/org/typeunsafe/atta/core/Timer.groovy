package org.typeunsafe.atta.core

import io.vertx.groovy.core.Vertx

/**
 * Helper: fluent api to play with time
 */
class Timer {
  Integer delay = null
  long timerID
  private static Vertx vertx = Utils.vertx
  private String kind = null

  static Timer every(Integer delay) {
    return new Timer(kind: "every", delay: delay)
  }

  static Timer after(Integer delay) {
    return new Timer(kind: "after", delay: delay)
  }

  Timer seconds() {
    this.delay = this.delay * 1000
    return this
  }

  Timer milliSeconds() {
    this.delay = this.delay * 1
    return this
  }

  void run(Closure r) {
    if(kind=="every") {
      this.timerID = vertx.setPeriodic(this.delay,  { id ->
        r()
      });
    }
    if(kind=="after") {
      this.timerID = vertx.setTimer(this.delay,  { id ->
        r()
      });
    }
  }

  void run(Runnable r) {
    if(kind=="every") {
      this.timerID = vertx.setPeriodic(this.delay,  { id ->
        r.run()
      });
    }
    if(kind=="after") {
      this.timerID = vertx.setTimer(this.delay,  { id ->
        r.run()
      });
    }
  }


  void cancel() {
    vertx.cancelTimer(this.timerID);
  }
}
