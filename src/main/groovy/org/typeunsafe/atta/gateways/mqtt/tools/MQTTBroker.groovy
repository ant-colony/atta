package org.typeunsafe.atta.gateways.mqtt.tools

class MQTTBroker {
  String protocol
  String host
  Integer port

  MQTTBroker(String protocol, String host, Integer port) {
    this.protocol = protocol
    this.host = host
    this.port = port
  }

  MQTTBroker() {
  }

  String protocol() {
    return protocol
  }

  void protocol(String protocol) {
    this.protocol = protocol
  }

  String host() {
    return host
  }

  void host(String host) {
    this.host = host
  }

  Integer port() {
    return port
  }

  void port(Integer port) {
    this.port = port
  }

  String url () {
    //return "$protocol://$host:$port?clientId=$clientId"
    return "$protocol://$host:$port"
  }
}
