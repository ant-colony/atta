package org.typeunsafe.atta.gateways.abilities

import java.time.Duration
import java.time.LocalDateTime

trait logging {
  HashMap<String,LocalDateTime> tasks = new HashMap<>()

  void startLog(String something) {
    tasks.put(something, LocalDateTime.now())
  }

  Map updateLog(String something) {
    LocalDateTime start = tasks.get(something)
    LocalDateTime end = LocalDateTime.now()
    Integer delay = Duration.between(start, end).toMillis()

    println(
        "-> " + something+" start:" + start.toLocalTime().toString()
            + " end:" + end.toLocalTime().toString()
            + " delay:" + delay + " ms"
    )

    return ["task":something, "start":start.toLocalTime(), "end":end.toLocalTime(), "delay":delay]

  }

}