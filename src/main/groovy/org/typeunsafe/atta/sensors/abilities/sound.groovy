package org.typeunsafe.atta.sensors.abilities

trait sound {
    Integer minVoltage = 4
    Integer maxVoltage = 12
    String soundUnit = "V"

    Integer soundValue = null

    Integer getVoltageLevel() {
        Random random = new Random()
        return random.nextInt(maxVoltage-minVoltage+1)+maxVoltage
    }
}