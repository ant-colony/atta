package org.typeunsafe.atta.functional

/**
 * Created by k33g_org on 10/01/16.
 * inspired by https://github.com/yloiseau/golo-lang/blob/feat/error/src/main/golo/errors.golo
 */
class Errors {
  static Optional None() {
    return Optional.empty()
  }

  static Optional Some(value) {
    return Optional.of(value)
  }

  static Optional Option(value) {
    return value instanceof Optional ? value : Optional.ofNullable(value)
  }

  static Optional optionDecorator(Closure something) {
    try {
      return Some(something())
    } catch(Exception e) {
      return None()
    }
  }

}
