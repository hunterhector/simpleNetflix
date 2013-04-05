package SimpleNetflix

/**
 * Created with IntelliJ IDEA.
 * User: Hector, Zhengzhong Liu
 * Date: 4/4/13
 * Time: 10:10 PM
 */
class NormalizedUserBasedKnnPredictor extends KnnPredictor{
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
    val profilesForSimilarity = userProfile
    val thisProfile = profilesForSimilarity(uid)   //profile of this movie id

    val knn = profilesForSimilarity.filter { //filter out movie profiles that this user didn't rate
      case (userId, ratings) => {
        ratings.contains(mid) && userId != uid
      }
    }.map {
      case (userId, ratings) =>  //calculate profile similarity for each profile (between movieId and mid)
      {
        val idTuple = if (userId > uid) (uid,userId) else (userId,uid)
        val sim =
          if (ProfileRatingCache.userRatings.contains(idTuple))
            ProfileRatingCache.userRatings(idTuple)   //use the cached result
          else{
            val dotP = MathUtils.dotProduct(thisProfile, ratings) //cal the dotProd/cosine/pcc similarity otherwise
            ProfileRatingCache.userRatings += (idTuple -> dotP)
            dotP
          } / (centeredUserProfileNorm(uid)*centeredUserProfileNorm(userId))
        (sim, userProfile(userId)(mid)-userAverage(userId) + userAverage(uid))  //store also the original rating
      }
    }.toSeq.sortBy(_._1).reverse.take(k).toList  //get the best k

    //return result
    knn
  }
}
