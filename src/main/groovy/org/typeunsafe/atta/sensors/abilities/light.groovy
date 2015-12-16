package org.typeunsafe.atta.sensors.abilities

trait light {
    Integer minLight = 0
    Integer maxLight = 20
    String lightUnit = "KOhm"

    Integer lightValue = null

    Integer getLightLevel() {
        Random random = new Random()
        return random.nextInt(maxLight-minLight+1)+maxLight
    }
}