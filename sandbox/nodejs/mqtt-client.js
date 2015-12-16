import mqtt from 'mqtt';

import readline from 'readline';

let rl = readline.createInterface({
  input: process.stdin,
  output: process.stdout
});

let client = mqtt.connect("mqtt://k33g-orgs-macbook-pro.local:1883?clientId=skynet");

client.on("connect", () => {
  client.subscribe("informations/+");
  client.subscribe("home/+");

  console.log("Skynet client is Listening ...");

  let recursiveQuestion = () => {
    rl.question(">>> ", (answer) => {

      // publish on topic
      client.publish(
        'bob/commands',
        JSON.stringify({
          cmd:answer
        })
      );
      recursiveQuestion();
    });
  }

  recursiveQuestion();

});

client.on('message', (topic, message) => {
  console.log(topic, ":", message.toString());

  let json = JSON.parse(message.toString());

});
