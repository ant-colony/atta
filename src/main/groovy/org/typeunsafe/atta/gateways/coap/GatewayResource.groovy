package org.typeunsafe.atta.gateways.coap

import groovy.json.JsonOutput
import org.eclipse.californium.core.CoapResource
import org.eclipse.californium.core.server.resources.CoapExchange
import org.typeunsafe.atta.gateways.Gateway

/**
 * Created by k33g_org on 11/12/15.
 */
class GatewayResource extends CoapResource {

  String name = null
  Gateway gateway = null


  GatewayResource(String name, Gateway gateway) {
    super(name)
    this.name = name
    this.gateway = gateway

    //Jedis jedis = new Jedis("localhost");
  }

  GatewayResource(String name) {
    super(name)
    this.name = name
  }

  GatewayResource gateway(Gateway gateway) {
    this.gateway = gateway
    return this
  }

  @Override
  public void handleGET(CoapExchange exchange) {
    // sensors are not directly interrogated
    // we read data in this.gateway.lastSensorsData() - memory of the gateway
    exchange.respond(JsonOutput.toJson([
        "id"          : this.gateway.id(),
        "kind"        : this.gateway.kind(),
        "locationName": this.gateway.locationName(),
        "sensors"     : this.gateway.lastSensorsData()
    ]));

  }
}
