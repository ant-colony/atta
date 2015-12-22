package org.typeunsafe.atta.sensors
import org.typeunsafe.atta.sensors.abilities.temperature
import org.typeunsafe.atta.sensors.core.location

import java.time.LocalDateTime

/**
 *
 */
class TemperatureSensor extends TemplateSensor implements temperature, location {
  String topic = "temperatures" // emission topic

  @Override
  void generateData() {
    LocalDateTime now = LocalDateTime.now()
    Double t = now.getMinute() + now.getSecond() / 100
    this.temperatureValue = this.getTemperatureLevel(t)
  }

  @Override
  Object data() {
    return [
        "kind": "TC°",
        "locationName":this.locationName,
        "temperature":["value": this.temperatureValue, "unit": this.temperatureUnit]
    ]
  }
}
