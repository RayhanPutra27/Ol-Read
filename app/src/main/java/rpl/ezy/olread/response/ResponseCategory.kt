package rpl.ezy.olread.response

import java.io.Serializable

class ResponseCategory (
    var status: Int,
    var message: String,
    var data: ArrayList<String>
): Serializable