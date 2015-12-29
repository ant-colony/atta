import express from 'express';
import mqtt from 'mqtt';
import socketIo from 'socket.io';

let httpPort = 8080, socketPort = 5556,
    app = express(),
    mqttCli = mqtt.connect("mqtt://localhost:1883?clientId=sentinel"),
    io = socketIo.listen(socketPort);

// serving the webapp
app
  .use(express.static(__dirname + '/public'))
  .listen(httpPort);

// listening the webapp
io.sockets.on('connection', (socket) => {
  console.log("io.sockets connection ok on " + socketPort);

  // if something do something
  socket.on('something', function (data) {
    console.log("something", JSON.parse(data))
  })
});

// connect to mqtt broker and subscribe
mqttCli.on('connect', () => {
  mqttCli.subscribe('cows/+');

});

// if message from MQTT Broker, send data to webapp with socket
mqttCli.on('message', function(topic, message) {
  console.log(topic, ":", message.toString());

  var json = JSON.parse(message.toString());

  console.log(json)

  if(topic=="cows/move") {
    io.sockets.emit("move", json);
  }

});


console.log(" Listening on: " + httpPort);
