package SimpleNetflix

import scala.collection.mutable
/**
 * Created with IntelliJ IDEA.
 * User: Hector, Zhengzhong Liu
 * Date: 4/4/13
 * Time: 7:36 PM
 */

/**
 * Simple class used to cache the result calculated to avoid duplicate computations
 */
object ProfileRatingCache {
      val movieRatings:mutable.HashMap[(Int,Int),Double] = new mutable.HashMap[(Int,Int),Double]()
      val userRatings:mutable.HashMap[(Int,Int),Double] = new mutable.HashMap[(Int,Int),Double]()
}
