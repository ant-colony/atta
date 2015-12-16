package org.typeunsafe.atta.gateways.coap
import org.eclipse.californium.core.CoapServer
import org.typeunsafe.atta.core.Message
import org.typeunsafe.atta.core.SimpleMessage
import org.typeunsafe.atta.gateways.Gateway
import org.typeunsafe.atta.gateways.abilities.commonGatewayAbilities
import org.typeunsafe.atta.sensors.Sensor
/**
 * Class CoapGateway
 *
 * ### implements commonGatewayAbilities:
 * ```groovy
 * String id=null
 * String locationName=null
 * String kind = null
 * List<Sensor> sensors = []
 * ExecutorService execEnv = null
 * JsonSlurper jsonSlurper = new JsonSlurper()
 * HashMap<String,Object> lastSensorsData = new HashMap<>()
 *
 * ```
 */
class CoapGateway implements Gateway, commonGatewayAbilities {
  String kind = "CoAP"
  private Integer coapPort = null

  @Override
  void update(Message message) { // I'm notified by the sensor
    SimpleMessage msg = message // is it a good thing to do casting ?...
    println("You've got a message from sensor $msg.from $msg.content on topic $msg.topic")
    HashMap<String,Object> sensorData = jsonSlurper.parseText(msg.content)

    sensorData.put("when", new Date())
    /*
    sensorData.put("gateway", [
            "id": this.id,
            "kind": this.kind,
            "locationName": this.locationName
    ])
    */

    lastSensorsData.put(msg.from, sensorData)
  }

  @Override
  void notifySensor(Sensor sensor) {
    sensor.update(new SimpleMessage(topic:"get_value"))
  }

  @Override
  void notifyAllSensors() {
    sensors.each { sensor ->
      sensor.update(new SimpleMessage(topic:"get_value"))
    }
  }

  @Override
  void start(Closure work) {

    this.checkExecutorService();

    execEnv.execute((Runnable){

      println("starting sensors ...")
      sensors.each { sensor ->
          sensor.start()
      }

      println("starting gateway ...")
      CoapServer server = new CoapServer(coapPort)
      GatewayResource gatewayResource = new GatewayResource("gateway").gateway(this)
      server.add(gatewayResource)
      server.start()

      println("CoAP Gateway $id is started and listening on $coapPort")
      //do something if you want
      work()
    })
  }

  @Override
  void provisioning(Closure r) {

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

  @Override
  CoapGateway sensors(List<Sensor> sensors) {
    this.sensors += sensors

    this.checkExecutorService();

    this.sensors.each { sensor ->
      sensor.gateway(this).execEnv(this.execEnv)
    }

    return this
  }

  @Override
  HashMap<String, Object> lastSensorsData() {
    return this.lastSensorsData
  }
}
