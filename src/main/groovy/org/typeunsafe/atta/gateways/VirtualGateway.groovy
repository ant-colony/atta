package org.typeunsafe.atta.gateways

import org.typeunsafe.atta.core.Supervisor
import org.typeunsafe.atta.gateways.abilities.gatewayAbilities

/**
 * Gateway for test
 */
class VirtualGateway implements Gateway, gatewayAbilities {
  String topic = "/virtual"

  @Override
  Supervisor supervisor() {
    return null
  }

  @Override
  void supervisor(Supervisor supervisor) {

  }

  void initializeBeforeWork() {
    println("### VirtualGateway Initializing ###")
  }
}
