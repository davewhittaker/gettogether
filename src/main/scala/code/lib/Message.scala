package code.lib

import java.util.Calendar
import java.util.Date

case class Message(
    getTogether: GetTogether, 
    from: String, 
    body: String, 
    date: Date = Calendar.getInstance().getTime()) {

}