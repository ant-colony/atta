package org.typeunsafe.atta.sensors

import org.typeunsafe.atta.sensors.core.location
import org.typeunsafe.atta.sensors.abilities.sound

class SoundSensor extends TemplateSensor implements sound, location {
    String topic="soundsensors"
    @Override
    void generateData() {
        this.soundValue = this.getVoltageLevel()
    }

    @Override
    Object data() {
        return [
                "kind": "SoundSensor",
                "locationName":this.locationName,
                "sound":["value": this.soundValue, "unit": this.soundUnit]
        ]
    }
}
