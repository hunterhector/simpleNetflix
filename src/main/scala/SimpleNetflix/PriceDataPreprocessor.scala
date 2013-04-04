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

/**
 * Class for training data preprocessing
 */
object PriceDataPreprocessor extends Logging {
  val dataFolder = new File(DocumentPaths.data)
  val ratingData = getRatings()

  /**
   * Class to get the movie profile and user profile
   * @return Movie profile, user profile, and the average used to center them
   */
  private def getRatings() = {
    log.info("Reading dataset...")
    val userProfile = mutable.Map.empty[Int, Map[Int, Double]]

    //build movie profile from file, user profile is built at the same ime
    val movieProfile = dataFolder.listFiles().map(file => {
      val lines = Source.fromFile(file).getLines().toList
      val mid = lines(0).trim.stripSuffix(":").toInt
      val ratings = lines.slice(1, lines.length).map(line => line.split(",")).filter(strs => strs.length == 3).map(strs => {
        val uid = strs(0).toInt
        val rating = strs(1).toDouble
        //build the user profile here
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

    //calculate the average of all ratings
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
    aver = aver / totalRatings

    //minus each rating by the average
    val centeredMovieProfile = movieProfile.map {
      case (mid, ratings) => {
        val centeredRatings = ratings.map {
          case (uid, rate) => {
            (uid, rate - aver)
          }
        }
        (mid, centeredRatings)
      }
    }

    //normalize to PCC
    var moviePccLength = 0.0
    val movieSelfCentered = centeredMovieProfile.map {
      case (mid, ratings) => {
        var ratingSum = 0.0
        ratings.foreach {
          case (uid, rate) => {
            ratingSum += rate
          }
        }
        //cal average for each vector
        val ratingAver = ratingSum / ratings.size
        val pccRatings = ratings.map {
          case (uid, rate) => {
            //center average on itself
            val selfCenter = rate - ratingAver
            moviePccLength += selfCenter * selfCenter
            (uid, selfCenter)
          }
        }
        (mid, pccRatings)
      }
    }

    //normalized by centered length
    val moviePcc = movieSelfCentered.map {
      case (mid, ratings) => {
        val pccRatings = ratings.map {
          case (uid, rate) => {
            (uid, rate / math.sqrt(moviePccLength))
          }
        }
        (mid, pccRatings)
      }
    }

    //minus each rating by the average
    val centeredUserProfile = userProfile.map {
      case (uid, ratings) => {
        val centeredRatings = ratings.map {
          case (mid, rate) => {
            (mid, rate - aver)
          }
        }
        (uid, centeredRatings)
      }
    }.toMap

    //normalize to pcc
    var userPCCLength = 0.0
    val userSelfCentered = centeredUserProfile.map {
      case (uid, ratings) => {
        //cal average for each vector
        var ratingSum = 0.0
        ratings.foreach {
          case (mid, rate) => {
            ratingSum += rate
          }
        }
        val ratingAver = ratingSum / ratings.size
        //center by self average
        val pccRatings = ratings.map {
          case (mid, rate) => {
            val selfCenter = rate - ratingAver
            userPCCLength += selfCenter * selfCenter
            (mid, selfCenter)
          }
        }
        (uid, pccRatings)
      }
    }

    //normalized by centered length
    val userPcc = userSelfCentered.map {
      case (uid, ratings) => {
        val pccRatings = ratings.map {
          case (mid, rate) => {
            (mid, rate /math.sqrt(userPCCLength))
          }
        }
        (uid, pccRatings)
      }
    }

    log.info("Done.")
    (centeredMovieProfile, centeredUserProfile, moviePcc, userPcc,movieSelfCentered,userSelfCentered, aver)
  }

  /**
   * The main class basically read the ratings and produce corpus statistics
   * @param args
   */
  def main(args: Array[String]) {
    val (movieProfile, userProfile, moviePcc, userPcc,movieSelfCentered,userSelfCentered, averageRating) = ratingData

    var aver = 0.0
    var num1Count = 0
    var num3Count = 0
    var num5Count = 0
    var totalRatings = 0

    movieProfile.foreach {
      case (mid, ratings) => {
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
    log.info("Number of movies: %d, Number of users: %d".format(movieProfile.size, userProfile.size))
    log.info("Number of rated 1 movies: %d, number of rated 3 movies: %s, number of rated 5 movies: %s".format(num1Count, num3Count, num5Count))

    log.info("Average rating: %f".format(aver))

    aver = 0.0
    num1Count = 0
    num3Count = 0
    num5Count = 0
    totalRatings = 0
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
