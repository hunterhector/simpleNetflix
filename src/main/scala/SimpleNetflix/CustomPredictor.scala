package SimpleNetflix

/**
 * Created with IntelliJ IDEA.
 * User: Hector, Zhengzhong Liu
 * Date: 4/4/13
 * Time: 9:09 PM
 */

/**
 * This method do an interpolation of the 2 values
 * 1. The user based output
 * 2. The normalized movie based output
 * Trying to find a good combination of the merit of both methods
 */
class CustomPredictor extends Predictor {

  val moviePred = new NormalizedMovieBasedKnnPredictor
  val userPred = new NormalizedUserBasedKnnPredictor


  /**
   * Just a wrapper to be called to get the result
   * @param mid Movie id
   * @param uid User id
   * @param k k in k best neighbour
   * @return  Score
   */
  def doQuery(mid: Int, uid: Int, k: Int): Double = {
    val lambda = 0.6      //iterpolation variable choice
    lambda * moviePred.doQuery(mid,uid,k) + (1-lambda)*userPred.doQuery(mid,uid,k)
  }
}
