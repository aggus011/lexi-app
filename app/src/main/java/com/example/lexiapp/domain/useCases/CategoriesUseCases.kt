package com.example.lexiapp.domain.useCases

import android.content.SharedPreferences
import android.util.Log
import com.example.lexiapp.data.repository.texttoread.*
import com.example.lexiapp.domain.exceptions.FirestoreException
import com.example.lexiapp.domain.model.TextToRead
import com.example.lexiapp.domain.service.FireStoreService
import javax.inject.Inject

class CategoriesUseCases @Inject constructor(
    private val fireStoreServiceImpl: FireStoreService,
    private val sharedPreferences: SharedPreferences
) {
    private val editor = sharedPreferences.edit()

    fun getEmailCurrentUser(): String?{
        return sharedPreferences.getString("email", null)
    }

    suspend fun saveCategories(email: String, categories: List<String>){
        if(validateIfUserHasChooseThreeCategories(categories)){
            saveCategoriesToSharedPreferences(categories)
            saveCategoriesToFirestore(email, categories)
        }
    }

    suspend fun getCategoriesFromPatient(email: String): List<String>{
        /*
        takes user email and search if categories are saved in shared preferences
        and convert string into a list of strings, or return null,
        if return null, search categories of user by his email in firestore,
        if not find categories for user email return a empty list
        */
        val sharedPrefsCategories = sharedPreferences.getString("categories", null)
            ?: return getCategoriesFromFirestore(email)

        return sharedPrefsCategories.split(";")
    }

    suspend fun getCategoriesFromFirestore(email: String): List<String> {
        return fireStoreServiceImpl.getPatientCategories(email)
    }
    
    fun getListTextToReadWithCategories(categories: List<String>): List<TextToRead>{
        val texts = mutableListOf<TextToRead>()
        
        if(categories.isNotEmpty()){

            categories.forEach{
                val textsToAdd = when(it) {
                    "Animales" -> TextAnimals.getTexts()
                    "Insectos" -> TextInsects.getTexts()
                    "Frutas" -> TextFruits.getTexts()
                    "Verduras" -> TextVegetables.getTexts()
                    "Colores" -> TextColors.getTexts()
                    "Nombres" -> TextNames.getTexts()
                    "Lugares" -> TextPlaces.getTexts()
                    "Países" -> TextCountries.getTexts()
                    "Vehículos"-> TextVehicles.getTexts()
                    else -> TextToReadMocks.getAllTextToReadMocks()
                }
                texts.addAll(textsToAdd)
            }
        }
        
        return texts
    }

    private fun saveCategoriesToSharedPreferences(categories: List<String>) {
        /*
        takes list string of categories selected and join to a one string
        for save it into shared preferences with each category
        separated by ";"
         */

        val joinedList = categories.joinToString(";")
        editor.putString("categories", joinedList).apply()
    }

    private suspend fun saveCategoriesToFirestore(email: String, categories: List<String>) {
        try {
            fireStoreServiceImpl.saveCategoriesFromPatient(email, categories)
        }catch (e: FirestoreException){
            Log.v(TAG, "Error to save categories from patient $email with message: ${e.message}")
        }
    }

    private fun validateIfUserHasChooseThreeCategories(categories: List<String>): Boolean{
        return categories.size == 3
    }

    companion object{
        private const val TAG = "CategoriesUseCases"
    }
}