module stress.my.broker

import gololang.Adapters
import gololang.Observable

import org.typeunsafe.atta.gateways.mqtt.tools.MQTTBroker
import org.typeunsafe.atta.gateways.mqtt.MQTTGateway

import org.typeunsafe.atta.core.Timer

# --- Create your own sensor ---
function PoneySensor = |id| {

  let x = Observable(0)
  x: onChange(|value| -> println("# sensor "+ id + " x:"+value))

  let y = Observable(0)
  y: onChange(|value| -> println("# sensor "+ id + " x:"+value))

  let sensorDefinition = Adapter()
    : extends("org.typeunsafe.atta.sensors.TemplateSensor")
    : overrides("generateData", |super, this| {
        x: set(java.util.Random(): nextInt(500))
        y: set(java.util.Random(): nextInt(500))
    })
    : overrides("data", |super, this| {
        return map[
          ["id", this: id()],
          ["kind", "PoneySensor"],
          ["locationName", "@Rainbow"],
          ["x", x: get()],
          ["y", y: get()],
          ["unit", "coordinates"]
        ]
    })

  let sensorInstance = sensorDefinition: newInstance()

  sensorInstance: id(id)
  sensorInstance: delay(2000)

  return sensorInstance

}

function MqttPoneyGateway = |id, mqttId, locationName, broker| {
  let gatewayDefinition = Adapter()
    : extends("org.typeunsafe.atta.gateways.mqtt.MQTTGateway")
    : implements("onPublishSuccess", |this, token| {
        println("Publication is OK")
    })
    : implements("onPublishFailure", |this, token, err| {
        println("Huston? We've got a problem!")
    })
    : implements("onStart", |this| {

        println(">>> the gateway is starting...")

        this: subscribeTo("huston/+")

        Timer.every(2): seconds(): run({

          this: notifyAllSensors()

          this: topic("poneys") # publication topic
            : jsonContent(this: lastSensorsData())
            : publish()

        })
    })
    : implements("onSuccess", |this, token| {
        println(this: id() + " is connected :)")
        # start the gateway when connection is ok
        this: start()
    })
    : implements("onFailure", |this, token, err| {
        println("Huston? We've got a problem!")
    })
    : implements("onMessageArrived", |this, topic, message| {
        println("You've got a message")
    })
    : implements("onDeliveryComplete", |this, token| {
        println("Delivery is ok :)")
    })
    : implements("onConnectionLost", |this, err| {
        println("Huston? We've got a problem!")
    })
    : implements("onSubscribeSuccess", |this, token| {
        println("Subscription is ok.")
    })

  let gatewayInstance = gatewayDefinition: newInstance()
  gatewayInstance: id(id)
  gatewayInstance: mqttId(mqttId)
  gatewayInstance: locationName(locationName)
  gatewayInstance: broker(broker)

  return gatewayInstance
}


function main = |args| {

  let broker = MQTTBroker(protocol="tcp", host="localhost", port=1883)

  5: times(|index| {

    let gateway = MqttPoneyGateway(
      id="g"+index,
      mqttId="mqtt_g"+index,
      locationName="somewhere over the rainbow",
      broker=broker
    ): sensors(list[
      PoneySensor(id="PoneysFarm_"+index)
    ])

    gateway: connect()

  })

}

