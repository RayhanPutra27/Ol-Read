package rpl.ezy.olread.response

import rpl.ezy.olread.model.MUser
import java.io.Serializable

class ResponseUsers(
    var status : Int,
    var message : String,
    var data : ArrayList<MUser>
): Serializable