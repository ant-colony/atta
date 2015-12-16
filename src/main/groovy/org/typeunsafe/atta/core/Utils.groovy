package org.typeunsafe.atta.core

import io.vertx.core.Vertx

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class Utils {
  static Vertx vertx = Vertx.vertx()
  static ExecutorService executorService = Executors.newCachedThreadPool()

}
