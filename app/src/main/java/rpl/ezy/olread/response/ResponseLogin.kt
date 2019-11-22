package rpl.ezy.olread.response

import rpl.ezy.olread.model.MUser
import java.io.Serializable

class ResponseLogin(
    var status : Int,
    var message : String,
    var data : MUser
): Serializable