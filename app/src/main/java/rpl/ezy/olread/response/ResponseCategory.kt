package rpl.ezy.olread.response

import rpl.ezy.olread.model.MCategory
import java.io.Serializable

class ResponseCategory (
    var status: Int,
    var message: String,
    var data: ArrayList<MCategory>
): Serializable