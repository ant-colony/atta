package groovy
import org.typeunsafe.atta.gateways.coap.CoapGateway
import org.typeunsafe.atta.sensors.DHTSensor
import org.typeunsafe.atta.sensors.LightSensor
import org.typeunsafe.atta.sensors.SoundSensor

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

import static org.typeunsafe.atta.core.Timer.every

ExecutorService env = Executors.newCachedThreadPool()

def coapGateway001 = new CoapGateway(id:"coapgw001", coapPort: 5683, execEnv: env, locationName: "Home") // each gateway has a unique port
def coapGateway002 = new CoapGateway(id:"coapgw002", coapPort: 5686, locationName: "Work")

coapGateway001.sensors([
  new DHTSensor(id:"dhtRoom1", locationName: "ROOM1"),
  new DHTSensor(id:"dhtRoom2", locationName: "ROOM2"),
  new LightSensor(id:"lightRoom9A", locationName: "ROOM9"),
  new LightSensor(id:"lightRoom9B", locationName: "ROOM9")
]).start {

  every().seconds(5).run {
    coapGateway001.notifyAllSensors()
  }

}

coapGateway002.sensors([
  new DHTSensor(id:"dhtRoom3",locationName: "OFFICE01"),
  new DHTSensor(id:"dhtRoom4",locationName: "OFFICE02"),
  new DHTSensor(id:"dhtRoom5",locationName: "OFFICE03"),
  new SoundSensor(id:"soundRoom6",locationName: "OFFICE02"),
  new DHTSensor(id:"dhtRoom7",locationName: "OFFICE04"),
  new DHTSensor(id:"dhtRoom8",locationName: "OFFICE05"),
  new SoundSensor(id:"soundRoom9",locationName: "OFFICE03"),
  new DHTSensor(id:"dhtRoom10",locationName: "OFFICE06")
])
.start {

  every().seconds(5).run {
    coapGateway002.notifyAllSensors() // I want all data of my sensors each 5s
  }

}

