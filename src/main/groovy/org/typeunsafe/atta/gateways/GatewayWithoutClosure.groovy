package org.typeunsafe.atta.gateways

/**
 * this interface is useful when using other language than Groovy
 * because closures implementation are different between Groovy and Java 8
 */
interface GatewayWithoutClosure {
  void onStart()
  void start()

  void onProvisioning()
  void provisioning()
}