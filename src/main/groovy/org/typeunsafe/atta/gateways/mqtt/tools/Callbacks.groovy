package org.typeunsafe.atta.gateways.mqtt.tools

import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttToken


class Callbacks implements IMqttActionListener {

  Closure success
  Closure failure

  @Override
  void onSuccess(IMqttToken asyncActionToken) {
    success(asyncActionToken)
  }

  @Override
  void onFailure(IMqttToken asyncActionToken, Throwable exception) {
    failure(asyncActionToken, exception)
  }

}
