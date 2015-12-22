package org.typeunsafe.atta.core
import io.vertx.core.Vertx
/**
 * Helper: fluent api to play with time
 */
class Timer {
  Integer delay = null
  long timerID
  private static Vertx vertx = Utils.vertx
  private String kind = null

  static Timer every() {
    return new Timer(kind: "every")
  }

  static Timer after() {
    return new Timer(kind: "after")
  }

  //TODO: double? -> see milliSeconds
  Timer seconds(Integer delay) {
    this.delay = delay * 1000
    return this
  }

  Timer milliSeconds(Integer delay) {
    this.delay = delay
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
