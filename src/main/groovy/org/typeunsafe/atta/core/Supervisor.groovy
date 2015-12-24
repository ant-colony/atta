package org.typeunsafe.atta.core

import groovy.json.JsonSlurper
import io.vertx.groovy.core.http.HttpServer
import io.vertx.groovy.ext.web.Router
import io.vertx.groovy.ext.web.handler.BodyHandler
import org.typeunsafe.atta.gateways.Gateway

import java.util.logging.FileHandler
import java.util.logging.Level
import java.util.logging.Logger

class Supervisor {
  public String scenarioName
  public String description
  public List<Gateway> gateways = []
  //public Integer httpPort = 9090
  JsonSlurper jsonSlurper = new JsonSlurper()

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
   * REST APIs to query data gateways
   */
  void startHttpServer(Integer httpPort) {
    HttpServer server = Utils.vertx.createHttpServer()
    Router router = Router.router(Utils.vertx)

    // enable the reading of the request body for all routes (globally)
    router.route().handler(BodyHandler.create())

    router.get("/api/about", { context ->
      context.sendJson([
          "about":"atta-beta-version-000"
      ])
    })

    router.get("/api/gateways", { context ->
      ArrayList<Object> gateways = []
      this.gateways.each {gateway ->
        gateways.add([
            "id": gateway.id(),
            "kind": gateway.kind(),
            "location": gateway.locationName(),
            "lastSensorsData": gateway.lastSensorsData()
        ])
      }
      context.sendJson(gateways)

    })

    router.get("/api/gateways/:id", { context ->
      Gateway gateway = this.gateways.find {gateway ->
        gateway.id().equals(context.param("id").toString())
      }
      context.sendJson([
          "id": gateway.id(),
          "kind": gateway.kind(),
          "location": gateway.locationName(),
          "lastSensorsData": gateway.lastSensorsData()
      ])
    })

    server.start(router, httpPort, "/*")
  }

}
