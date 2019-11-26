package rpl.ezy.olread.response

import rpl.ezy.olread.model.MRecipe

class ResponseRecipeById(
    var status: Int,
    var message: String,
    var data: MRecipe
)