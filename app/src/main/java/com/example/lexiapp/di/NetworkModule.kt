package com.example.lexiapp.di

import dagger.Module
import dagger.hilt.InstallIn

//Configure dependency injection with Koin here
@Module
@InstallIn()
object NetworkModule {

}

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


