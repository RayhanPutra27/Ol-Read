package rpl.ezy.olread.response

import rpl.ezy.olread.model.MRecipe

class ResponseRecipes(
    var status: Int,
    var message: String,
    var data: ArrayList<MRecipe>
)