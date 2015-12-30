package org.typeunsafe.atta.gateways.mqtt
import org.typeunsafe.atta.gateways.Gateway
import org.typeunsafe.atta.gateways.abilities.gatewayAbilities
import org.typeunsafe.atta.gateways.abilities.gatewayAbilitiesWithoutClosure
import org.typeunsafe.atta.gateways.abilities.supervisable
import org.typeunsafe.atta.gateways.mqtt.tools.MQTTBroker
import org.typeunsafe.atta.gateways.mqtt.tools.MQTTDevice
/**
 *
 * MQTTGateway is a MQTT Client (with Paho library),
 * it implements **GatewayAbilities** (id, locationName,
 * kind, sensors (`[]`), execEnv (`ExecutorService`), jsonSlurper,
 * lastSensorsData (`HashMap<String,Object>`)
 *
 * You can initialize the gateway like that
 *
 * ```groovy
 * ExecutorService env = Executors.newCachedThreadPool()
 *
 * def mqttGatewayOne = new MQTTGateway(
 *   id:"myMQTTGateway",
 *   mqttId: "mqttgw001",
 *   execEnv: env,  // if not set, the gateway uses org.typeunsafe.atta.core.Utils.executorService
 *   locationName: "Home",
 *   broker: new MQTTBroker(protocol:"tcp", host:"localhost", port:1883),
 *   connectOptions: MQTTHelper.getConnectOptions()
 * ).sensors([ // and you can add sensors to the gateway
 *     new DHTSensor(id:"dhtRoom1", locationName: "ROOM1"),
 *     new LightSensor(id:"lightRoom9B", locationName: "ROOM9")
 * ])
 * ```
 *
 * class MQTTGateway extends MQTTDevice implements Gateway, GatewayWithoutClosure, GatewayAbilities, GatewayAbilitiesWithoutClosure
 *
 */
class MQTTGateway extends MQTTDevice implements Gateway, gatewayAbilities, gatewayAbilitiesWithoutClosure, supervisable {

  MQTTGateway(String id, String mqttId, String locationName, MQTTBroker broker) {
    this.id = id
    this.mqttId = mqttId
    this.locationName = locationName
    this.broker = broker
    this.kind("MQTT")
  }

  MQTTGateway() {
    this.kind("MQTT")
  }

  void initializeBeforeWork() {
    println("### MQTT Gateway Initializing ###")

  }

}
