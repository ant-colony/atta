package org.typeunsafe.atta.sensors.core

trait location {
  //TODO: WIP ... adress, other, ...
  String locationName = null

  // add a GPS Sensor to the gateway
  Double latitude = null
  Double longitude = null

  Double x = null
  Double y = null
  Double z = null

  Double xVelocity = null
  Double yVelocity = null
  Double zVelocity = null

  Double xVelocity() { return this.xVelocity }
  Double yVelocity() { return this.yVelocity }
  Double zVelocity() { return this.zVelocity }

  Double x() {
    return this.x
  }

  Double y() {
    return this.y
  }

  Double z() {
    return this.z
  }

}