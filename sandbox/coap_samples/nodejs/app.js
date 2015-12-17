import coap from 'coap';
import bl from 'bl';

setInterval(() => {

  let requestToCoapGateway001   = coap.request('coap://127.0.0.1:5683/home');

  requestToCoapGateway001.on('response', (res) => {
    res.pipe(bl((err, data) => {
      let json = JSON.parse(data);
      console.log(json);
    }));
  });
  requestToCoapGateway001.end();

}, 1000);

setInterval(() => {

  let requestToCoapGateway002   = coap.request('coap://127.0.0.1:5686/work');

  requestToCoapGateway002.on('response', (res) => {
    res.pipe(bl((err, data) => {
      let json = JSON.parse(data);
      console.log(json);
    }));
  });
  requestToCoapGateway002.end();

}, 1000);