package controllers

import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import play.api.libs.json._
import models.{BlogDAO, Blog}
import laika.api._
import utils.JSONParsing
import laika.parse.markdown.Markdown
import laika.parse.rst.ReStructuredText



object Application extends Controller {
  
  def index = Action {
    Ok(views.html.index())
  }

  def main(any: String) = Action {
    Ok(views.html.main())
  }

  def blogs = Action {
    Ok(views.html.blogs())
  }

  //todo - put this in a more sensible place
  implicit val blogWrites = new Writes[Blog] {
    def writes(b: Blog): JsValue = {
      Json.obj(
        "title" -> b.title,
        "body" -> b.body,
        "slug" -> b.slug
      )
    }
  }
  val testBlog = Blog(title="Why aren't there more women in tech?", body="<p>Today I got asked why aren't there more women opting to go into technology roles. My first answer was 'I don't know'. The work appeals to me, and I'm female. Clearly I'm not representative of all womankind, but it's likely the aspects of the role that gives me job satisfaction would be true for lots of other women.</p><p>But then I wasn't born with an innate desire to program and if you had asked me a few years ago whether I would consider a role in technology I would have said no. I feel fortunate that I have been able to overcome the barriers that made me think technology wouldn't be for me. Here are some of the reasons:</p><p><b>Little experience of programming:</b> I never had a reason to program, so I didn't. For a long time I worried whether this made me a less legitimate 'programmer'. I have often felt if I was genuinely a good software engineer, wouldn't have I been able to figure it out by myself? But really what you need to a desire to solve a problem and then you pick up the skills you need to solve it. For me, I care far more about solving a problem if I know the outcome affects others (be it my team, the company etc). Plus I'm a social learner. It's suited me to learn programming with other programmers, and not alone in my bedroom. Had I have not been offered the opportunity to learn on the job, I'm not certain I would have picked up the skills just off my own back. But now that I have a set of skills, I can apply my problem solving skills in this domain</p> <p><b>Perception of sitting alone at your desk all day:</b> I'm a fairly social worker, and I did worry that I might find the work isolating. The stereotype I had in my head was some guy sitting at his desk all day with his headphones on, with never a need to communicate. It is not like this. At all. Almost every day I collaborate with people in some form. Sometimes this is debugging with a fellow dev, or working out the spec with the product manager, making interface tweaks with UX, testing out new functionality of code with QA. It's not all talking, and I spend a lot of my time problem solving too. But there is so much more to being a developer than churning out code.</p><p><b>Male dominated culture:</b>It's sort of a feedback loop situation. As there are more men who opt into the field, 'tech' events as more naturally tailored to appeal to the demographic. This can make technology feel like it's only welcoming for men, but I believe this is more of a side effect than a cause. The only way this can change is for more women to take the plunge and join the force.</p><p>If the hurdles I have perceived are the same preventing other women from going for tech jobs, then barrier to entry can be solved with skills training and cultural changes with probably happen naturally as more women enter the field. Apparently if as many women opted for tech roles as men, then we'd solve the skills shortage too.</p>", createdOn=2L, slug="women-in-tech")
  def apiblogs = Action {
   val blogs = BlogDAO.list
    Ok(Json.obj("status" -> "OK", "data" -> blogs))
  }
  
  val blogForm = Form("post" -> text)

  def addNew = Action { request =>
    Ok(views.html.newBlog(blogForm))
  }

  def newPost = Action { implicit request =>
    val post = blogForm.bindFromRequest.get
    val result =Transform from Markdown to laika.render.HTML fromString post toString()
    Ok(result)
  }

}