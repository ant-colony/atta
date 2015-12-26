package groovy

import org.typeunsafe.atta.gateways.VirtualGateway
import org.typeunsafe.atta.sensors.SimpleSensor

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

import static org.typeunsafe.atta.core.Timer.every

ExecutorService env = Executors.newCachedThreadPool()

def g = new VirtualGateway(id: "G", execEnv: env)
def g2 = new VirtualGateway(id: "G2")

def s1 = new SimpleSensor(id: "s1", value: 12)
def s2 = new SimpleSensor(id: "s2", value: 24)
def s3 = new SimpleSensor(id: "s3", value: 5)
def s4 = new SimpleSensor(id: "s4", value: 42)

g.sensors([s1, s2, s3])
g2.sensors([s4])

g.start { // this is a thread

  every(5).seconds().run {
    g.notifySensor(s1)
    g.notifySensor(s2)
    g.notifySensor(s3)
  }

}

g2.start({

  every(3).seconds().run {
    g2.notifySensor(s4)
  }

})


