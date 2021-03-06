package org.typeunsafe.atta.sensors

import org.typeunsafe.atta.sensors.core.location
import org.typeunsafe.atta.sensors.abilities.randomHumidity
import org.typeunsafe.atta.sensors.abilities.randomTemperature

class DHTSensor extends TemplateSensor implements randomTemperature, randomHumidity, location {
    String topic = "dhtsensors" // emission topic
    //Integer delay = 10000 // default is 5000

    @Override
    void generateData() {
        this.humidityValue = this.getHumidityLevel()
        this.temperatureValue = this.getTemperatureLevel()
    }

    @Override
    Object data() {
        return [
                "kind": "DHT",
                "locationName":this.locationName,
                "temperature":["value": this.temperatureValue, "unit": this.temperatureUnit],
                "humidity":["value": this.humidityValue, "unit": this.humidityUnit]
        ]
    }

}
