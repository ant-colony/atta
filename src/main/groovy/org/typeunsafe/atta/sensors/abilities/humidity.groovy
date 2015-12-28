package org.typeunsafe.atta.sensors.abilities

//y = Amplitude() * cos B(x - unitsTranslatedToTheRight) + unitsTranslatedUp()
//ref:https://www.niwa.co.nz/education-and-training/schools/resources/climate/modelling

trait humidity {
  Double minHumidity = 1.0
  Double maxHumidity = 100.0
  Double B = Math.PI / 2
  Double unitsTranslatedToTheRight = new Random().nextInt(5).toDouble()
  String humidityUnit = "%"

  Double humidityValue = null

  Double amplitude() { return (maxHumidity-minHumidity)/2 }
  Double unitsTranslatedUp() { return minHumidity + amplitude() }

  Double getHumidityLevel(Double t) {
    return amplitude() * Math.cos(B *(t-unitsTranslatedToTheRight)) + unitsTranslatedUp()
  }

}