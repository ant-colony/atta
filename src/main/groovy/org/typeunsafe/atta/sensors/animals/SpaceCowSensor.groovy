package org.typeunsafe.atta.sensors.animals

/**
 * ...
 */
class SpaceCowSensor extends BoidSensor {
  String nickName

  String nickName() { return nickName }
  SpaceCowSensor nickName(String nickName) {
    this.nickName = nickName
    return this
  }

}
