package SimpleNetflix

import scala.io.Source
import scala.collection.mutable.ListBuffer
import org.eintr.loglady.Logging

/**
 * Created with IntelliJ IDEA.
 * User: Hector, Zhengzhong Liu
 * Date: 4/3/13
 * Time: 10:25 PM
 */
object QueryReader extends Logging{

  log.info("Reading quereis")
  private var mid = -1
  val queries = Source.fromFile(DocumentPaths.queries).getLines().foldLeft(ListBuffer[(Int, Int)]())((queries, line) => {
    val l = line.trim
    if (l.endsWith(":")) {
      mid = l.stripSuffix(":").toInt
      queries
    } else {
      val uid = l.toInt
      queries.append((mid, uid))
      queries
    }
  }).toList

  log.info("%d queries read ".format(queries.length))
}
