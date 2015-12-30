package boids_samples_2
import org.typeunsafe.atta.gateways.mqtt.MQTTGateway
import org.typeunsafe.atta.gateways.mqtt.tools.MQTTBroker
import org.typeunsafe.atta.sensors.animals.Boid
import org.typeunsafe.atta.sensors.animals.BoidSensor
import org.typeunsafe.atta.sensors.animals.Constraints

import static org.typeunsafe.atta.core.Timer.every


public class SpaceCowSensor extends BoidSensor {
  String nickName
  String kind = "Cow"
  String locationName = "Dallas"
  String sex = null

  // each cow has a size, I use it to display the cow in a webapp
  Double size = 5.0

  Integer delay = 1000 // refresh rate

  @Override
  Object data() {

    return [
        "kind": this.kind,
        "locationName": this.locationName,
        "position": ["x": this.x, "y": this.y],
        "size": this.size,
        "id": this.id,
        "nickName": this.nickName,
        "sex": this.sex,
        "xVelocity": this.xVelocity,
        "yVelocity": this.yVelocity
    ]
  }

}

MQTTBroker broker = new MQTTBroker(protocol:"tcp", host:"localhost", port:1883)

Constraints constraints = new Constraints(
    border:5,
    width: 800,
    height: 600,
    maxVelocity: 5
)

Double random(Double value) {
  return (new Random()).nextInt(value.toInteger()).toDouble()
}

String uuid() {
  return java.util.UUID.randomUUID().toString()
}

List<Boid> cows = []
Integer counter = 1
String sex = "female"

every(1).seconds().run({

  MQTTGateway g =new MQTTGateway(
      id:"g${counter}",
      mqttId: "g${counter}",
      locationName: "somewhere",
      broker: broker
  ).sensors([
      new SpaceCowSensor(id: uuid(), nickName:"cow-${counter}", sex:sex, size:5, x: random(constraints.width), y: random(constraints.height), constraints: constraints, boids: cows),
  ])
  counter+=1
  if (sex=="female") sex="male" else sex="female"

  g.connect(success: { token ->

    g.start { // this is a thread

      every(1).seconds().run {
        g.notifyAllSensors()
        g.topic("cows/move")
            .jsonContent(g.lastSensorsData())
            .publish(success: {publishToken ->
          println(g.lastSensorsData())
        })
      } // end every
    } // end start
  })
})




