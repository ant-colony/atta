package org.typeunsafe.atta.gateways

import org.typeunsafe.atta.gateways.abilities.GatewayAbilities

/**
 * Gateway for test
 */
class VirtualGateway implements GatewayAbilities {
  String topic = "/virtual"

  void initializeBeforeWork() {
    println("### VirtualGateway Initializing ###")
  }
}
