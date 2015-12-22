package org.typeunsafe.atta.gateways.abilities

import groovy.json.JsonSlurper
import org.typeunsafe.atta.core.Message
import org.typeunsafe.atta.core.SimpleMessage
import org.typeunsafe.atta.gateways.Gateway
import org.typeunsafe.atta.sensors.Sensor

import java.util.concurrent.ExecutorService

trait GatewayAbilities implements Gateway {

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

  /**
   * This method is called by the sensor,
   * *I'm notified by a sensor*
   *
   * @param message
   *
   */
  void update(Message message) {
    //I've got a message from a sensor (from, topic, content)
    SimpleMessage msg = message as SimpleMessage
    //TODO: disable this println
    println("You've got a message from sensor $msg.from $msg.content on topic $msg.topic");
    //Cast message content to Hashmap
    HashMap<String,Object> sensorData = jsonSlurper.parseText(msg.content) as HashMap<String, Object>

    //Add some informations to sensorData
    sensorData.put("when", new Date())
    sensorData.put("gateway", [
        "id": this.id,
        "kind": this.kind,
        "locationName": this.locationName
    ])
    //you can get the data
    lastSensorsData.put(msg.from, sensorData)


  }

  /**
   * The gateway notifies the sensor, *he! give me a value!*
   * @param sensor
   */
  void notifySensor(Sensor sensor) {
    sensor.update(new SimpleMessage(topic:"get_value"))
  }

  /**
   * The gateway notifies all sensors
   */
  void notifyAllSensors() {
    sensors.each { sensor ->
      sensor.update(new SimpleMessage(topic:"get_value"))
    }
  }

  void initializeBeforeWork() {
    println("--- initializing ---")
  }

  /**
   * Run a closure when started *(I've got something to do, this is my work)*
   * @param work
   */
  void start(Closure work) {

    /**
     * Check if executorService is null
     * if null this.execEnv = org.typeunsafe.atta.core.Utils.executorService
     * (Executors.newCachedThreadPool())
     */
    this.checkExecutorService();

    execEnv.execute((Runnable){
      println("starting sensors ...");

      sensors.each { sensor ->
        sensor.start()
      }

      println("starting gateway ...");
      //println("$kind Gateway $id is started and emitting/listening on $broker.port");
      println("$kind Gateway $id is started and emitting/listening");

      this.initializeBeforeWork()

      //when all is ok, do the job
      work()
    })

  }


  /**
   * Run a closure when provisioning the gateway *(Hello, it's me!)*
   * @param work
   */
  void provisioning(Closure work) {
    //TODO
    //TODO: check if overriding is possible
  }

  String id() {
    return this.id
  }

  void id(String id) {
    this.id = id
  }

  String locationName() {
    return this.locationName
  }

  void locationName(String locationName) {
    this.locationName = locationName
  }

/**
 * Set sensors property / you can add sensors at runtime
 * `.sensors([]).sensors([]).sensors([]) ...`
 * @param sensors
 * @return this
 */
  Gateway sensors(List<Sensor> sensors) {
    this.sensors += sensors

    /**
     * Check if executorService is null
     * if null this.execEnv = org.typeunsafe.atta.core.Utils.executorService
     * (Executors.newCachedThreadPool())
     */
    this.checkExecutorService();

    sensors.each { sensor ->
      sensor.gateway((Gateway)this).execEnv(this.execEnv)
    }

    return (Gateway)this
  }

  /**
   * Get the last data of the sensors
   * @return this
   */
  HashMap<String, Object> lastSensorsData() {
    return lastSensorsData
  }
}