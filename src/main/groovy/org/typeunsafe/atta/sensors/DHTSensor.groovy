package org.typeunsafe.atta.sensors

import org.typeunsafe.atta.sensors.core.location
import org.typeunsafe.atta.sensors.abilities.humidity
import org.typeunsafe.atta.sensors.abilities.temperature

class DHTSensor extends TemplateSensor implements temperature, humidity, location {
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
