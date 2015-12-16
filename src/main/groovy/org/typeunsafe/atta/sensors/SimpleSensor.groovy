package org.typeunsafe.atta.sensors

import org.typeunsafe.atta.core.Message
import org.typeunsafe.atta.core.SimpleMessage
import org.typeunsafe.atta.gateways.Gateway
import groovy.json.JsonOutput
import org.typeunsafe.atta.sensors.core.coreProperties

import java.util.concurrent.ExecutorService

/**
 * Created by k33g_org on 20/11/15.
 * SimpleSensor send value only on demand
 */
class SimpleSensor implements Sensor, coreProperties {
    private Integer value = 0

    private Integer min = 0
    private Integer max = 100

    @Override
    void notifyGateway() { // notify observer
        this.gateway.update(new SimpleMessage(
                topic: "yo",
                from: id,
                content: JsonOutput.toJson(this.data())
        ))
    }
    // content: JsonOutput.toJson(["value": value(), "unit": unit()])
    @Override
    void update(Message message) {
        SimpleMessage msg = message
        if (msg.topic.equals("get_value")) {
            this.notifyGateway()
        }
    }

    @Override
    void generateData() {
        Random random = new Random()
        value = random.nextInt(max-min+1)+max
    }

    @Override
    void start() {
        println("$id is started with value: $value")
        execEnv.execute((Runnable){
            while (true) {
                this.generateData()
                Thread.sleep(1000)
            }

        })
    }

    @Override
    Object data() {
        return ["value": this.value, "unit": this.unit()]
    }


    Integer value() {
        return value
    }

    String unit() {
        return "no unit"
    }

    @Override
    String id() {
        return id
    }

    @Override
    void id(String id) {

    }

    @Override
    String topic() {
        return null
    }

    @Override
    void topic(String topic) {

    }

    @Override
    Integer delay() {
        return null
    }

    @Override
    void delay(Integer ms) {

    }

    @Override
    Gateway gateway() {
        return this.gateway
    }

    @Override
    SimpleSensor gateway(Gateway gateway) {
        this.gateway = gateway
        return this
    }

    @Override
    SimpleSensor execEnv(ExecutorService execEnv) {
        this.execEnv = execEnv
        return this
    }
}
