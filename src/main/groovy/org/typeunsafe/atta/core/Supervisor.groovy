package org.typeunsafe.atta.core
import org.typeunsafe.atta.gateways.Gateway

class Supervisor {
  public String scenarioName
  public String description = null
  public String path = null // path+scenarioName+date
  public List<Gateway> gateways = []

  Supervisor gateways(List<Gateway> gateways) {
    this.gateways += gateways

    gateways.each { gateway ->
     gateway.supervisor(this)
    }

    return (Supervisor)this
  }

}
