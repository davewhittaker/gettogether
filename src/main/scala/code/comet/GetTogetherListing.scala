package code.comet

import net.liftweb.http._
import net.liftweb.util.Helpers._
import code.lib._
import scala.xml.Text
import net.liftweb.http.js.JsCmds

class GetTogetherListing extends CometActor with CometListener {
  
  override def registerWith = State
  
  override def render = {
    ".get-together" #> {
	    val future = State !< AllGetTogethers
	    val getTogethers = future.get(1000).asA[Iterable[GetTogether]] openOr Nil
	    getTogethers map { gt => 
	      "button" #> SHtml.ajaxButton(Text("x"), 
	          { () =>
	          	State ! DeleteGetTogether(gt.id)
	          	JsCmds.Noop
	          }, "type" -> "button", "class" -> "close") &
	      "h5 *" #> <a href={"/with/" + gt.id}>{gt.name}</a> &
	      "p *" #> gt.description
	    }
    }
  }
  
  override def lowPriority = {
    case GetTogethersChanged =>
      reRender()
  }
  
}