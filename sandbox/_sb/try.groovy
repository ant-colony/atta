package _sb

import static org.typeunsafe.atta.functional.Errors.None;
import static org.typeunsafe.atta.functional.Errors.Some;
import static org.typeunsafe.atta.functional.Errors.optionDecorator

def Optional toInt(sParam) {
  try {
    return Some(Integer.parseInt(sParam.trim()))
  } catch(Exception e) {
    return None()
  }
}

def Optional toIntBis(sParam) {
  return optionDecorator({
    return Integer.parseInt(sParam.trim())
  })
}


// --- Always return 42 ---

Integer res1 = toInt("hello").either(success: { value->
  println("You win ${value}")
  return (Integer) value
}, failure: {
  println("Huston?!")
  return 42
})


Integer res2 = toInt("42").either(success: {value->
  println("You win ${value}")
  return value
},failure: {
  println("Huston?!")
  return 42
})

println(res1.equals(res2))


Integer res3 = toIntBis("hello").either(success: { value->
  println("You win ${value}")
  return value
}, failure: {
  println("Huston?!")
  return 42
})


Integer res4 = toIntBis("42").either(success: {value->
  println("You win ${value}")
  return value
},failure: {
  println("Huston?!")
  return 42
})

println(res3.equals(res4))