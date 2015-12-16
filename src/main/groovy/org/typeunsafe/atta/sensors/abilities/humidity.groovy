package org.typeunsafe.atta.sensors.abilities


trait humidity {
    Integer minHumidity = 0
    Integer maxHumidity = 100
    String humidityUnit = "%"

    Integer humidityValue = null

    Integer getHumidityLevel() {
        Random random = new Random()
        return random.nextInt(maxHumidity-minHumidity+1)+maxHumidity
    }
}