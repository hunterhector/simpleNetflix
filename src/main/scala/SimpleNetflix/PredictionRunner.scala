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
    val predictorAndOutput: List[(FileWriter, Predictor)] = List(movieMovie(), userUser(),normalized())
//    val predictorAndOutput: List[(FileWriter, Predictor)] = List(movieMovie(), userUser(), normalized(), custom())
    val queries = QueryReader.queries

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
          val pred = p.doQuery(mid, uid, 10)

          count += 1
          if (count % 100 == 0)
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
    val p = new UserBasedPredictor

    (output, p)
  }

  def movieMovie() = {
    val outputFile = new File(DocumentPaths.movieBasedOutput)
    outputFile.createNewFile()
    val output = new FileWriter(outputFile)
    val p = new MovieBasedPredictor

    (output,p)
  }

  def normalized() = {
    val outputFile = new File(DocumentPaths.normalizedOutput)
    outputFile.createNewFile()
    val output = new FileWriter(outputFile)
    val p = new NormalizedMovieBasedPredictor

    (output,p)
  }

  def custom() = {

  }
}