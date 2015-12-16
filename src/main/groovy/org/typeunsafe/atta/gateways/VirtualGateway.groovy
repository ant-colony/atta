package org.typeunsafe.atta.gateways
import org.typeunsafe.atta.core.Message
import org.typeunsafe.atta.core.SimpleMessage
import org.typeunsafe.atta.gateways.abilities.commonGatewayAbilities
import org.typeunsafe.atta.sensors.Sensor

class VirtualGateway implements Gateway, commonGatewayAbilities {
  String topic = "/virtual"
  @Override
  void update(Message message) {
    SimpleMessage msg = message
    println("You've got a message: $msg.from $msg.content on topic $msg.topic")
  }

  @Override
  void notifySensor(Sensor sensor) {
    sensor.update(new SimpleMessage(topic:"get_value"))
  }

  @Override
  void notifyAllSensors() {
    sensors.each { sensor ->
        this.notifySensor(sensor)
    }
  }

  @Override
  void start(Closure work) {

    this.checkExecutorService();

    execEnv.execute((Runnable){
      println("starting sensors ...");

      sensors.each { sensor ->
        sensor.start()
      }
      println("$id is started ...")
      work()

    })
  }

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

  @Override
  VirtualGateway sensors(List<Sensor> sensors) {
    this.sensors += sensors
    this.checkExecutorService();
    sensors.each { sensor ->
      sensor.gateway(this).execEnv(this.execEnv)
    }
    return this
  }

  @Override
  HashMap<String, Object> lastSensorsData() {
    return null
  }
}
