package SimpleNetflix

/**
 * Created with IntelliJ IDEA.
 * User: Hector, Zhengzhong Liu
 * Date: 4/3/13
 * Time: 10:25 PM
 */
trait KnnPredictor extends Predictor{
  /**
   * The abstract KNN method
   * Given a movie id, an user id, and the Parameter k for KNN
   * Return a list of closest profile wih weight and its prediction
   * @param mid Movie id to query
   * @param uid User id to query
   * @param k K for KNN
   * @return A list of tuple, first element is profile distance, second one is prediction
   */
  protected def getKNN(mid: Int, uid: Int, k: Int): List[(Double, Double)]

  /**
   * A private method that use the KNN result to calculate the prediction
   * Possiblely be override by subclasses
   * @param scoresWithWeights Weight, and profile rating given by KNN
   * @param k k in the k best user
   * @return  A score for this query
   */
  protected def getPrediction(scoresWithWeights: List[(Double, Double)], k: Int): Double = {
    //val actualNeighbourNumber = scoresWithWeights.length
    val weightedScore = scoresWithWeights.map {
      case (weight, score) => score  //currently did not deploy weighting
    }.sum

    weightedScore / k + averageRating
  }

  /**
   * Just a wrapper to be called to get the result
   * @param mid Movie id
   * @param uid User id
   * @param k k in k best neighbour
   * @return  Score
   */
  def doQuery(mid: Int, uid: Int, k: Int): Double = {
    getPrediction(getKNN(mid, uid, k), k)
  }
}