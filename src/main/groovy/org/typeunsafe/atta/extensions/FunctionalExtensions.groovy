package org.typeunsafe.atta.extensions
/**
 * Created by k33g_org on 10/01/16.
 * inspired https://github.com/yloiseau/golo-lang/blob/feat/error/src/main/golo/errors.golo
 */

class FunctionalExtensions {
  // Test if this optional is empty.
  static boolean isEmpty(Optional self) {
    return ! self.isPresent()
  }
  // Alias for `isEmpty`
  static boolean isNone(Optional self) {
    return ! self.isPresent()
  }
  // Alias for `isPresent`
  static boolean isSome(Optional self) {
    return ! self.isPresent()
  }
  // Test if this optional contains a value equals to the provided one.
  static boolean isSome(Optional self, value) {
    return self.ifPresent() && self.get().equals(value)
  }
  // ?
  static Iterator iterator(Optional self) {
    return self.isPresent() ? new LinkedList([self.get()]).iterator() : java.util.Collections.emptyIterator()
  }

  /*
  Reduce this option using `func` with `init` as initial value.

  For instance:

      Some("b"): reduce("a", |x, y| -> x + y) == "ab"
      None(): reduce(42, |x, y| -> x + y) == 42

  - *param* `init` the initial value
  - *param* `func` the aggregation function
  - *return* the initial value if this is empty, the aggregated result otherwise

   */
  static reduce(Optional self, initialValue, Closure aggreationFunction) {
    return self.isPresent() ? aggreationFunction(initialValue, self.get()) : initialValue
    // invoke (?)
  }

  static reduce(Optional self, reduceParams) {
    return self.isPresent() ? reduceParams.aggreationFunction(reduceParams.initialValue, self.get()) : reduceParams.initialValue
    // invoke (?)
  }


  static Object either(Optional self, Closure mapping, Closure ifEmpty) {
    return self.isPresent() ? mapping(self.get()) : ifEmpty()
  }


  static Object either(Optional self, closures) {
    return self.isPresent() ? closures.success(self.get()) : closures.failure()
  }


}

/*
  function either = |this, mapping, default| -> match {
    when this: isPresent() then mapping(this: get())
    otherwise default()
  }
 */