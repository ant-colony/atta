package groovy
import org.typeunsafe.atta.gateways.mqtt.MQTTGateway
import org.typeunsafe.atta.gateways.mqtt.tools.MQTTBroker
import org.typeunsafe.atta.gateways.mqtt.tools.MQTTHelper
import org.typeunsafe.atta.sensors.DHTSensor
import org.typeunsafe.atta.sensors.LightSensor

//import java.util.concurrent.ExecutorService
//import java.util.concurrent.Executors

import static org.typeunsafe.atta.core.Timer.after
import static org.typeunsafe.atta.core.Timer.every

//ExecutorService env = Executors.newCachedThreadPool()
// execEnv: env


def mqttGatewayOne = new MQTTGateway(
    id:"myMQTTGateway",
    mqttId: "mqttgw001",
    locationName: "Home",
    broker: new MQTTBroker(protocol:"tcp", host:"localhost", port:1883),
    connectOptions: MQTTHelper.getConnectOptions()
).sensors([
    new DHTSensor(id:"dhtRoom1", locationName: "ROOM1"),
    new DHTSensor(id:"dhtRoom2", locationName: "ROOM2"),
    new LightSensor(id:"lightRoom9A", locationName: "ROOM9"),
    new LightSensor(id:"lightRoom9B", locationName: "ROOM9")
]).sensors([
    new DHTSensor(id:"DHT--ROOM--000", locationName: "ROOM0")
])


mqttGatewayOne.connect(
    messageArrived: { topic, message ->
      println "you've got a message on topic: $topic: $message"
    },
    deliveryComplete: { token ->
      println "delivery complete!!!"
    },
    connectionLost: { e ->
      println "HUSTON? WE'VE GOT A PROBLEM!!!"
    },
    success: { token ->
      println mqttGatewayOne.id + ": connection is ok"

      // subscription
      mqttGatewayOne.subscribe(
          topic:"home/bob",
          success:{ t -> null },
          failure:{ t, e -> null}
      )

      mqttGatewayOne.start {

        every(2).seconds().run {
          mqttGatewayOne.notifyAllSensors() // eh!, please, give me some data

          after(1).seconds().run {
            // sensor's data publication
            mqttGatewayOne
              .topic("home/sensors")
              .jsonContent(mqttGatewayOne.lastSensorsData())
              .publish(
                success: { t -> null },
                failure: { t, e -> null}
              )
          }
        }
      }

    },
    failure: { token, e ->
      println mqttGatewayOne.id + ": connection is ko"
      println e.message
    }
)


