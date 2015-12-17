#CoAP Gateway

##How to use

You can define a CoAP Gateway, with Groovy, like that:

```groovy
def coapGateway = new SimpleCoapGateway(id:"coapgw000", coapPort: 5686, locationName: "Work", path:"work")

//add some sensors and start

coapGateway.sensors([
  new DHTSensor(id:"dht1", locationName: "DESK1"),
  new LightSensor(id:"l1", locationName: "DESK1")
]).start {

  every().seconds(5).run {
    coapGateway.notifyAllSensors()
  }

}

```

see `sandbox/coap_sample/groovy/`


##You can query CoAP resource with NodeJS

```javascript
import coap from 'coap';
import bl from 'bl';

setInterval(() => {

  let requestToCoapGateway   = coap.request('coap://127.0.0.1:5686/work');

  requestToCoapGateway.on('response', (res) => {
    res.pipe(bl((err, data) => {
      let json = JSON.parse(data);
      console.log(json);
    }));
  });
  requestToCoapGateway.end();

}, 1000);
```

see `sandbox/coap_sample/nodejs/`


##You can query CoAP resource with Golo

```golo
module coapclient

import coap
import gololang.Async

function main = |args| {

  let request = |server, port, resource| -> promise(): initializeWithinThread(|resolve, reject| {

    try {
      let coapCli = coapClient(server+":"+port+"/"+resource)
      let response = coapCli: getJsonData(): getResponseText()
      resolve(response)
    } catch (err) {
        reject(err)
    }
  })

  request(server="coap://127.0.0.1", port=5686, resource="work")
    : onSet(|response| {
      println(response)
    })
    : onFail(|err| {
        println(err: getMessage())
    })

}
```

see `sandbox/coap_sample/golo/`

