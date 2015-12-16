package org.typeunsafe.atta.core


class ComplexMessage implements Message {
  String from
  String to
  String content
  List<Object> contents = new LinkedList<Object>()

  def add (Object content) {
    this.contents.push(content)
  }

  def empty () {
    this.contents.clear()
  }

}
