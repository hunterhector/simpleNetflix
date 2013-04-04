package SimpleNetflix

/**
 * Created with IntelliJ IDEA.
 * User: Hector, Zhengzhong Liu
 * Date: 4/3/13
 * Time: 10:25 PM
 */
trait Predictor {
  val (movieProfile,userProfile,averageRating) = PriceDataPreprocessor.getRatings()

  def getKNN(mid:Int, uid:Int, k:Int) : List[(Double,Double)]

  def getPrediction(scoresWithWeights:List[(Double,Double)],k:Int):Double = {
    scoresWithWeights.map{case (weight,score)=>score}.sum/k + averageRating
  }

  def doQuery(mid:Int,uid:Int,k:Int):Double = {
    getPrediction(getKNN(mid,uid,k),k)
  }
}
