#MQTT Gateway

##How to use

You can define a MQTT Gateway, with Groovy, like that:

```groovy
def mqttGatewayOne = new MQTTGateway(
    id:"myMQTTGateway",
    mqttId: "mqttgw001",
    locationName: "Home",
    broker: new MQTTBroker(protocol:"tcp", host:"localhost", port:1883),
    connectOptions: MQTTHelper.getConnectOptions()
).sensors([
    new DHTSensor(id:"dhtRoom1", locationName: "ROOM1"),
    new LightSensor(id:"lightRoom9A", locationName: "ROOM9")
])

//Connect and start the gateway
mqttGatewayOne.connect(
    success: { token ->
      println mqttGatewayOne.id + ": connection is ok"

      // subscription
      mqttGatewayOne.subscribe(
          topic:"home/bob",
          success:{ t -> null },
          failure:{ t, e -> null}
      )

      mqttGatewayOne.start {

        every().seconds(2).run {
          mqttGatewayOne.notifyAllSensors() // eh!, please, give me some data

          after().seconds(1).run {
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

```

You can create a new sensor, with Groovy, like that:

```groovy
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
```

see `sandbox/mqtt_sample/groovy/`


##You can create a MQTT broker with NodeJS

```javascript
import mosca from 'mosca';

let mqttBroker = new mosca.Server({
  port: 1883
});

// When a message is received
mqttBroker.on('published', (packet, client) => {

  if(packet.cmd=="publish") {
    console.log(client.id, packet.payload.toString());
  }

});

mqttBroker.on('ready', () => {
  console.log('MQTT Broker listening on 1883');
})
```

see `sandbox/mqtt_sample/nodejs/`


##You can create (virtual) Sensors and MQTT gateway with Golo

**Create a sensor:**

```golo
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
```

**Create a gateway:**

```golo
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

        Timer.every(): seconds(2): run({

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
```

**Use the gateway with sensor:**

```golo
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
```

see `sandbox/mqtt_sample/golo/`

##How to run

###Run the MQTT broker

    cd sandbox/mqtt_sample/nodejs
    ./broker.sh

###Run the simulator (Groovy version)

    cd sandbox/mqtt_sample/groovy
    ./mqtt.sh # or ./mqtt2.sh

###Run the simulator (Golo version)

    cd sandbox/mqtt_sample/golo
    ./mqtt.sh 
