package org.typeunsafe.atta.core
import org.typeunsafe.atta.gateways.Gateway

import java.util.logging.FileHandler
import java.util.logging.Level
import java.util.logging.Logger

class Supervisor {
  public String scenarioName
  public String description
  public List<Gateway> gateways = []

  //public Logger logger = Logger.getLogger(this.class.getName())
  public Logger logger

  Supervisor loggerName(String logName) {
    logger = Logger.getLogger(logName)
    return this
  }

  Supervisor loggerFileName(String logFileName) {
    FileHandler fileHandler = new FileHandler(logFileName);

    fileHandler.setLevel(Level.ALL)

    logger.addHandler(fileHandler);
    return this
  }

  Supervisor gateways(List<Gateway> gateways) {
    this.gateways += gateways

    gateways.each { gateway ->
     gateway.supervisor(this)
    }

    return (Supervisor)this
  }

  /**
   * TODO: here
   * REST API to query data gateways
   */

}
