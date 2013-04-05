package SimpleNetflix

import org.eintr.loglady.Logging
import java.io.{File, FileWriter}

/**
 * Created with IntelliJ IDEA.
 * User: Hector, Zhengzhong Liu
 * Date: 4/3/13
 * Time: 11:50 PM
 */

/**
 * This class basically run the system and output results
 */
object PredictionRunner extends Logging {
  def main(args: Array[String]) {
    // call the four methods to get four different output writer and 4 predictor
    val predictorAndOutput: List[(FileWriter, Predictor)] = List(userUser(), movieNormalized(), custom())
    //get the queries
    val queries = QueryReader.queries

    //run experiments and get outputs
    predictorAndOutput.foreach {
      case (output, p) => {
        log.info("Running experiment with %s".format(p.getClass.getName))
        var currentMid = -1
        var count = 0
        queries.foreach(q => {
          val mid = q._1
          val uid = q._2
          if (currentMid != mid) {
            output.write(mid + ":\n")
            currentMid = mid
          }
          val pred = p.doQuery(mid, uid, 11)

          count += 1
          if (count % 1000 == 0)
            log.info("%d queries done.", count)
          output.write(pred + "\n")
        })
        output.close()
      }
    }
  }

  def userUser() = {
    val outputFile = new File(DocumentPaths.userBasedOutput)
    outputFile.createNewFile()
    val output = new FileWriter(outputFile)
    val p = new UserBasedKnnPredictor

    (output, p)
  }

  def movieMovie() = {
    val outputFile = new File(DocumentPaths.movieBasedOutput)
    outputFile.createNewFile()
    val output = new FileWriter(outputFile)
    val p = new MovieBasedKnnPredictor

    (output,p)
  }

  def movieNormalized() = {
    val outputFile = new File(DocumentPaths.movieNormalizedOutput)
    outputFile.createNewFile()
    val output = new FileWriter(outputFile)
    val p = new NormalizedMovieBasedKnnPredictor

    (output,p)
  }

  def userNormalized() = {
    val outputFile = new File(DocumentPaths.userNormalizedOutput)
    outputFile.createNewFile()
    val output = new FileWriter(outputFile)
    val p = new NormalizedUserBasedKnnPredictor

    (output,p)
  }

  def custom() = {
    val outputFile = new File(DocumentPaths.customOutput)
    outputFile.createNewFile()
    val output = new FileWriter(outputFile)
    val p = new CustomPredictor

    (output,p)
  }
}