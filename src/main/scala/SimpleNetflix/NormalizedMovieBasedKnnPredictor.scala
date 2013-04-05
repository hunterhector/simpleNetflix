package SimpleNetflix

/**
 * Created with IntelliJ IDEA.
 * User: Hector
 * Date: 4/4/13
 * Time: 4:07 AM
 * To change this template use File | Settings | File Templates.
 */
class NormalizedMovieBasedKnnPredictor extends KnnPredictor{
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
          } / (centeredMovieProfileNorm(mid)*centeredMovieProfileNorm(movieId))  // divide to get cosine
        (sim, movieProfile(movieId)(uid)-movieAverage(movieId) + movieAverage(mid))  //do the normalization here
      }
    }.toSeq.sortBy(_._1).reverse.take(k).toList  //get the best k

    //return result
    knn
  }
}
