package org.typeunsafe.atta.gateways.coap
import org.eclipse.californium.core.CoapServer
import org.typeunsafe.atta.gateways.Gateway
import org.typeunsafe.atta.gateways.abilities.gatewayAbilities
import org.typeunsafe.atta.gateways.abilities.supervisable
/**
 * Class SimpleCoapGateway
 *
 * ### implements GatewayAbilities:
 * ```groovy
 * String id=null
 * String locationName=null
 * String kind = null
 * List<Sensor> sensors = []
 * ExecutorService execEnv = null
 * JsonSlurper jsonSlurper = new JsonSlurper()
 * HashMap<String,Object> lastSensorsData = new HashMap<>()
 *
 * ```
 */
class SimpleCoapGateway implements Gateway, gatewayAbilities, supervisable {
  Integer coapPort = null
  String path = "gateway" //identifier of the only resource

  void initializeBeforeWork() {
    this.kind("CoAP")
    println("*** CoAP Gateway Initializing ***")
    println("coapPort:$coapPort")
    println("path:$path")
    CoapServer server = new CoapServer(coapPort)
    GatewayResource gatewayResource = new GatewayResource(this.path)
    gatewayResource.gateway = this
    server.add(gatewayResource)
    server.start()
  }

}
