package SimpleNetflix

/**
 * Created with IntelliJ IDEA.
 * User: Hector
 * Date: 4/4/13
 * Time: 2:30 AM
 * To change this template use File | Settings | File Templates.
 */
class UserBasedKnnPredictor extends KnnPredictor{
  /**
   * Given a movie id, an user id, and the Parameter k for KNN
   * Return a list of closest profile wih weight and its prediction
   * Implemented based of user profile similarity
   * @param mid Movie id to query
   * @param uid User id to query
   * @param k K for KNN
   * @return A list of tuple, first element is profile distance, second one is prediction
   */
  protected def getKNN(mid: Int, uid: Int, k: Int): List[(Double, Double)] = {
    val profilesForSimilarity = userProfile
    val thisProfile = profilesForSimilarity(uid)  //profile of this user id

    val knn = profilesForSimilarity.filter { //first filter out users that did not rate this
      case (userId , ratings) => {
        ratings.contains(mid) && userId != uid
      }
    }.map {//calculate dot product between each profile with thisProfile
      case (userId, ratings) => {
        val idTuple = if (userId > uid) (uid,userId) else (userId,uid)
        val sim =
          if (ProfileRatingCache.userRatings.contains(idTuple))
            ProfileRatingCache.userRatings(idTuple)   //use the cached result
          else{
            val dotP = MathUtils.dotProduct(thisProfile, ratings) //cal the dotProd/cosine/pcc similarity otherwise
            ProfileRatingCache.userRatings += (idTuple -> dotP)
            dotP
          } / (centeredUserProfileNorm(uid)*centeredUserProfileNorm(userId))   //divide by norm to get cosine
        (sim, userProfile(userId)(mid)) //store the distance, and original user rating
      }
    }.toSeq.sortBy(_._1).reverse.take(k).toList  //sort by dot product and get the highest k

    //return result
    knn
  }
}