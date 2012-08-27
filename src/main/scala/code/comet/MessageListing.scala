package code.comet

import net.liftweb.http._
import net.liftweb.util.Helpers._
import code.lib._
import scala.xml.NodeSeq

class MessageListing extends CometActor with CometListener {
  
  override def registerWith = State
  
  lazy val getTogether = name flatMap { id =>
	val future = State !< FindGetTogether(id)
	future.get(1000).asA[Option[GetTogether]] flatMap { gt => gt }
  }
  
  def messages(gt: GetTogether) = {
    val future = State !< AllMessages(gt)
    future.get(1000).asA[List[Message]] openOr Nil
  }
  
  override def render = {
    ".listing" #> {
      getTogether map { gt =>
    	".message" #> {
    	  messages(gt) map { m =>
    	    ".message-from *" #> m.from &
    	    ".message-body *" #> m.body
          }
    	}
      } openOr { "*" #> <div class="error">No get together found</div> }
    }
  }
  
  override def lowPriority = {
    case MessagesChanged(gt) if getTogether === gt =>
      reRender()
  }
  
  

}