package org.typeunsafe.atta.sensors

import org.typeunsafe.atta.sensors.abilities.humidity
import org.typeunsafe.atta.sensors.core.location

import java.time.LocalDateTime

/**
 *
 */
class HumiditySensor extends TemplateSensor implements humidity, location {
  String topic = "humidity" // emission topic

  @Override
  void generateData() {
    LocalDateTime now = LocalDateTime.now()
    Double t = now.getMinute() + now.getSecond() / 100
    this.humidityValue = this.getHumidityLevel(t)
  }

  @Override
  Object data() {
    return [
        "kind": "H%°",
        "locationName":this.locationName,
        "humidity":["value": this.humidityValue, "unit": this.humidityUnit]
    ]
  }
}
