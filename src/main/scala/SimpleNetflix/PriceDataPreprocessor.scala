package SimpleNetflix

import java.io.File
import scala.io.Source
import scala.collection.mutable
import org.eintr.loglady.Logging

/**
 * Created with IntelliJ IDEA.
 * User: Hector, Zhengzhong Liu
 * Date: 4/3/13
 * Time: 8:34 PM
 */
object PriceDataPreprocessor extends Logging {
  val dataFolder = new File(DocumentPaths.data)

  def getRatings() = {
    log.info("Reading dataset...")
    val userProfile = mutable.Map.empty[Int, Map[Int, Int]]

    val movieProfile = dataFolder.listFiles().map(file => {
      val lines = Source.fromFile(file).getLines().toList
      val mid = lines(0).trim.stripSuffix(":").toInt
      val ratings = lines.slice(1, lines.length).map(line => line.split(",")).filter(strs => strs.length == 3).map(strs => {
        val uid = strs(0).toInt
        val rating = strs(1).toInt
        if (userProfile.contains(uid))
          userProfile(uid) = userProfile(uid) + (mid -> rating)
        else userProfile += uid -> Map {
          mid -> rating
        }
        (uid, rating)
      }
      ).toMap

      (mid, ratings)
    }).toMap

    var aver = 0.0
    var totalRatings = 0

    movieProfile.foreach {
      case (mid, ratings) => {
        ratings.foreach {
          case (uid, rating) => {
            aver += rating
            totalRatings += 1
          }
        }
      }
    }

    aver = aver/totalRatings

    val centeredMovieProfile = movieProfile.map{case (mid,ratings) =>{
      val centeredRatings = ratings.map{ case (uid,rate)=>{
        (uid,rate-aver)
      }}
      (mid,centeredRatings)
    }}

    val centeredUserProfile = userProfile.map{case (uid,ratings) =>{
      val centeredRatings = ratings.map{ case (mid,rate)=>{
        (mid,rate-aver)
      }}
      (uid,centeredRatings)
    }}.toMap

    log.info("Done.")
    (centeredMovieProfile, centeredUserProfile, aver)
  }

  def main(args: Array[String]) {
    val (movieProfile, userProfile, averageRating) = getRatings()

    var num1Count = 0
    var num3Count = 0
    var num5Count = 0


    movieProfile.foreach {
      case (mid, ratings) => {
        ratings.foreach {
          case (uid, rating) => {
            if (rating == 1) num1Count += 1
            else if (rating == 3) num3Count += 1
            else if (rating == 5) num5Count += 1
          }
        }
      }
    }
    log.info("Number of movies: %d, Number of users: %d".format(movieProfile.size, userProfile.size))
    log.info("Number of rated 1 movies: %d, number of rated 3 movies: %s, number of rated 5 movies: %s".format(num1Count, num3Count, num5Count))

    log.info("Average rating: %f".format(averageRating))

     var aver = 0.0
     num1Count = 0
     num3Count = 0
     num5Count = 0
     var totalRatings = 0
    userProfile.get(1234576).foreach {
      case (ratings) => {
        ratings.foreach {
          case (mid, rating) => {
            aver += rating
            totalRatings += 1
            if (rating == 1) num1Count += 1
            else if (rating == 3) num3Count += 1
            else if (rating == 5) num5Count += 1
          }
        }
      }
    }
    aver = aver / totalRatings

    log.info("For user 1234576: ")
    log.info("Number of times he give '1': %d".format(num1Count))
    log.info("Number of times he give '3': %d".format(num3Count))
    log.info("Number of times he give '5': %d".format(num5Count))
    log.info("Average rating by him: %f".format(aver))

     aver = 0.0
     num1Count = 0
     num3Count = 0
     num5Count = 0
     totalRatings = 0


    movieProfile.get(4321).foreach {
      case (ratings) => {
        ratings.foreach {
          case (uid, rating) => {
            aver += rating
            totalRatings += 1
            if (rating == 1) num1Count += 1
            else if (rating == 3) num3Count += 1
            else if (rating == 5) num5Count += 1
          }
        }
      }
    }
    aver = aver / totalRatings
    log.info("For movie 4321: ")
    log.info("Number of times given '1':%d".format(num1Count))
    log.info("Number of times given '3':%d".format(num3Count))
    log.info("Number of times given '5':%d".format(num5Count))
    log.info("Average rating given: %f".format(aver))
  }
}
