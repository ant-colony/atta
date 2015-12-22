package org.typeunsafe.atta.sensors.abilities

//y = Amplitude() * cos B(x - unitsTranslatedToTheRight) + unitsTranslatedUp()
//ref:https://www.niwa.co.nz/education-and-training/schools/resources/climate/modelling

trait temperature {
  Double minTemperature = -10.0
  Double maxTemperature = 10.0
  Double B = Math.PI / 2
  Double unitsTranslatedToTheRight = new Random().nextInt(5).toDouble()
  String temperatureUnit = "Celsius"

  Double temperatureValue = null

  Double amplitude() { return (maxTemperature-minTemperature)/2 }
  Double unitsTranslatedUp() { return minTemperature + amplitude() }

  Double getTemperatureLevel(Double t) {
    return amplitude() * Math.cos(B *(t-unitsTranslatedToTheRight)) + unitsTranslatedUp()
  }


}