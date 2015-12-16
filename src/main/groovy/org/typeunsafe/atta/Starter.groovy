package org.typeunsafe.atta

import io.vertx.groovy.ext.web.Router
import io.vertx.groovy.ext.web.handler.BodyHandler
import org.typeunsafe.atta.sandbox.Human

def server = vertx.createHttpServer()
def router = Router.router(vertx)

/*
  enable the reading of the request body for all routes (globally)
 */
router.route().handler(BodyHandler.create())

router.get("/api/yo",{ context ->
  context.response().putHeader("content-type", "text/html").end("<h1>YO!</h1>")
  //todo: send html
})

router.get("/api/hi/:name", { context ->

  context.sendJson([
      "message":"Hi!",
      "name": context.param("name").toString()
  ])

})

router.post("/api/humans", { context ->

  def obj = context.bodyAsJson(Object.class)
  obj.id = new Random().nextInt(100)
  context.sendJson(obj)

})


router.post("/api/2/humans", { context ->

  Human bob = context.bodyAsJson(Human.class)
  bob.id = new Random().nextInt(100)
  context.sendJson(bob)
})

server.start(router, 8080, "/*")