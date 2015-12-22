package org.typeunsafe.atta.sensors.abilities

trait randomTemperature {
    Integer minTemperature = -100
    Integer maxTemperature = 100
    String temperatureUnit = "Celsius"

    Integer temperatureValue = null

    Integer getTemperatureLevel() {
        Random random = new Random()
        return random.nextInt(maxTemperature-minTemperature+1)+maxTemperature
    }
}