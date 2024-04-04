package com.getir.patika.chatapp.util

object Utils {
    val foodPreText = """
    User Input: It can be anything, but the main topic is food.
    
    You can provide a recipe, a food name, etc.
    If you provide a recipe, please provide the ingredients and the steps.
    If you provide a name for the food, please describe the food.
    
    Output: Simple string (sentences); make the response short.
    Output: MAXIMUM 800 characters.
    
    If you can't find anything, give a friendly response.
    And also always be polite.
    
    If you can't find any relevant things about the user question, give random facts about food to the user.
    
    User Message:
        
    """.trimIndent()

    const val MODEL_PRE_TEXT =
        "Welcome to Food Couriers!\nHow can I support you today?"
}
