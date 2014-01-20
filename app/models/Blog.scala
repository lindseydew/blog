package models

import org.joda.time.DateTime


import laika.api.Transform
import laika.parse.markdown.Markdown
import java.io.File

import java.text.SimpleDateFormat
import io.{BufferedSource, Source}
import java.net.URL
import play.api.Play
import play.api.Play.current


case class Blog (title: String,
                 body: String,
                 slug: String,
                 createdOn: DateTime )

case class Navigation(prev: Option[Blog], next: Option[Blog])

object Blog  {

  lazy val allBlogs: List[Blog] = {
     val url = Play.getFile("resources/blogs")
     getBlogsFromDir(new File(url.toURI))
    .sortWith((b1, b2) => b1.createdOn.isAfter(b2.createdOn))
  }

  def getBlogsFromDir(dir: File): List[Blog] = {
    val files = Option(dir.listFiles())
    files match {
      case Some(fs) => {
        (for(f <- fs;
             contents = io.Source.fromFile(f);
             blog <- Blog.apply(contents)
        ) yield blog).toList
      }
      case None => Nil
    }
  }
  
  def bySlug(slug: String): Option[Blog] = {
    allBlogs.find(_.slug==slug)
  }


  def getPrevAndNext(slug: String): Navigation = {
    allBlogs.span(_.slug != slug) match {
      case (Nil,x::y::rest) => Navigation(None,Some(y))
      case (left,x::y::rest) => Navigation(Some(left.last),Some(y))
      case (left,x::rest) => Navigation(Some(left.last),None)
      case _ => Navigation(None,None)
    }
  }
  
  def renderContent(post: String): String = {
    Transform from Markdown to laika.render.HTML fromString post toString()
  }
  def parseDate(d: String): DateTime = {
    new DateTime(new SimpleDateFormat("dd/MM/yyyy").parse(d))
  }

  private val validSlug = "([-|\\w]+)".r
  private val validDate = "([0-3][0-9]/[0-1][0-9]/[2][0][0-9][0-9])".r
    def apply(contents: BufferedSource): Option[Blog] = {
     try {
       contents.mkString.split("---").toList match {
         case metadata :: post :: Nil=> {
           val data: List[String] = metadata.split("\n").toList
           data match {
             case title :: validSlug(slug) :: validDate(date) :: Nil =>
               Some(Blog(title, renderContent(post), slug, parseDate(date)))
             case _ :: badSlug :: validDate(date) :: Nil => {
               println("could not parse blog due to bad slug " + badSlug)
               None
             }
             case _ :: slug :: badDate :: Nil =>
              println("could not parse blog due to bad date " + badDate + " with slug " + slug)
              None
           }
         }
         case _ => {
           println("Structure of the post not in the correct format ")
           None
         }
       }
     }
     catch{
       case ex: Throwable =>{
         println(ex.printStackTrace())
         None
       }
     }
   }


}

