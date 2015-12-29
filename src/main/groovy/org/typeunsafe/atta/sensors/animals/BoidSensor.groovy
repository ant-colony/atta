package org.typeunsafe.atta.sensors.animals

import org.typeunsafe.atta.sensors.TemplateSensor
import org.typeunsafe.atta.sensors.core.location

class BoidSensor extends TemplateSensor implements Boid, location {

  String topic = "boid" // emission topic
  String kind = "Boid"


  List<Boid> boids = []

  Constraints constraints = null
  Double size = null

  Double xVelocity = 1.0;
  Double yVelocity = -1.0;

  Double distanceMoveWith = 300
  Double distanceMoveCloser = 300
  Double distanceMoveAway = 15

  @Override
  void move() {
    this.x += this.xVelocity
    this.y += this.yVelocity

    if(this.x <= this.constraints.border || this.x >= this.constraints.width - this.constraints.border) {
      this.x -= this.xVelocity
      this.x = Math.max(this.x, this.constraints.border)
      this.x = Math.min(this.x, this.constraints.width - this.constraints.border)
      this.xVelocity = -this.xVelocity
      this.x += this.xVelocity
    }
    if(this.y <= this.constraints.border || this.y >= this.constraints.height - this.constraints.border) {
      this.y -= this.yVelocity
      this.y = Math.max(this.y, this.constraints.border)
      this.y = Math.min(this.y, this.constraints.height - this.constraints.border)
      this.yVelocity = -this.yVelocity
      this.y += this.yVelocity
    }
  }

  @Override
  Double distance(Boid boid) {
    Double distX = this.x - boid.x()
    Double distY = this.y - boid.y()
    return Math.sqrt(distX * distX + distY * distY)
  }

  @Override
  void moveAway(Double minDistance) {
    Double distanceX = 0.0
    Double distanceY = 0.0
    Double numClose = 0.0

    for(Integer i = 0; i < boids.size(); i++) {
      Boid boid = boids[i]

      if(boid.x() == this.x && boid.y() == this.y) {
        Double distance = this.distance(boid)
        if(distance < minDistance) {
          numClose++
          Double xdiff = (this.x - boid.x())
          Double ydiff = (this.y - boid.y())

          if(xdiff >= 0.0) xdiff = Math.sqrt(minDistance) - xdiff
          else if(xdiff < 0.0) xdiff = -Math.sqrt(minDistance) - xdiff

          if(ydiff >= 0.0) ydiff = Math.sqrt(minDistance) - ydiff
          else if(ydiff < 0.0) ydiff = -Math.sqrt(minDistance) - ydiff

          distanceX += xdiff
          distanceY += ydiff
          boid = null;
        }
      }

    }

    //if(numClose == new Double(0.0)) return
    if(numClose == new Double(0.0) || -numClose == new Double(0.0)) return

    this.xVelocity -= distanceX / 5
    this.yVelocity -= distanceY / 5
  }

  @Override
  void moveCloser(Double distance) {

    if(boids.size() < 1) return

    Double avgX = 0
    Double avgY = 0
    for(Integer i = 0; i < boids.size(); i++) {
      Boid boid = boids[i]
      if(boid.x() == this.x && boid.y() == this.y) {
        if(this.distance(boid) > distance) {

          avgX += (this.x - boid.x())
          avgY += (this.y - boid.y())

          boid = null
        }
      }

    }

    avgX /= boids.size()
    avgY /= boids.size()

    distance = Math.sqrt((avgX * avgX) + (avgY * avgY)) * -1.0

    if(distance == new Double(0.0) || -distance == new Double(0.0)) return

    this.xVelocity= Math.min(this.xVelocity + (avgX / distance) * 0.15, this.constraints.maxVelocity)
    this.yVelocity = Math.min(this.yVelocity + (avgY / distance) * 0.15, this.constraints.maxVelocity)
  }

  @Override
  void moveWith(Double distance) {

    if(boids.size() < 1) return

    // calculate the average velocity of the other boids
    Double avgX = 0
    Double avgY = 0

    for(Integer i = 0; i < boids.size(); i++) {
      Boid boid = boids[i]

      if(boid.x().equals(this.x) && boid.y().equals(this.y)) {
        if(this.distance(boid) > distance) {
          avgX += boid.xVelocity()
          avgY += boid.yVelocity()
          boid = null
        }
      }

    }

    avgX /= boids.size()
    avgY /= boids.size()

    distance = Math.sqrt((avgX * avgX) + (avgY * avgY)) * 1.0
    //if(distance == new Double(0.0)) return
    if(distance == new Double(0.0) || -distance == new Double(0.0)) return

    this.xVelocity= Math.min(this.xVelocity + (avgX / distance) * 0.05, this.constraints.maxVelocity)
    this.yVelocity = Math.min(this.yVelocity + (avgY / distance) * 0.05, this.constraints.maxVelocity)
  }

  @Override
  void beforeStart() {
    boids.add(this)
  }

  @Override
  void generateData() {
    this.moveWith(distanceMoveWith)
    this.moveCloser(distanceMoveCloser)
    this.moveAway(distanceMoveAway)
    this.move()

  }

  @Override
  Object data() {

    return [
        "kind": this.kind,
        "locationName":this.locationName,
        "position":["x": this.x, "y": this.y],
        "id":this.id()
    ]
  }

}
