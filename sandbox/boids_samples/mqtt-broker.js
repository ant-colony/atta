// MQTT Broker on Desktop
import mosca from 'mosca';

let mqttBroker = new mosca.Server({
  port: 1883
});

mqttBroker.on('clientConnected', (client) => {
  console.log('client connected', client.id);
});

// When a client subscribes to a topic
mqttBroker.on('subscribed', (topic, client) => {
  console.log('subscribed : ', topic, client.id);
});


mqttBroker.on('clientDisconnected', (client) => {
  console.log('clientDisconnected : ', client.id)
});

mqttBroker.on('ready', () => {
  process.stdout.write("\u001b[2J\u001b[0;0H");
  console.log('MQTT Broker is listening on 1883');
})

