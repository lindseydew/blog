package models

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat


import laika.api.Transform
import laika.parse.markdown.Markdown
import java.io.{IOException, FileNotFoundException, File}

import java.text.SimpleDateFormat
import io.{BufferedSource, Source}


case class Blog (title: String,
                 body: String,
                 slug: String,
                 createdOn: DateTime
                 )

object Blog  {

  lazy val allBlogs: List[Blog] = {
     getBlogsFromDir(new File("app/blogs"))
    .sortWith((b1, b2) => b1.createdOn.isAfter(b2.createdOn))
  }

  def getBlogsFromDir(f: File): List[Blog] = {
    val files: List[File] = f.listFiles().toList
    files.flatMap(f=> Blog.apply(io.Source.fromFile(f)))
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

