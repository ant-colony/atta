package groovy


import static org.typeunsafe.atta.core.Timer.after
import static org.typeunsafe.atta.core.Timer.every

every().seconds(1).run {
  println("Hello")
}

after().seconds(5).run {
  println("YO!!!")
}
