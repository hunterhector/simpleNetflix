package SimpleNetflix

/**
 * Created with IntelliJ IDEA.
 * User: Hector, Zhengzhong Liu
 * Date: 4/3/13
 * Time: 8:43 PM
 */

/**
 * This is a class that can be regarded as the configuration
 */
object DocumentPaths {
  private val basePath = "data/download_sample/"
  val queries = basePath + "queries.txt"
  val titles = basePath + "movie_titles.txt"
  val data = basePath + "training_set"
  val movieBasedOutput = "data/output/movieBased.txt"
  val userBasedOutput = "data/output/userBased.txt"
  val movieNormalizedOutput = "data/output/normalized_movie.txt"
  val userNormalizedOutput = "data/output/normalized_user.txt"
  val customOutput = "data/output/custom.txt"
}
