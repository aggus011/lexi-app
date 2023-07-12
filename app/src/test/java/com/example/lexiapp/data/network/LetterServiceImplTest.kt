package com.example.lexiapp.data.network

import android.content.SharedPreferences
import com.example.lexiapp.data.api.word_asociation_api.WordAssociationGateway
import com.example.lexiapp.data.model.toCorrectWordDataResult
import com.example.lexiapp.data.model.toWhereIsTheLetterDataResult
import com.example.lexiapp.domain.model.gameResult.CorrectWordGameResult
import com.example.lexiapp.domain.model.gameResult.WhereIsTheLetterResult
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class LetterServiceImplTest {
    @RelaxedMockK
    private lateinit var apiWordService: WordAssociationGateway
    @RelaxedMockK
    private lateinit var db: FireStoreServiceImpl
    @RelaxedMockK
    private lateinit var prefs: SharedPreferences
    @RelaxedMockK
    private lateinit var notificationServiceImpl: FirebaseNotificationServiceImpl

    lateinit var letterServiceImpl: LetterServiceImpl
    lateinit var userMail: String

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        every{ prefs.getString("email", null) } returns emailTest
        userMail = prefs.getString("email", null)!!
        letterServiceImpl = LetterServiceImpl(apiWordService, db, prefs, notificationServiceImpl)
    }

    @Test
    fun `when get wrongWods and have a valid length, use it to call the api`() = runBlocking{
        //Give
        coEvery { db.getWordPlayed(userMail) } returns Pair(true, validLengthWrongWords)
        //When
        letterServiceImpl.getWord(1,1,"").collect {
            //Then
            coVerify (exactly = 1) { db.getWordPlayed(userMail) }
            coVerify (exactly = 0) { db.getWordCategories(userMail) }
            coVerify (exactly = 1) { apiWordService.getWordToWhereIsTheLetterGame(1, 1, "", validLengthWrongWords) }
        }
    }

    @Test
    fun `when get wrongWods and don´t have a valid length, get and use the categoris from firestore to call the api`() = runBlocking{
        //Give
        coEvery { db.getWordPlayed(userMail) } returns Pair(false, emptyList())
        coEvery { db.getWordCategories(userMail) } returns categories
        //When
        letterServiceImpl.getWord(1,1,"").collect {
            //Then
            coVerify (exactly = 1) { db.getWordPlayed(userMail) }
            coVerify (exactly = 1) { db.getWordCategories(userMail) }
            coVerify (exactly = 1) { apiWordService.getWordToWhereIsTheLetterGame(1, 1, "", categories) }
        }
    }

    @Test
    fun `when get words from the api, filter the list and return the first word`() = runBlocking{
        //Give
        coEvery { db.getWordPlayed(userMail) } returns Pair(true, listWithInvalidWords)
        //When
        letterServiceImpl.getWord(1,1,"").collect {
            //Then
            assert (word == it)
            coVerify (exactly = 1) { db.getWordPlayed(userMail) }
            coVerify (exactly = 0) { db.getWordCategories(userMail) }
            coVerify (exactly = 1) { apiWordService.getWordToWhereIsTheLetterGame(1, 1, "", listWithInvalidWords) }
        }
    }

    @Test
    fun `when try to save a playResultWITL, save the result and update the progress`() = runBlocking {
        //Give
        val result = WhereIsTheLetterResult (
            userMail, char, char, word,negativeSuccess, date
        )
        //When
        letterServiceImpl.saveResult(result)
        //Then
        coVerify (exactly = 1) { db.updateObjectiveProgress(typeGameWITL, typePlay) }
        coVerify (exactly = 0) { db.updateObjectiveProgress(typeGameWITL, typeHit) }
        coVerify (exactly = 1) { db.saveWhereIsTheLetterResult(result.toWhereIsTheLetterDataResult(), userMail) }
    }

    @Test
    fun `when try to save a hitResultWITL, save the result and update the progress`() = runBlocking {
        //Give
        val result = WhereIsTheLetterResult (
            userMail, char, char, word,positiveSuccess, date
        )
        //When
        letterServiceImpl.saveResult(result)
        //Then
        coVerify (exactly = 1) { db.updateObjectiveProgress(typeGameWITL, typePlay) }
        coVerify (exactly = 1) { db.updateObjectiveProgress(typeGameWITL, typeHit) }
        coVerify (exactly = 1) { db.saveWhereIsTheLetterResult(result.toWhereIsTheLetterDataResult(), userMail) }
    }

    @Test
    fun `when try to save a playResultCW, save the result and update the progress`() = runBlocking {
        //Give
        val result = CorrectWordGameResult (
            userMail, word, word, negativeSuccess, date
        )
        //When
        letterServiceImpl.saveResult(result)
        //Then
        coVerify (exactly = 1) { db.updateObjectiveProgress(typeGameCW, typePlay) }
        coVerify (exactly = 0) { db.updateObjectiveProgress(typeGameCW, typeHit) }
        coVerify (exactly = 1) { db.saveCorrectWordResult(result.toCorrectWordDataResult(), userMail) }
    }

    @Test
    fun `when try to save a hitResultCW, save the result and update the progress`() = runBlocking {
        //Give
        val result = CorrectWordGameResult (
            userMail, word, word, positiveSuccess, date
        )
        //When
        letterServiceImpl.saveResult(result)
        //Then
        coVerify (exactly = 1) { db.updateObjectiveProgress(typeGameCW, typePlay) }
        coVerify (exactly = 1) { db.updateObjectiveProgress(typeGameCW, typeHit) }
        coVerify (exactly = 1) { db.saveCorrectWordResult(result.toCorrectWordDataResult(), userMail) }
    }

    @Test
    fun `when get words from the api, filter the list and return the count of parameter`() = runBlocking{
        //Give
        coEvery { db.getWordPlayed(userMail) } returns Pair(true, listWithWrongWords)
        //When
        letterServiceImpl.getWordsForChallengeReading(1,1,"").collect {
            //Then
            assert ( listWords == it)
            coVerify (exactly = 1) { db.getWordPlayed(userMail) }
            coVerify (exactly = 0) { db.getWordCategories(userMail) }
            coVerify (exactly = 1) { apiWordService.getWordToWhereIsTheLetterGame(1, 1, "", listWithInvalidWords) }
        }
    }
}

const val emailTest = "test@gmail.com"
val categories = listOf("category1", "category2", "category3")
val listWithInvalidWords = listOf("PINGO", "SEXO", "WORD")
val validLengthWrongWords = listOf("CHALLENGE", "READING", "BETWEEN", "TODAY", "FAVORITE", "FRIEND", "WORLD", "PEOPLE", "TOGETHER", "PLAY", "LEARN", "LAUGH", "CREATE", "EXPLORE", "DISCOVER", "SUCCESS")
const val positiveSuccess = true
const val negativeSuccess = false
const val totalWords = 20
const val char = '*'
const val word = "WORD"
val listWithWrongWords = listOf("PINGO", "SEXO", "WORD","CASA", "PATO", "BOTA")
val listWords = listOf("CASA", "PATO", "BOTA")
var date = System.currentTimeMillis().toString()
const val nameWTIL = "Donde Esta La Letra"
const val nameCW = "Palabra Correcta"
const val typeGameWITL = "WL"
const val typeGameCW = "CW"
const val typePlay = "play"
const val typeHit = "hit"