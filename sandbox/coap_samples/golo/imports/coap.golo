module coap

function TEXT_PLAIN = -> 0
function APPLICATION_JSON = -> 50

augment org.eclipse.californium.core.CoapClient {
  function getJsonData = |this| ->  this: get(APPLICATION_JSON())
  function postJsonData = |this, message| -> this: post(message, APPLICATION_JSON())
}

function coapClient = |server| {
  return org.eclipse.californium.core.CoapClient(server)
}


