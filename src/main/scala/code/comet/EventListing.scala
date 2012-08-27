package code.comet

import net.liftweb.http._
import net.liftweb.util.Helpers._
import code.lib._
import scala.xml.NodeSeq

class EventListing extends CometActor with CometListener {
  
  override def registerWith = State
  
  lazy val getTogether = name flatMap { id =>
	val future = State !< FindGetTogether(id)
	future.get(1000).asA[Option[GetTogether]] flatMap { gt => gt }
  }
  
  def events(gt: GetTogether) = {
    val future = State !< AllEvents(gt)
    future.get(1000).asA[List[Event]] openOr Nil
  }
  
  override def render =  {
    ".listing" #> {
      getTogether map { gt =>
    	".event" #> {
	    	  events(gt) map { e =>
	    	    ".event-title *" #> e.title &
	    	    ".event-date *" #> State.dateFormat.format(e.date) & 
	    	    ".event-beer" #> {
	    	        e.beer match {
	    	          case true =>
	    	            "input [checked]" #> "checked"
	    	          case false => 
	    	            (html: NodeSeq) => html
	    	        }
	    	    } & 
	    	    ".event-pizza" #> {
	    	      e.pizza match {
	    	        case true =>
	    	          "input [checked]" #> "checked"
	    	        case false => 
	    	          (html: NodeSeq) => html
	    	      }
	    	    }
	          }
    	}
      } openOr { "*" #> <div class="error">No get together found</div> }
    }
  }
  
  override def lowPriority = {
    case EventsChanged(gt) if getTogether === gt =>
      reRender()
  }

}