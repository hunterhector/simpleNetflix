package SimpleNetflix

/**
 * Created with IntelliJ IDEA.
 * User: Hector, Zhengzhong Liu
 * Date: 4/3/13
 * Time: 11:12 PM
 */
class MovieBasedKnnPredictor extends KnnPredictor{
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
      case (movieId, ratings) =>  //calculate profile similarity for each profile (between movieId and mid)
        {
          val idTuple = if (movieId > mid) (mid,movieId) else (movieId,mid)
          val sim =
          if (ProfileRatingCache.movieRatings.contains(idTuple))
            ProfileRatingCache.movieRatings(idTuple)   //use the cached result
          else{
            val dotP = MathUtils.dotProduct(thisProfile, ratings) //cal the dotProd/cosine/pcc similarity otherwise
            ProfileRatingCache.movieRatings += (idTuple -> dotP)
            dotP
          } / (centeredMovieProfileNorm(mid)*centeredMovieProfileNorm(movieId))   //divide l2 to get cosine
          (sim, movieProfile(movieId)(uid))  //store also the original rating
        }
    }.toSeq.sortBy(_._1).reverse.take(k).toList  //get the best k

    //return result
    knn
  }
}