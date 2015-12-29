package boids_samples

import org.typeunsafe.atta.gateways.Gateway
import org.typeunsafe.atta.gateways.mqtt.MQTTGateway
import org.typeunsafe.atta.gateways.mqtt.tools.MQTTBroker
import org.typeunsafe.atta.sensors.animals.Boid
import org.typeunsafe.atta.sensors.animals.BoidSensor
import org.typeunsafe.atta.sensors.animals.Constraints

import static org.typeunsafe.atta.core.Timer.every

class SpaceCowSensor extends BoidSensor {
  String nickName
  String kind = "Cow"
  String locationName = "Dallas"
  String sex = null
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

List<Boid> boids = []

/*
SpaceCowSensor c1 = new SpaceCowSensor(id: "c1", nickName: "Prudence", sex:"female", x: 10, y: 10, constraints: constraints, boids: boids)
SpaceCowSensor c2 = new SpaceCowSensor(id: "c2", nickName: "Caramelle", sex:"female", x: 30, y: 30, constraints: constraints, boids: boids)
SpaceCowSensor c3 = new SpaceCowSensor(id: "c3", nickName: "Martha", sex:"female", x: 50, y: 50, constraints: constraints, boids: boids)
*/

def spaceCows = [
    new SpaceCowSensor(id: uuid(), nickName:"Prudence", sex:"female", size:5, x: random(constraints.width), y: random(constraints.height), constraints: constraints, boids: boids),
    new SpaceCowSensor(id: uuid(), nickName:"Hazel", sex:"female", size:5, x: random(constraints.width), y: random(constraints.height), constraints: constraints, boids: boids),
    new SpaceCowSensor(id: uuid(), nickName:"Daisy", sex:"female", size:5, x: random(constraints.width), y: random(constraints.height), constraints: constraints, boids: boids),
    new SpaceCowSensor(id: uuid(), nickName:"Lilly", sex:"female", size:5, x: random(constraints.width), y: random(constraints.height), constraints: constraints, boids: boids),
    new SpaceCowSensor(id: uuid(), nickName:"Cinnamon", sex:"female", size:5, x: random(constraints.width), y: random(constraints.height), constraints: constraints, boids: boids),
    new SpaceCowSensor(id: uuid(), nickName:"Caramelle", sex:"female", size:5, x: random(constraints.width), y: random(constraints.height), constraints: constraints, boids: boids),
    new SpaceCowSensor(id: uuid(), nickName:"Martha", sex:"female", size:5, x: random(constraints.width), y: random(constraints.height), constraints: constraints, boids: boids),
    new SpaceCowSensor(id: uuid(), nickName:"Moode", sex:"female", size:5, x: random(constraints.width), y: random(constraints.height), constraints: constraints, boids: boids),
    new SpaceCowSensor(id: uuid(), nickName:"Button", sex:"female", size:5, x: random(constraints.width), y: random(constraints.height), constraints: constraints, boids: boids),

    new SpaceCowSensor(id: uuid(), nickName:"Ginger", sex:"female", size:5, x: random(constraints.width), y: random(constraints.height), constraints: constraints, boids: boids),
    new SpaceCowSensor(id: uuid(), nickName:"Belle", sex:"female", size:5, x: random(constraints.width), y: random(constraints.height), constraints: constraints, boids: boids),
    new SpaceCowSensor(id: uuid(), nickName:"Doris", sex:"female", size:5, x: random(constraints.width), y: random(constraints.height), constraints: constraints, boids: boids),
    new SpaceCowSensor(id: uuid(), nickName:"Moomy", sex:"female", size:5, x: random(constraints.width), y: random(constraints.height), constraints: constraints, boids: boids),
    new SpaceCowSensor(id: uuid(), nickName:"Brownie", sex:"female", size:5, x: random(constraints.width), y: random(constraints.height), constraints: constraints, boids: boids),
    new SpaceCowSensor(id: uuid(), nickName:"Dream", sex:"female", size:5, x: random(constraints.width), y: random(constraints.height), constraints: constraints, boids: boids),
    new SpaceCowSensor(id: uuid(), nickName:"Sweetie", sex:"female", size:5, x: random(constraints.width), y: random(constraints.height), constraints: constraints, boids: boids),

    new SpaceCowSensor(id: uuid(), nickName:"Bigfoot", sex:"male", size:5, x: random(constraints.width), y: random(constraints.height), constraints: constraints, boids: boids),
    new SpaceCowSensor(id: uuid(), nickName:"Vegas", sex:"male", size:5, x: random(constraints.width), y: random(constraints.height), constraints: constraints, boids: boids),
    new SpaceCowSensor(id: uuid(), nickName:"George", sex:"male", size:5, x: random(constraints.width), y: random(constraints.height), constraints: constraints, boids: boids),
    new SpaceCowSensor(id: uuid(), nickName:"Gus", sex:"male", size:5, x: random(constraints.width), y: random(constraints.height), constraints: constraints, boids: boids),
    new SpaceCowSensor(id: uuid(), nickName:"Kargo", sex:"male", size:5, x: random(constraints.width), y: random(constraints.height), constraints: constraints, boids: boids),
    new SpaceCowSensor(id: uuid(), nickName:"Chuck", sex:"male", size:5, x: random(constraints.width), y: random(constraints.height), constraints: constraints, boids: boids),
    new SpaceCowSensor(id: uuid(), nickName:"Boomboom", sex:"male", size:5, x: random(constraints.width), y: random(constraints.height), constraints: constraints, boids: boids),
    new SpaceCowSensor(id: uuid(), nickName:"Warpath", sex:"male", size:5, x: random(constraints.width), y: random(constraints.height), constraints: constraints, boids: boids),
    new SpaceCowSensor(id: uuid(), nickName:"Tiny", sex:"male", size:5, x: random(constraints.width), y: random(constraints.height), constraints: constraints, boids: boids),
    new SpaceCowSensor(id: uuid(), nickName:"Spice", sex:"male", size:5, x: random(constraints.width), y: random(constraints.height), constraints: constraints, boids: boids),

    new SpaceCowSensor(id: uuid(), nickName:"Berry", sex:"male", size:5, x: random(constraints.width), y: random(constraints.height), constraints: constraints, boids: boids),
    new SpaceCowSensor(id: uuid(), nickName:"Booth", sex:"male", size:5, x: random(constraints.width), y: random(constraints.height), constraints: constraints, boids: boids),
    new SpaceCowSensor(id: uuid(), nickName:"Pierce", sex:"male", size:5, x: random(constraints.width), y: random(constraints.height), constraints: constraints, boids: boids),
    new SpaceCowSensor(id: uuid(), nickName:"Crazy", sex:"male", size:5, x: random(constraints.width), y: random(constraints.height), constraints: constraints, boids: boids),
    new SpaceCowSensor(id: uuid(), nickName:"Rambo", sex:"male", size:5, x: random(constraints.width), y: random(constraints.height), constraints: constraints, boids: boids),
    new SpaceCowSensor(id: uuid(), nickName:"Rumble", sex:"male", size:5, x: random(constraints.width), y: random(constraints.height), constraints: constraints, boids: boids),
    new SpaceCowSensor(id: uuid(), nickName:"Slate", sex:"male", size:5, x: random(constraints.width), y: random(constraints.height), constraints: constraints, boids: boids),
    new SpaceCowSensor(id: uuid(), nickName:"Rant", sex:"male", size:5, x: random(constraints.width), y: random(constraints.height), constraints: constraints, boids: boids),
    new SpaceCowSensor(id: uuid(), nickName:"Coloss", sex:"male", size:5, x: random(constraints.width), y: random(constraints.height), constraints: constraints, boids: boids)

]


Gateway g = new MQTTGateway(
    id:"g001",
    mqttId: "g001",
    locationName: "somewhere",
    broker: broker
).sensors(spaceCows)

g.connect(success: { token ->

  g.start { // this is a thread

    every(1000).milliSeconds().run {
      g.notifyAllSensors()


      g.topic("cows/move")
          .jsonContent(g.lastSensorsData())
          .publish(success: {publishToken ->
            println(g.lastSensorsData())
          })

    } // end every

  } // end start

})


