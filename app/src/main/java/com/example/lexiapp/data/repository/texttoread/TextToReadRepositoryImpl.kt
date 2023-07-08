package com.example.lexiapp.data.repository.texttoread

import com.example.lexiapp.domain.model.TextToRead
import com.example.lexiapp.domain.service.TextToReadRepository
import javax.inject.Inject

class TextToReadRepositoryImpl @Inject constructor(): TextToReadRepository {

    override fun getTextToRead(categories: List<String>): List<TextToRead> {
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
}