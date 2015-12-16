package org.typeunsafe.atta.sensors

import org.typeunsafe.atta.core.Message
import org.typeunsafe.atta.gateways.Gateway

import java.util.concurrent.ExecutorService


interface Sensor {
  // send message to the gateway
  void notifyGateway()

  // receive message from something, is notified
  void update(Message message)

  void generateData()
  void start()

  Object data() // a sensor can have several units

  String id()
  void id(String id)

  String topic()
  void topic(String topic)

  Integer delay()
  void delay(Integer ms)




  Gateway gateway()
  Sensor gateway(Gateway gateway)

  Sensor execEnv(ExecutorService execEnv)



}