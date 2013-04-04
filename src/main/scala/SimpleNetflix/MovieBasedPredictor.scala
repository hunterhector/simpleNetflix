package SimpleNetflix

import org.eintr.loglady.Logging

/**
 * Created with IntelliJ IDEA.
 * User: Hector, Zhengzhong Liu
 * Date: 4/3/13
 * Time: 11:12 PM
 */
class MovieBasedPredictor extends Predictor with Logging {
  def getKNN(mid: Int, uid: Int, k: Int): List[(Double, Double)] = {
    //log.debug("Finding knn by Movie for mid: %d, uid: %d, k is %d".format(mid, uid, k))
    val thisProfile = movieProfile(mid)

    val knn = movieProfile.filter {
      case (movieId, ratings) => {
        ratings.contains(uid) && movieId != mid
      }
    }.map {
      case (movieId, ratings) =>
        (MathUtils.dotProduct(thisProfile, ratings), ratings(uid))
    }.toSeq.sortBy(_._1).reverse.take(k).toList

    knn
  }
}
