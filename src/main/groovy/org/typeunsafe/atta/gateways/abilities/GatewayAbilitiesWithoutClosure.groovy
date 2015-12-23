package org.typeunsafe.atta.gateways.abilities

import org.typeunsafe.atta.gateways.GatewayWithoutClosure

/**
 * this traits is useful when using other language than Groovy
 * because closures implementation are different between Groovy and Java 8
 */
trait gatewayAbilitiesWithoutClosure implements GatewayWithoutClosure {
  /*--- useful for golo ---*/
  void onStart() {

  }

  void start() {
    this.checkExecutorService();

    execEnv.execute((Runnable){
      println("starting sensors ...");

      sensors.each { sensor ->
        sensor.start()
      }

      println("starting gateway ...");
      //println("MQTT Gateway $id is started and emitting/listening on $broker.port");
      println("$kind Gateway $id is started and emitting/listening");

      this.initializeBeforeWork()

      //when all is ok, do the job
      this.onStart()
    })

  }

  void onProvisioning() {

  }

  void provisioning() {

  }
/*--- ---*/
}