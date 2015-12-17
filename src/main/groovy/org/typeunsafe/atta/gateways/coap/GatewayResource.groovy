package org.typeunsafe.atta.gateways.coap

import groovy.json.JsonOutput
import org.eclipse.californium.core.CoapResource
import org.eclipse.californium.core.server.resources.CoapExchange
import org.typeunsafe.atta.gateways.Gateway

/**
 * Created by k33g_org on 11/12/15.
 */
class GatewayResource extends CoapResource {

  String identifier = null
  Gateway gateway = null


  GatewayResource(String identifier, Gateway gateway) {
    super(identifier)
    this.identifier = identifier
    this.gateway = gateway
  }

  GatewayResource(String identifier) {
    super(identifier)
    this.identifier = identifier
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
        "id"          : this.gateway.id,
        "kind"        : this.gateway.kind,
        "locationName": this.gateway.locationName,
        "sensors"     : this.gateway.lastSensorsData()
    ]));

  }

  @Override
  public void handlePOST(CoapExchange exchange) {
    //TODO:....
    /* golo sample
     onPost= |exchange| {
        exchange: accept()
        println(exchange: getRequestText())
        exchange: respond(
          JsonMessage(
            what= "temperature",
            data= RandomInteger(min=13, max=23)
          )
        )
      }
     */
  }

}
