package org.typeunsafe.atta.extensions
import io.vertx.core.json.Json
import io.vertx.groovy.core.http.HttpServer
import io.vertx.groovy.ext.web.Router
import io.vertx.groovy.ext.web.RoutingContext
import io.vertx.groovy.ext.web.handler.StaticHandler

class WebExtensions {

  static void start(HttpServer self, Router router, Integer port, String staticPath) {
    println("HttpServer is listening on " + port)
    router.route(staticPath).handler(StaticHandler.create())
    self.requestHandler(router.&accept).listen(port)
  }

  static void get(Router self, uri, handler) {
    self.get(uri).handler(handler)
  }

  static void post(Router self, uri, handler) {
    self.post(uri).handler(handler)
  }

  static Object param(RoutingContext self, String paramName) {
    return self.request().getParam(paramName)
  }

  static void sendJson(RoutingContext self, content) {
    self.response().putHeader("content-type", "application/json;charset=UTF-8").end(Json.encodePrettily(content))
  }

  static void sendText(RoutingContext self, String content) {
    self.response().putHeader("content-type", "text/plain;charset=UTF-8").end(content)
  }

  static void sendHtml(RoutingContext self, String content) {
    self.response().putHeader("content-type", "text/html;charset=UTF-8").end(content)
  }

  static Object bodyAsJson(RoutingContext self, klass) {
    return Json.decodeValue(self.getBodyAsString(), klass)
  }

}
