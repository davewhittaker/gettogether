package code.snippet

import net.liftweb.http._
import net.liftweb.util.Helpers._
import code.lib._
import net.liftweb.http.js.JsCmds
import java.text.SimpleDateFormat
import java.util.Calendar
import scala.xml.Text

class GetTogetherView(getTogether: GetTogether) extends DispatchSnippet {
  
  override def dispatch = {
    case "title" => title
    case "messages" => messages
    case "events" => events
  }
  
  def title = "* *" #> getTogether.name
  
  def messages = {
    var from = ""
    var body = ""
    "#mform-from" #> SHtml.text(from, from = _) &
    "#mform-body" #> SHtml.textarea(body, body = _) &
    "button" #> SHtml.ajaxSubmit("Post Message", { () =>
      	val message = Message(getTogether, from, body)
      	State ! NewMessage(message)
      	JsCmds.Run("clearForm($('#messages form'))")
      }, "class" -> "btn") &
    ".listing [data-lift+]" #> Text("?name=" + getTogether.id)
  }
  
  def events = {
    var title = ""
    var date = Calendar.getInstance().getTime()
    var beer = true
    var pizza = true
    "#eform-title" #> SHtml.text(title, title = _) &
    "#eform-date" #> SHtml.text(State.dateFormat.format(date), { d =>
      	try {
      	  date = State.dateFormat.parse(d)
      	} catch {
      	  case e => e.printStackTrace()
      	}
      }) &
    "#eform-beer" #> {
      "input" #> SHtml.checkbox(beer, beer = _)
    } &
    "#eform-pizza" #> {
      "input" #> SHtml.checkbox(pizza, pizza = _) 
    } &
    "button" #> SHtml.ajaxSubmit("New Event", { () =>
      	val event = Event(getTogether, title, date, beer, pizza)
      	State ! NewEvent(event)
      	JsCmds.Run("clearForm($('#events form'))")
      }, "class" -> "btn") &
    ".listing [data-lift+]" #> Text("?name=" + getTogether.id)
  }

}