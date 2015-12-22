package groovy
import org.typeunsafe.atta.gateways.mqtt.MQTTGateway
import org.typeunsafe.atta.gateways.mqtt.tools.MQTTBroker
import org.typeunsafe.atta.sensors.TemperatureSensor

import static org.typeunsafe.atta.core.Timer.every

def broker = new MQTTBroker(protocol:"tcp", host:"localhost", port:1883)

def gateway = new MQTTGateway(
    id:"g001",
    mqttId: "g001",
    locationName: "somewhere",
    broker: broker
).sensors([
    new TemperatureSensor(id:"001", minTemperature: -5.0, maxTemperature: 10.0, delay: 1000, locationName:"RoomA"),
    new TemperatureSensor(id:"002", minTemperature: 0.0, maxTemperature: 20.0, delay: 1000, locationName:"RoomB")
])

gateway.connect(success: { token ->
  println "$gateway.mqttId is connected"

  gateway.start {

    gateway.startLog("emitting")

    every().milliSeconds(100).run {
      gateway.notifyAllSensors()

      gateway.startLog("publication")

      gateway
        .topic("home/sensors")
        .jsonContent(gateway.lastSensorsData())
        .publish(success: {publishToken ->
          def res = gateway.updateLog("publication")
          //println(res.delay)

        })

      gateway.updateLog("emitting")
    }
  }

})