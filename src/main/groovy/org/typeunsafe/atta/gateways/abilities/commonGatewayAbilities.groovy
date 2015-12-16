package org.typeunsafe.atta.gateways.abilities

import groovy.json.JsonSlurper
import org.typeunsafe.atta.sensors.Sensor

import java.util.concurrent.ExecutorService

trait commonGatewayAbilities {

  String id=null
  String locationName=null
  String kind = null

  List<Sensor> sensors = []
  ExecutorService execEnv = null

  JsonSlurper jsonSlurper = new JsonSlurper()

  HashMap<String,Object> lastSensorsData = new HashMap<>()

  ExecutorService checkExecutorService() {
    if(execEnv==null) {
      println("--- Using default executorService ---")
      execEnv = org.typeunsafe.atta.core.Utils.executorService
    }
  }
}