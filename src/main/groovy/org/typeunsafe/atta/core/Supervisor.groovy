package org.typeunsafe.atta.core
import groovy.json.JsonOutput
import io.vertx.groovy.core.http.HttpServer
import io.vertx.groovy.core.http.HttpServerResponse
import io.vertx.groovy.ext.web.Router
import io.vertx.groovy.ext.web.handler.BodyHandler
import org.typeunsafe.atta.gateways.Gateway

import java.util.logging.FileHandler
import java.util.logging.Level
import java.util.logging.Logger
//import static org.typeunsafe.atta.core.Timer.every

class Supervisor {
  public String scenarioName
  public String description
  public List<Gateway> gateways = []
  //public Integer httpPort = 9090
  //JsonSlurper jsonSlurper = new JsonSlurper()

  public List<HttpServerResponse> openConnections = []

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
  void startHttpServer(Integer httpPort, Integer sseDelay=1000) {
    HttpServer server = Utils.vertx.createHttpServer()
    Router router = Router.router(Utils.vertx)

    // enable the reading of the request body for all routes (globally)
    router.route().handler(BodyHandler.create())

    //--- SSE ---

    Timer.every().milliSeconds(sseDelay).run {

      ArrayList<Object> gateways = []

      this.gateways.each {gateway ->
        gateways.add([
            id: gateway.id(),
            kind: gateway.kind(),
            location: gateway.locationName(),
            lastSensorsData: gateway.lastSensorsData()
        ])
      }

      this.openConnections.each { res ->

        StringBuilder sb = new StringBuilder('event: message\ndata: ')
        sb.append(JsonOutput.toJson(gateways))
        sb.append('\n\n')
        res.write(sb.toString())

      }
    }

    // start the "SSE single long-running, open connection"
    router.get("/sse/all", { context ->

      //HttpServerResponse res = ((RoutingContext) context).response()
      HttpServerResponse res = context.response()

      /**
       * java.lang.IllegalStateException: You must set the Content-Length header to be the total size of the message body
       * BEFORE sending any data if you are not using HTTP chunked encoding.
       */
      res.setChunked(true)

      // send headers for event-stream connection
      // see spec for more information
      res.headers().add("Content-Type", "text/event-stream")
      res.headers().add("Cache-Control", "no-cache")
      res.headers().add("Connection", "keep-alive")


      res.setStatusCode(200)
      res.write("\n")

      // push res object to our openConnections property
      this.openConnections.add(res)

      // When the request is closed, e.g. the browser window
      // is closed. We search through the open connections
      // list and remove this connection.

      res.closeHandler({
        println("<### Close SSE Handler - Bye! ###>")
        this.openConnections.remove(res);
      })

    })

    //--- SSE ---

    router.get("/api/about", { context ->
      context.sendJson([
          "about":"atta-beta-version-000"
      ])
    })

    router.get("/api/gateways", { context ->

      ArrayList<Object> gateways = []
      this.gateways.each {gateway ->
        gateways.add([
            id: gateway.id(),
            kind: gateway.kind(),
            location: gateway.locationName(),
            lastSensorsData: gateway.lastSensorsData()
        ])
      }
      context.sendJson(gateways)

    })

    router.get("/api/gateways/:id", { context ->
      Gateway gateway = this.gateways.find {gateway ->
        gateway.id().equals(context.param("id").toString())
      }
      context.sendJson([
          id: gateway.id(),
          kind: gateway.kind(),
          location: gateway.locationName(),
          lastSensorsData: gateway.lastSensorsData()
      ])
    })

    server.start(router, httpPort, "/*")
  }

}

