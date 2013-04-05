package SimpleNetflix

import org.eintr.loglady.Logging

/**
 * Created with IntelliJ IDEA.
 * User: Hector, Zhengzhong Liu
 * Date: 4/4/13
 * Time: 9:20 PM
 */
trait Predictor extends Logging{
  //Get all the profiles from the Preprocessor
  val (movieProfile, userProfile,centeredMovieProfileNorm,centeredUserProfileNorm, movieAverage,userAverage,averageRating) = PriceDataPreprocessor.ratingData
  /**
   * Just a wrapper to be called to get the result
   * @param mid Movie id
   * @param uid User id
   * @param k k in k best neighbour
   * @return  Score
   */
  def doQuery(mid: Int, uid: Int, k: Int): Double

}
