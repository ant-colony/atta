package org.typeunsafe.atta.sensors

import org.typeunsafe.atta.sensors.core.location
import org.typeunsafe.atta.sensors.abilities.light

class LightSensor extends TemplateSensor implements light, location  {
    String topic = "sensorslight"
    Integer delay = 10000 // default is 5000

    @Override
    void generateData() {
        this.lightValue = this.getLightLevel()
    }

    @Override
    Object data() {
        return [
                "kind": "LightSensor",
                "locationName":this.locationName,
                "light":["value": this.lightValue, "unit": this.lightUnit]
        ]
    }
}
