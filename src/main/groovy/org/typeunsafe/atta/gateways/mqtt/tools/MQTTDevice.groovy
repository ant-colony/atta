package org.typeunsafe.atta.gateways.mqtt.tools
import groovy.json.JsonOutput
import org.eclipse.paho.client.mqttv3.MqttAsyncClient
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

class MQTTDevice {
  String mqttId

  MQTTBroker broker() {
    return broker
  }

  void broker(MQTTBroker broker) {
    this.broker = broker
  }

  MQTTBroker broker
  Integer qos
  MqttConnectOptions connectOptions = null
  Closure whenMessageArrived = { topic, message -> null }
  Closure whenDeliveryComplete = { token -> null }
  Closure whenConnectionLost = { error -> null }


  private MqttAsyncClient client
  private String content
  private String topic

  String mqttId() {
    return mqttId
  }

  void mqttId(String mqttId) {
    this.mqttId = mqttId
  }

  MQTTDevice content (String content) {
    this.content = content
    return this
  }

  MQTTDevice jsonContent (Object content) {
    this.content = JsonOutput.toJson(content)
    return this
  }

  String content () {
    return this.content
  }


  MQTTDevice topic (String topic) {
    this.topic = topic
    return this
  }

  String topic () {
    return this.topic
  }

  private MqttAsyncClient getMqttClient () {
    MemoryPersistence persistence = new MemoryPersistence()
    return new MqttAsyncClient(this.broker.url(), this.mqttId, persistence)
  }

  private MQTTDevice activateCallBacks () {
    this.client.setCallback(new MQTTCallback(
        whenConnectionLost: this.whenConnectionLost,
        whenMessageArrived: this.whenMessageArrived,
        whenDeliveryComplete: this.whenDeliveryComplete
    ))
    return this
  }

  MQTTDevice initialize () {
    this.qos = this.qos ?: 0
    this.mqttId = this.mqttId ?: MqttClient.generateClientId()
    this.client = this.getMqttClient()

    return this
  }


  private MQTTDevice activateCallBacks (MQTTCallback mqttCallback) {
    this.client.setCallback(mqttCallback)
    return this
  }

  MQTTDevice connect (Callbacks actionListenerCbk) {

    if(connectOptions==null){
      connectOptions = MQTTHelper.getConnectOptions()
    }

    String url = this.broker.url()
    println("[$mqttId]:Connecting to broker: $url")

    this.activateCallBacks()

    this.client.connect(this.connectOptions, null,actionListenerCbk)
    return this
  }


  MQTTDevice connect (params) {

    if(connectOptions==null){
      connectOptions = MQTTHelper.getConnectOptions()
    }

    this.initialize()

    String url = this.broker.url()
    println("[$mqttId]:Connecting to broker: $url")

    if(params["connectionLost"]) {
      this.whenConnectionLost = (Closure) params["connectionLost"]
    }

    if(params["deliveryComplete"]) {
      this.whenDeliveryComplete = (Closure) params["deliveryComplete"]
    }

    if(params["messageArrived"]) {
      this.whenMessageArrived = (Closure) params["messageArrived"]
    }

    this.activateCallBacks()

    this.client.connect(this.connectOptions, null,new Callbacks(
        success: (Closure) params["success"], failure: (Closure) params["failure"]
    ))
    return this
  }



  /*--- useful for golo ---*/
  void onSuccess (token) {}
  void onFailure (token, err) {}
  void onMessageArrived (topic, message) {}
  void onDeliveryComplete (token) {}
  void onConnectionLost (err) {}

  MQTTDevice connect () {

    if(connectOptions==null){
      connectOptions = MQTTHelper.getConnectOptions()
    }

    def successClosure = { token ->
      this.onSuccess(token)
    }

    def failureClosure = { token, err ->
      this.onFailure(token, err)
    }

    def messageArrivedClosure = { topic, message ->
      this.onMessageArrived(topic, message)
    }

    def deliveryCompleteClosure = { token ->
      this.onDeliveryComplete(token)
    }

    def connectionLostClosure = { err ->
      this.onConnectionLost(err)
    }

    this.initialize()

    String url = this.broker.url()
    println("[$mqttId]:Connecting to broker: $url")

    this.whenConnectionLost = connectionLostClosure
    this.whenDeliveryComplete = deliveryCompleteClosure
    this.whenMessageArrived = messageArrivedClosure

    this.activateCallBacks()

    this.client.connect(this.connectOptions, null,new Callbacks(
        success: successClosure, failure: failureClosure
    ))
    return this

  }
  /*--- ---*/


  MQTTDevice subscribe (String topicFilter, Callbacks actionListenerCbk) {
    this.client.subscribe(topicFilter, this.qos as int, null as Object, actionListenerCbk)
    return this
  }

  MQTTDevice subscribe (Object params) {
    this.client.subscribe((String) params["topic"], this.qos as int, null as Object, new Callbacks(
        success: (Closure) params["success"], failure: (Closure) params["failure"]
    ))
    return this
  }

  /*--- useful for golo ---*/
  void onSubscribeSuccess(token) {}

  void onSubscribeFailure(token, err) {}

  MQTTDevice subscribeTo (String topicFilter) {
    this.client.subscribe(topicFilter, this.qos as int, null as Object, new Callbacks(
        success: { token ->
          this.onSubscribeSuccess(token)
        },
        failure: { token, err ->
          this.onSubscribeFailure(token, err)
        }
    ))
    return this
  }
  /*--- ---*/

  MQTTDevice unsubscribe (String topicFilter, Callbacks actionListenerCbk) {
    this.client.unsubscribe(topicFilter, null as Object, actionListenerCbk)
    return this
  }

  MQTTDevice publish (Callbacks actionListenerCbk) {
    MqttMessage message  = new MqttMessage(this.content().getBytes())
    message.setQos(this.qos)
    this.client.publish(this.topic(), message, null as Object, actionListenerCbk)
    return this
  }

  MQTTDevice publish (params) {
    MqttMessage message  = new MqttMessage(this.content().getBytes())
    message.setQos(this.qos)
    this.client.publish(this.topic(), message, null as Object, new Callbacks(
        success: (Closure) params["success"], failure: (Closure) params["failure"]
    ))
    return this
  }

  /*--- useful for golo ---*/
  void onPublishSuccess(token) {}

  void onPublishFailure(token, error) {}

  MQTTDevice publish () {
    MqttMessage message  = new MqttMessage(this.content().getBytes())
    message.setQos(this.qos)
    this.client.publish(this.topic(), message, null as Object, new Callbacks(
        success: {token ->
          this.onPublishSuccess(token)
        },
        failure: {token, error ->
          this.onPublishFailure(token, error)
        }
    ))
    return this
  }
  /*--- ---*/


  MQTTDevice disconnect (Callbacks actionListenerCbk) {
    this.client.disconnect(null as Object, actionListenerCbk)
    return this
  }


}
