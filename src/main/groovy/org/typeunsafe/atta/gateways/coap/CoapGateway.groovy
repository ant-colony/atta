package org.typeunsafe.atta.gateways.coap
import org.eclipse.californium.core.CoapServer
import org.typeunsafe.atta.gateways.abilities.GatewayAbilities
/**
 * Class CoapGateway
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
class CoapGateway implements GatewayAbilities {
  String kind = "CoAP"
  private Integer coapPort = null

  void initializeBeforeWork() {
    println("*** CoAP Gateway Initializing ***")
    CoapServer server = new CoapServer(coapPort)
    GatewayResource gatewayResource = new GatewayResource("gateway").gateway(this)
    server.add(gatewayResource)
    server.start()
  }

}
