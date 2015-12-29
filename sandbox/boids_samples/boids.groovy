package boids_samples
import org.typeunsafe.atta.gateways.VirtualGateway
import org.typeunsafe.atta.sensors.animals.Boid
import org.typeunsafe.atta.sensors.animals.Constraints

import org.typeunsafe.atta.sensors.animals.BoidSensor

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

import static org.typeunsafe.atta.core.Timer.every



ExecutorService env = Executors.newCachedThreadPool()

Constraints constraints = new Constraints(
    border:5,
    width: 600,
    height: 400,
    maxVelocity: 5
)

List<Boid> boids = []

BoidSensor s1 = new BoidSensor(id: "s1", x: 10, y: 10, constraints: constraints, boids: boids)
BoidSensor s2 = new BoidSensor(id: "s2", x: 30, y: 30, constraints: constraints, boids: boids)
BoidSensor s3 = new BoidSensor(id: "s3", x: 50, y: 50, constraints: constraints, boids: boids)

def g = new VirtualGateway(id: "G")


g.sensors([s1, s2, s3])

g.start { // this is a thread

  every(1).seconds().run {
    g.notifySensor(s1)
    g.notifySensor(s2)
    g.notifySensor(s3)
  }

}
