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
  val queries = basePath + "queries-small.txt"
  val titles = basePath + "movie_titles.txt"
  val data = basePath + "training_set"
  val movieBasedOutput = "data/sample_output/movieBased.txt"
  val userBasedOutput = "data/sample_output/userBased.txt"
  val normalizedOutput = "data/sample_output/normalized.txt"
  val customOutput = "data/sample_output/custom.txt"
}
