package SimpleNetflix

import org.eintr.loglady.Logging

/**
 * Created with IntelliJ IDEA.
 * User: Hector, Zhengzhong Liu
 * Date: 4/3/13
 * Time: 11:12 PM
 */
class MovieBasedPredictor extends Predictor with Logging {
  /**
   * Given a movie id, an user id, and the Parameter k for KNN
   * Return a list of closest profile wih weight and its prediction
   * Implementation based on movie profile similarity
   * @param mid Movie id to query
   * @param uid User id to query
   * @param k K for KNN
   * @return A list of tuple, first element is profile distance, second one is prediction
   */
  def getKNN(mid: Int, uid: Int, k: Int): List[(Double, Double)] = {
    val profilesForSimilarity = movieProfile
    val thisProfile = profilesForSimilarity(mid)   //profile of this movie id

    val knn = profilesForSimilarity.filter { //filter out movie profiles that this user didn't rate
      case (movieId, ratings) => {
        ratings.contains(uid) && movieId != mid
      }
    }.map {
      case (movieId, ratings) =>  //calculate profile similarity for each profile
        (MathUtils.dotProduct(thisProfile, ratings), movieProfile(movieId)(uid))  //store the pcc distance, and original rating
    }.toSeq.sortBy(_._1).reverse.take(k).toList  //get the best k

    //return result
    knn
  }
}