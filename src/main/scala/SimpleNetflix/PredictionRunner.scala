package SimpleNetflix

import org.eintr.loglady.Logging
import java.io.{File, FileWriter}

/**
 * Created with IntelliJ IDEA.
 * User: Hector, Zhengzhong Liu
 * Date: 4/3/13
 * Time: 11:50 PM
 */
object PredictionRunner extends Logging {
  def main(args: Array[String]) {

    val outputFile = new File(DocumentPaths.outputPath)
    outputFile.createNewFile()
    val output = new FileWriter(outputFile)
    val p: Predictor = new MovieBasedPredictor
    val queries = QueryReader.queries

    var currentMid = -1
    var count = 0
    queries.foreach(q => {
      val mid = q._1
      val uid = q._2
      if (currentMid != mid) {
        output.write(mid + ":\n")
        currentMid = mid
      }
      val pred = p.doQuery(mid, uid, 3)

      count += 1
      if (count % 100 == 0)
        log.info("%d queries done.", count)
      //log.info("For query [Movie: %d User: %d], prediction is %f".format(q._1,q._2,pred))
      output.write(pred + "\n")
    })

    output.close()
  }
}