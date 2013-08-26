package models

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

import scala.io.Source

import laika.api.Transform
import laika.parse.markdown.Markdown
import java.io.{IOException, FileNotFoundException, File}

import java.text.SimpleDateFormat



case class Blog (title: String,
                 body: String,
                 slug: String,
                 createdOn: DateTime = DateTime.now()
                 )

object Blog  {

  lazy val allBlogs: List[Blog] = {
    getBlogsFromDir(new File("app/blogs"))
    .sortWith((b1, b2) => b1.createdOn.isAfter(b2.createdOn))
  }

  def getBlogsFromDir(f: File): List[Blog] = {
    val files = f.listFiles().toList
                .filter(_.getName.endsWith(".md"))

    files.flatMap(Blog.apply(_))
  }
  
  def bySlug(slug: String): Option[Blog] = {
    allBlogs.find(_.slug==slug)
  }


  def getPrevAndNext(slug: String) = {
    allBlogs.span(_.slug != slug) match {
      case (Nil,x::y::rest) => (None,Some(y))
      case (left,x::y::rest) => (Some(left.last),Some(y))
      case (left,x::rest) => (Some(left.last),None)
      case _ => (None,None)
    }
  }
  
  def renderContent(post: String): String = {
    Transform from Markdown to laika.render.HTML fromString post toString()
  }
  def parseDate(d: String): DateTime = {
    new DateTime(new SimpleDateFormat("dd/MM/yyyy").parse(d))
  }

  private val validSlug = "([-|\\w]+)".r
    def apply(f: File): Option[Blog] = {
     val fileName = f.getName()
     try {
       val source = io.Source.fromFile(f)
       val contents = source.mkString.split("---").toList
       source.close()
       try {
         contents match {
           case metadata :: post :: Nil=> {
             val data: List[String] = metadata.split("\n").toList
             data match {
               case title :: validSlug(slug) :: date :: Nil =>
                 Some(Blog(title, renderContent(post), slug, parseDate(date)))
               case _ => {
                 println("format error")
                 None
               }
             }
           }  
           case _ => {
             println("I failed to parse blog " + fileName)
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

     catch  {
       case _: FileNotFoundException => {
         println("No such file " + f.getName() )
         None
       }
       case ex: IOException => {
         println(ex.printStackTrace())
         None
       }
     }
   }
}

