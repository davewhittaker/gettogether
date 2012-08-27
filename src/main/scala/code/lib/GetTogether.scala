package code.lib

import net.liftweb.util.Helpers._

case class GetTogether(name: String, description: String) {

  lazy val id = name.toLowerCase.replaceAll("""\s+""","_") 
  
}