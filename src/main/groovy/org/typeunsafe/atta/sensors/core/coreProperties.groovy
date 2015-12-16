package org.typeunsafe.atta.sensors.core

import org.typeunsafe.atta.gateways.Gateway

import java.util.concurrent.ExecutorService

trait coreProperties {
    Gateway gateway = null
    String id = null
    ExecutorService execEnv = null

}