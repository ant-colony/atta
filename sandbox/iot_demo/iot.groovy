package iot_demo
import org.typeunsafe.atta.gateways.Gateway
import org.typeunsafe.atta.gateways.coap.SimpleCoapGateway
import org.typeunsafe.atta.gateways.mqtt.MQTTGateway
import org.typeunsafe.atta.gateways.mqtt.tools.MQTTBroker
import org.typeunsafe.atta.sensors.HumiditySensor
import org.typeunsafe.atta.sensors.LightSensor
import org.typeunsafe.atta.sensors.SoundSensor
import org.typeunsafe.atta.sensors.TemperatureSensor

import static org.typeunsafe.atta.core.Timer.every

MQTTBroker broker = new MQTTBroker(protocol:"tcp", host:"localhost", port:1883)

SimpleCoapGateway coapGateway0 = new SimpleCoapGateway(id:"coapgw0", coapPort: 5686, locationName: "Work", path:"work")

coapGateway0.sensors([
    new SoundSensor(id:"soundRoom9",locationName: "OFFICE03"),
    new LightSensor(id:"lightRoom9A", locationName: "ROOM9")

]).start {
  every(5).seconds().run {
    coapGateway0.notifyAllSensors() // I want all data of my sensors each 5s
  }
}


Gateway gateway1 = new MQTTGateway(
    id:"g001",
    mqttId: "g001",
    locationName: "somewhere",
    broker: broker
).sensors([
    new TemperatureSensor(id:"001", minTemperature: -5.0, maxTemperature: 10.0, delay: 1000, locationName:"ChildrenRoom"),
    new HumiditySensor(id:"H003", locationName:"Garden")
])

Gateway gateway2 = new MQTTGateway(
    id:"g002",
    mqttId: "g002",
    locationName: "somewhere",
    broker: broker
).sensors([
    new TemperatureSensor(id:"T003", minTemperature: -5.0, maxTemperature: 10.0, delay: 1000, locationName:"ParentsRoom"),
    new HumiditySensor(id:"H002", locationName:"BathRoom")
])


gateway1.connect(success: { token ->

  gateway1.start {

    every(3).seconds().run {
      gateway1.notifyAllSensors()

      gateway1
        .topic("home/g1/sensors")
        .jsonContent(gateway1.lastSensorsData())
        .publish(success: {publishToken -> })

    }
  }

})

gateway2.connect(success: { token ->

  gateway2.start {

    every(4).seconds().run {
      gateway2.notifyAllSensors()

      gateway2
          .topic("home/g2/sensors")
          .jsonContent(gateway2.lastSensorsData())
          .publish(success: {publishToken -> })

    }
  }

})