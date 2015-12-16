package groovy

import org.typeunsafe.atta.gateways.mqtt.MQTTGateway
import org.typeunsafe.atta.gateways.mqtt.tools.MQTTBroker
import org.typeunsafe.atta.sensors.TemplateSensor

import static org.typeunsafe.atta.core.Timer.every


// --- Create your own sensor ---

class TinySensor extends TemplateSensor {
  Integer value = 0
  Integer delay = 1000 // default delay is 5000 ms
  @Override
  void generateData() {
    this.value =  new Random().nextInt(500)
  }

  @Override
  Object data() {
    return [
        "id": this.id,
        "kind": "Tiny",
        "locationName": "@Home",
        "value": this.value,
        "unit": "something"
    ]
  }
}


def broker = new MQTTBroker(protocol:"tcp", host:"localhost", port:1883)
// --- Define a MQTT Gateway
def gateway = new MQTTGateway(
    id:"g001",
    mqttId: "g001",
    locationName: "somewhere",
    broker: broker
).sensors([
    new TinySensor(id:"001"),
    new TinySensor(id:"002")
])

gateway.connect(success: { token ->
  println "$gateway.mqttId is connected"

  gateway.start {
    every().seconds(2).run {
      gateway.notifyAllSensors()

      gateway
        .topic("home/sensors")
        .jsonContent(gateway.lastSensorsData())
        .publish(success: {publishToken -> println "yeah!"})
    }
  }

})