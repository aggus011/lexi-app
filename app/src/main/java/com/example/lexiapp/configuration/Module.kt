package com.example.lexiapp.configuration

import com.example.lexiapp.data.word_asociation_api.WordAssociationClient
import com.example.lexiapp.data.word_asociation_api.WordAssociationService
import com.example.lexiapp.domain.LetterRepository
import com.example.lexiapp.ui.games.whereistheletter.WhereIsTheLetterViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//Configure dependency injection with Koin here

/*val appModule = module {
    single<WordAssociationService> {
        Retrofit.Builder()
            .baseUrl("https://api.wordassociations.net")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WordAssociationService::class.java)
    }
    single { WordAssociationClient(get()) }
    single { LetterRepository(get()) }
    viewModel { WhereIsTheLetterViewModel(get()) }
}*/


