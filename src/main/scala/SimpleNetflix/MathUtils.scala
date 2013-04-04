package SimpleNetflix

/**
 * Created with IntelliJ IDEA.
 * User: Hector, Zhengzhong Liu
 * Date: 4/3/13
 * Time: 11:20 PM
 */
object MathUtils {
  /**
   * Calculate dot product using two map (representing sparse vector)
   * It can be applied to both user profile and movie profile
   * @param p1 profile 1
   * @param p2 profile 2
   * @return Dot product for the two profiles
   */
  def dotProduct(p1: Map[Int, Double], p2: Map[Int, Double]): Double = {
    p1.map {
      case (id, score) => {
        if (p2.contains(id))  p1(id) * p2(id) else  0.0
      }
    }.sum
  }
}
