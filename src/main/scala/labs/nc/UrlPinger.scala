package labs.nc

import java.io.{ByteArrayOutputStream, FileOutputStream, PrintStream}
import java.net.{HttpURLConnection, URI, URL}
import java.nio.charset.StandardCharsets
import java.util.{HashSet, Set}
import java.util.concurrent.CountDownLatch
import java.util.regex.Pattern

import scala.collection.JavaConversions._
import scala.collection.mutable.ArrayBuffer
import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.matching.Regex

import org.apache.commons.lang3.StringEscapeUtils
import org.bson.Document
import com.mongodb.MongoClient
import com.mongodb.client.{FindIterable, MongoCollection, MongoCursor, MongoDatabase}
//import org.codehaus.jackson.map.ObjectMapper
//import com.google.gson.GsonBuilder

case class RegExp(regExp: String)

case class HttpResponse(url: String, status: Int, data: Array[Byte], headers: Map[String, String]) {
  override def toString(): String = {
//    new ObjectMapper().writer().writeValueAsString(this)
//    new GsonBuilder().create().toJson(this)
//    s"{url:$url,status:$status,data:new String(data, StandardCharsets.UTF_8),header:$headers}"
    "{\"url\":\"" + url + "\",\"status\":" + status + ",\"data\":\"" + StringEscapeUtils.escapeJava(new String(data)) + "\",\"header\":\"" + StringEscapeUtils.escapeJava(headers.toString()) + "\"}"
        
//    "{\"url\":\"" + url + "\",\"status\":" + status + ",\"header\":\"" + headers + "\"}"
  }
}

class Helper {
  private val MONGO: MongoClient = new MongoClient("localhost", 27017)
	private val DB: MongoDatabase = MONGO.getDatabase("nc")
	val COLL_URL: MongoCollection[Document] = DB.getCollection("URL")
	
  /*
  * Saves an arbitrary object in a NoSQL database
  */
  def save(obj: Object) = {
    val res: HttpResponse = obj.asInstanceOf[HttpResponse]
    val dbObject: Document = Document.parse(res.toString())
    val key: Document = new Document("url", dbObject.get("url"))
    var record: Document = null
  	val iterable: FindIterable[Document] = COLL_URL.find(key)
    val cursorDoc: MongoCursor[Document] = iterable.iterator()
    if (cursorDoc.hasNext()) {
    	record = cursorDoc.next();
    }
    if (record == null) {
      COLL_URL.insertOne(dbObject) 
    } else {
    	COLL_URL.deleteOne(key);
    	COLL_URL.insertOne(dbObject);
    }
    println(obj)
  }
  /*
  * Find all objects whose attibute @attr matches the regular
  * expression @what
  */
  def find(attr: String, what: RegExp): Set[String] = {
    var result: Set[String] = new HashSet[String]()
    val regex: Pattern = Pattern.compile(what.regExp)
    //("(?i)" + what.regExp).r
  	val iterable: FindIterable[Document] = COLL_URL.find(new Document(attr, new Document("$regex", regex)))
    val cursorDoc: MongoCursor[Document] = iterable.iterator()
    while (cursorDoc.hasNext()) {
      val doc = cursorDoc.next()
//      println(doc)
    	result.add(doc.getString("url"))
    }
  	result
  }
}
 
object UrlPinger {
 
  def main(args: Array[String]) = {
    if (args.length == 3 && args.head.startsWith("-s")) {//db.URL.find({"header": {"$regex": "X-cache", "$options": "i"}});
      val db: Helper = new Helper()
      println(args(0) + "," + args(1) + "," + args(2))
      val result = db.find(args(1), RegExp(args(2)))
      println(result)
    } else {
//      for (arg <- args) {
//        println(arg)
//      }
      new UrlPinger(args).crawl
    }
  }
 
}
 
/**
 * @param startPage crawler would crawl from that page
 * @param filter crawler just crawl those url which match the filter
 * @param onComplete handler for download complete
 */
class UrlPinger(urls: Array[String]) {
  private val latch = new CountDownLatch(1)
  
  val db: Helper = new Helper()
 
  def load(res: HttpResponse) = {
    val uri = URI.create(res.url)
    if ("test.com".equals(uri.getHost)) {
      println(res.url + "\n" + res.status + "\n" + res.headers.toString())
    //println(new String(data, StandardCharsets.UTF_8))
    } else {
      db.save(res)
    }
  }
  
  def crawl {
    urls.map {
      url =>
        val future = Future(get(url))
        future.onSuccess {
          case (url: String, status: Int) =>
            println(s"downloaded $url")
        }
        future.onFailure {
          case e: Exception =>
            println(s"visit $url error!")
            e.printStackTrace
        }
    }
    latch.await()
  }

  def get(url: String) = {
    val uri = new URL(url);
    val conn = uri.openConnection().asInstanceOf[HttpURLConnection];
    conn.setConnectTimeout(100000)
    conn.setReadTimeout(1000000)
    val stream = conn.getInputStream()
    val buf = Array.fill[Byte](1024)(0)
    var len = stream.read(buf)
    val out = new ByteArrayOutputStream
    while (len > -1) {
      out.write(buf, 0, len)
      len = stream.read(buf)
    }
 
    val data = out.toByteArray()
    val status = conn.getResponseCode()
 
    val headers = conn.getHeaderFields().toMap.map {
      head => (head._1, head._2.mkString(","))
    }
    conn.disconnect
    load(HttpResponse(url, status, data, headers))
    (url, conn.getResponseCode())
  }

} 
