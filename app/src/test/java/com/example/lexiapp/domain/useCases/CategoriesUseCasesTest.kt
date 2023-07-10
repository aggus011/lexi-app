package com.example.lexiapp.domain.useCases

import android.content.SharedPreferences
import com.example.lexiapp.data.network.FireStoreServiceImpl
import com.example.lexiapp.data.network.LetterServiceImpl
import com.example.lexiapp.data.repository.texttoread.TextAnimals
import com.example.lexiapp.data.repository.texttoread.TextFruits
import com.example.lexiapp.data.repository.texttoread.TextInsects
import com.example.lexiapp.domain.model.TextToRead
import com.example.lexiapp.domain.service.TextToReadRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class CategoriesUseCasesTest{
    @RelaxedMockK
    private lateinit var repository: TextToReadRepository

    @RelaxedMockK
    private lateinit var db: FireStoreServiceImpl

    @RelaxedMockK
    private lateinit var prefs: SharedPreferences

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `when exist categories in sharedPreferences, get them`() = runBlocking {
        // Given
        every { prefs.getString("categories", null) } returns "category1category2category3"
        val useCase = CategoriesUseCases(db,prefs, repository)
        val email = "test@example.com"
        // When
        val categories = useCase.getCategoriesFromPatient(email)
        //Then
        assert(categories==("category1category2category3").split(";"))
    }

    @Test
    fun `when not exist categories in sharedPreferences, get categories from firestore`() = runBlocking {
        // Given
        every { prefs.getString("categories", null) } returns null
        val email = "test@example.com"
        coEvery { db.getPatientCategories(email)} returns listOf("category1","category2","category3")
        val useCase = CategoriesUseCases(db,prefs, repository)
        // When
        val categories = useCase.getCategoriesFromPatient(email)
        //Then
        assert(categories==listOf("category1","category2","category3"))
    }

    @Test
    fun `get texts form repository, when get categories from sharedPreferences`() = runBlocking {
        every { prefs.getString("categories", null) } returns "AnimalesFrutasInsectos"
        every { repository.getTextToRead(("AnimalesFrutasInsectos").split(";")) } returns getTexts()
        val useCase = CategoriesUseCases(db,prefs, repository)
        val email = "test@example.com"
        // When
        val categories = useCase.getCategoriesFromPatient(email)
        val texts = useCase.getListTextToReadWithCategories(categories)
        //Then
        assert(texts.size == getTexts().size)
        assert(texts.map {it.text}== getStringTexts())
    }
}
fun getStringTexts(): List<String> {
    val texts = mutableListOf<TextToRead>()
    texts.addAll(TextAnimals.getTexts())
    texts.addAll(TextInsects.getTexts())
    texts.addAll(TextFruits.getTexts())
    return texts.map { it.text!! }
}

fun getTexts(): List<TextToRead> {
    val texts = mutableListOf<TextToRead>()
    texts.addAll(TextAnimals.getTexts())
    texts.addAll(TextInsects.getTexts())
    texts.addAll(TextFruits.getTexts())
    return texts
}