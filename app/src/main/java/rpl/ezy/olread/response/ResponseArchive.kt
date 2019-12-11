package rpl.ezy.olread.response

import java.io.Serializable

class ResponseArchive (
    var status: Int,
    var message: String,
    var data: ArrayList<MArchive>
): Serializable {
    inner class MArchive(
        var user_id: Int,
        var recipe_id: Int
    )
}
