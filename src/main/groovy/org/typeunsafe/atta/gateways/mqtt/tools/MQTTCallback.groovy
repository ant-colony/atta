package org.typeunsafe.atta.gateways.mqtt.tools

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage


class MQTTCallback implements MqttCallback {

  Closure whenMessageArrived
  Closure whenDeliveryComplete
  Closure whenConnectionLost

  @Override
  void connectionLost(Throwable throwableCause) {
    whenConnectionLost(throwableCause)
  }

  @Override
  void messageArrived(String stringTopic, MqttMessage mqttMessage) throws Exception {
    whenMessageArrived(stringTopic, mqttMessage)
  }

  @Override
  void deliveryComplete(IMqttDeliveryToken token) {
    whenDeliveryComplete(token)
  }
}
