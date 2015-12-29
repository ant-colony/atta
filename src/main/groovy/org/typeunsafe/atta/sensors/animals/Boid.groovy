package org.typeunsafe.atta.sensors.animals

/**
 * Created by k33g_org on 28/12/15.
 */
interface Boid {
  void move()
  Double distance(Boid boid)
  Double x()
  Double y()

  void moveAway(Double minDistance)
  void moveCloser(Double distance)
  void moveWith(Double distance)

  Double xVelocity()
  Double yVelocity()

}