package org.typeunsafe.atta.gateways.abilities
import org.typeunsafe.atta.core.Supervisor
import org.typeunsafe.atta.gateways.Gateway

import java.time.Duration
import java.time.LocalDateTime

trait supervisable {

  Supervisor supervisor = null

  HashMap<String,LocalDateTime> tasks = new HashMap<>()

  Supervisor supervisor() { return supervisor }
  void supervisor(Supervisor supervisor) { this.supervisor = supervisor }

  void startLog(String taskName) {
    tasks.put(taskName, LocalDateTime.now())
  }

  Map updateLog(String taskName) {
    return this.updateLog(taskName, true, true)
  }

  Map updateLog(String taskName, Boolean display, Boolean persistence) {
    LocalDateTime start = tasks.get(taskName)
    LocalDateTime end = LocalDateTime.now()
    Integer delay = Duration.between(start, end).toMillis()
    Gateway g = ((Gateway)this)

    Map res = [
        "scenarioName": supervisor.scenarioName,
        "gatewayId": g.id(),
        "gatewayType": g.kind(),
        "task":taskName,
        "start":start.toLocalTime(),
        "end":end.toLocalTime(),
        "delay":delay
    ]

    if(display) {

      println(
          "-> [$supervisor.scenarioName]($g.id):" + taskName+" start:" + start.toLocalTime().toString()
              + " end:" + end.toLocalTime().toString()
              + " delay:" + delay + " ms"
      )
    }

    if(persistence) {
      supervisor.logger.info(res.toMapString())
    }

    return res
  }
}