package org.typeunsafe.atta.sensors

import org.typeunsafe.atta.core.Message
import org.typeunsafe.atta.core.SimpleMessage
import groovy.json.JsonOutput
import org.typeunsafe.atta.gateways.Gateway
import org.typeunsafe.atta.sensors.core.coreProperties

import java.util.concurrent.ExecutorService

class TemplateSensor implements Sensor, coreProperties {
  String topic = "sensors" // topic for gateway notification
  Integer delay = 5000
  @Override
  void notifyGateway() {
      this.gateway.update(new SimpleMessage(
              topic: this.topic,
              from: id,
              content: JsonOutput.toJson(this.data())
      ))
  }

  @Override
  void update(Message message) {
      SimpleMessage msg = message
      if (msg.topic.equals("get_value")) { // this is a message from the gateway: "hello i'm the gateway, please give me some data ..."
          this.notifyGateway()
      }
  }

  @Override
  void generateData() {}


  @Override
  void start() {
      println("$id is started")
      execEnv.execute((Runnable){
          while (true) {
              this.generateData()
              Thread.sleep(this.delay)
          }
      })
  }

  @Override
  Object data() {}

  @Override
  String id() {
    return id
  }

  @Override
  void id(String id) {
    this.id = id;
  }

  @Override
  String topic() {
    return this.topic
  }

  @Override
  void topic(String topic) {
    this.topic = topic
  }

  @Override
  Integer delay() {
    return this.delay
  }

  @Override
  void delay(Integer ms) {
    this.delay = ms
  }

  @Override
  Gateway gateway() {
      return this.gateway
  }

  @Override
  Sensor gateway(Gateway gateway) {
      this.gateway = gateway
      return this
  }

  @Override
  Sensor execEnv(ExecutorService execEnv) {
      this.execEnv = execEnv
      return this
  }
}
