package models

import myApp._
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

import Status._
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

  def renderContent(post: String): String = {
    Transform from Markdown to laika.render.HTML fromString post toString()
  }
  def parseDate(d: String): DateTime = {
    new DateTime(new SimpleDateFormat("dd/MM/yyyy").parse(d))
  }

  private val vaildSlug = "([-|\\w]+)".r
   //todo - exception handling
    def apply(f: File): Option[Blog] = {
     val fileName = f.getName()
     try {
       val source = io.Source.fromFile(f)
       val contents = source.getLines().toList
       source.close()
       try {
         contents match {
           case title :: vaildSlug(slug) :: created :: Nil => {
            println("no content for slug " + slug)
            None
           }
           case title :: vaildSlug(slug) :: created :: post => {
             Some(Blog(title, renderContent(post.mkString), slug, parseDate(created)))
           }
           case _ :: badSlug :: _ :: _ => {
             println("could not parse blog " + fileName + " because of bad slug " + badSlug)
             None
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

