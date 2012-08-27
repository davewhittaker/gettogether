package code.lib

import net.liftweb.actor.LiftActor
import scala.collection.immutable.TreeMap
import net.liftweb.http._
import java.text.SimpleDateFormat

object State extends LiftActor with ListenerManager {
  
  lazy val dateFormat = new SimpleDateFormat("MM/dd/yyyy")
  
  var getTogethers = TreeMap[String, GetTogether]()
  
  var events = Map[GetTogether, List[Event]]()
  
  var messages = Map[GetTogether, List[Message]]()
  
  override def lowPriority = {
    /*
     * Handle Get Togethers
     */
    case AllGetTogethers =>
      reply(getTogethers.values)
    case FindGetTogether(id) =>
      reply(getTogethers.get(id))
    case NewGetTogether(gt) =>
      getTogethers += (gt.id -> gt)
      updateListeners(GetTogethersChanged)
    case DeleteGetTogether(id) =>
      getTogethers get id map { getTogether =>
        events -= getTogether
        updateListeners(EventsChanged(getTogether))
        messages -= getTogether
        updateListeners(MessagesChanged(getTogether))
      }
      getTogethers -= id
      updateListeners(GetTogethersChanged)
    /*
     * Handle Events
     */
    case AllEvents(getTogether) =>
      reply(events.get(getTogether) getOrElse Nil)
    case NewEvent(event: Event) =>
      val existing = events.get(event.getTogether) getOrElse Nil
      val ordered = event :: existing sortWith { _.date.getTime > _.date.getTime }
      events += ((event.getTogether, ordered))
      updateListeners(EventsChanged(event.getTogether))
    case DeleteEvent(event: Event) =>
      val existing = events.get(event.getTogether) getOrElse Nil
      events += ((event.getTogether, existing filterNot { _ == event }))
      updateListeners(EventsChanged(event.getTogether))
    /*
     * Handle Messages
     */
    case AllMessages(getTogether) =>
      reply(messages.get(getTogether) getOrElse Nil)
    case NewMessage(message) =>
      val existing = messages.get(message.getTogether) getOrElse Nil
      messages += ((message.getTogether, message :: existing))
      updateListeners(MessagesChanged(message.getTogether))
  }
  
  override def createUpdate = Unit

}

/*
 * The messages this actor can handle
 */

case object AllGetTogethers

case class NewGetTogether(gt: GetTogether)

case class DeleteGetTogether(id: String)

case class FindGetTogether(id: String)

case object GetTogethersChanged

case class AllEvents(getTogether: GetTogether)

case class NewEvent(event: Event)

case class DeleteEvent(event: Event)

case class EventsChanged(getTogether: GetTogether)

case class AllMessages(getTogether: GetTogether)

case class NewMessage(message: Message)

case class MessagesChanged(getTogether: GetTogether)

