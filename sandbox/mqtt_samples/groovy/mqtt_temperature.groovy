package groovy

import org.typeunsafe.atta.core.Supervisor
import org.typeunsafe.atta.gateways.Gateway
import org.typeunsafe.atta.gateways.mqtt.MQTTGateway
import org.typeunsafe.atta.gateways.mqtt.tools.MQTTBroker
import org.typeunsafe.atta.sensors.TemperatureSensor

import static org.typeunsafe.atta.core.Timer.every

MQTTBroker broker = new MQTTBroker(protocol:"tcp", host:"localhost", port:1883)

Gateway gateway1 = new MQTTGateway(
    id:"g001",
    mqttId: "g001",
    locationName: "somewhere",
    broker: broker
).sensors([
    new TemperatureSensor(id:"001", minTemperature: -5.0, maxTemperature: 10.0, delay: 1000, locationName:"RoomA"),
    new TemperatureSensor(id:"002", minTemperature: 0.0, maxTemperature: 20.0, delay: 1000, locationName:"RoomB")
])

Gateway gateway2 = new MQTTGateway(
    id:"g002",
    mqttId: "g002",
    locationName: "somewhere",
    broker: broker
).sensors([
    new TemperatureSensor(id:"T003", minTemperature: -5.0, maxTemperature: 10.0, delay: 1000, locationName:"RoomA"),
    new TemperatureSensor(id:"T004", minTemperature: 0.0, maxTemperature: 20.0, delay: 1000, locationName:"RoomB")
])

Supervisor supervisor = new Supervisor(scenarioName:"S001", description:"DESC001")
    .loggerName("S001").loggerFileName("temperatures.log");
supervisor.gateways([gateway1, gateway2])

supervisor.startHttpServer(9090) // default sse refresh delay = 1000 ms
//supervisor.startHttpServer(9090, 2000)


gateway1.connect(success: { token ->
  println "$gateway1.mqttId is connected"

  gateway1.start {

    gateway1.startLog("emitting")

    every().milliSeconds(2000).run {
      gateway1.notifyAllSensors()

      gateway1.startLog("publication")

      gateway1
        .topic("home/sensors")
        .jsonContent(gateway1.lastSensorsData())
        .publish(success: {publishToken ->
          def res = gateway1.updateLog("publication")
          println("=> $res")

        })

      gateway1.updateLog("emitting")
    }
  }

})

gateway2.connect(success: { token ->
  println "$gateway2.mqttId is connected"

  gateway2.start {

    gateway2.startLog("emitting")

    every().milliSeconds(2000).run {
      gateway2.notifyAllSensors()

      gateway2.startLog("publication")

      gateway2
          .topic("home/sensors")
          .jsonContent(gateway2.lastSensorsData())
          .publish(success: {publishToken ->
            def res = gateway2.updateLog("publication")
            println("=> $res")

      })

      gateway2.updateLog("emitting")
    }
  }

})