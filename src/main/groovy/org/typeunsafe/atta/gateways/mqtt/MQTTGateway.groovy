package org.typeunsafe.atta.gateways.mqtt
import org.typeunsafe.atta.core.Message
import org.typeunsafe.atta.core.SimpleMessage
import org.typeunsafe.atta.gateways.Gateway
import org.typeunsafe.atta.gateways.abilities.commonGatewayAbilities
import org.typeunsafe.atta.gateways.mqtt.tools.MQTTBroker
import org.typeunsafe.atta.gateways.mqtt.tools.MQTTDevice
import org.typeunsafe.atta.sensors.Sensor
/**
 *
 * MQTTGateway is a MQTT Client (with Paho library),
 * it implements **commonGatewayAbilities** (id, locationName,
 * kind, sensors (`[]`), execEnv (`ExecutorService`), jsonSlurper,
 * lastSensorsData (`HashMap<String,Object>`)
 *
 * You can initialize the gateway like that
 *
 * ```groovy
 * ExecutorService env = Executors.newCachedThreadPool()
 *
 * def mqttGatewayOne = new MQTTGateway(
 *   id:"myMQTTGateway",
 *   mqttId: "mqttgw001",
 *   execEnv: env,  // if not set, the gateway uses org.typeunsafe.atta.core.Utils.executorService
 *   locationName: "Home",
 *   broker: new MQTTBroker(protocol:"tcp", host:"localhost", port:1883),
 *   connectOptions: MQTTHelper.getConnectOptions()
 * ).sensors([ // and you can add sensors to the gateway
 *     new DHTSensor(id:"dhtRoom1", locationName: "ROOM1"),
 *     new LightSensor(id:"lightRoom9B", locationName: "ROOM9")
 * ])
 * ```
 */
class MQTTGateway extends MQTTDevice implements Gateway, commonGatewayAbilities {

  //It allows to recognize the type of the gateway (useful ?)
  String kind = "MQTT"

  MQTTGateway(String id, String mqttId, String locationName, MQTTBroker broker) {
    this.id = id
    this.mqttId = mqttId
    this.locationName = locationName
    this.broker = broker
  }

  MQTTGateway() {}

/**
   * This method is called by the sensor,
   * *I'm notified by a sensor*
   *
   * @param message
   *
   */
  @Override
  void update(Message message) {
    //I've got a message from a sensor (from, topic, content)
    SimpleMessage msg = message
    println("You've got a message from sensor $msg.from $msg.content on topic $msg.topic");
    //Cast message content to Hashmap
    HashMap<String,Object> sensorData = jsonSlurper.parseText(msg.content)

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
  @Override
  void notifySensor(Sensor sensor) {
    sensor.update(new SimpleMessage(topic:"get_value"))
  }

  /**
   * The gateway notifies all sensors
   */
  @Override
  void notifyAllSensors() {
    sensors.each { sensor ->
      sensor.update(new SimpleMessage(topic:"get_value"))
    }
  }

  /*--- useful for golo ---*/
  void onStart() {

  }

  void start() {
    this.checkExecutorService();

    execEnv.execute((Runnable){
      println("starting sensors ...");

      sensors.each { sensor ->
        sensor.start()
      }

      println("starting gateway ...");
      println("MQTT Gateway $id is started and emitting/listening on $broker.port");

      //when all is ok, do the job
      this.onStart()
    })

  }
  /*--- ---*/

  /**
   * Run a closure when started *(I've got something to do, this is my work)*
   * @param work
   */
  @Override
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
      println("MQTT Gateway $id is started and emitting/listening on $broker.port");

      //when all is ok, do the job
      work()
    })

  }

  /**
   * Run a closure when provisioning the gateway *(Hello, it's me!)*
   * @param work
   */
  @Override
  void provisioning(Closure work) {

  }

  @Override
  String id() {
    return this.id
  }

  @Override
  void id(String id) {
    this.id = id
  }

  @Override
  String locationName() {
    return this.locationName
  }

  @Override
  void locationName(String locationName) {
    this.locationName = locationName
  }
/**
   * Set sensors property / you can add sensors at runtime
   * `.sensors([]).sensors([]).sensors([]) ...`
   * @param sensors
   * @return this
   */
  @Override
  MQTTGateway sensors(List<Sensor> sensors) {
    this.sensors += sensors

    /**
     * Check if executorService is null
     * if null this.execEnv = org.typeunsafe.atta.core.Utils.executorService
     * (Executors.newCachedThreadPool())
     */
    this.checkExecutorService();

    sensors.each { sensor ->
      sensor.gateway(this).execEnv(this.execEnv)
    }

    return this
  }

  /**
   * Get the last data of the sensors
   * @return this
   */
  @Override
  HashMap<String, Object> lastSensorsData() {
    return lastSensorsData
  }

}
