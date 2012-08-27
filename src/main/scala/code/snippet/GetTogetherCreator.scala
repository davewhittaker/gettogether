package code.snippet

import net.liftweb.http._
import net.liftweb.util.Helpers._
import code.lib._
import net.liftweb.http.js.JsCmds

class GetTogetherCreator extends DispatchSnippet {
  
  override def dispatch = {
    case _ => {
      var name = ""
      var description = ""
      "#gt-name" #> SHtml.text("", name = _, "id" -> "gt-name") &
      "#gt-description" #> SHtml.textarea("", description = _, "id" -> "gt-description") &
      "button" #> SHtml.ajaxSubmit("Make it happen!",
        { () =>
        	val gt = GetTogether(name, description)
        	State ! NewGetTogether(gt)
        	JsCmds.Run("clearForm($('#new-get-together form'))")
        })
    }
  }

}