package org.typeunsafe.atta.gateways.mqtt.tools

import org.eclipse.paho.client.mqttv3.MqttConnectOptions

/**
 * http://www.eclipse.org/paho/files/javadoc/org/eclipse/paho/client/mqttv3/MqttConnectOptions.html
 */
class MQTTHelper {

  static MqttConnectOptions getConnectOptions (Boolean cleanSession=true) {
    MqttConnectOptions connOpts = new MqttConnectOptions()
    connOpts.setCleanSession(cleanSession)
    return connOpts
  }

}
