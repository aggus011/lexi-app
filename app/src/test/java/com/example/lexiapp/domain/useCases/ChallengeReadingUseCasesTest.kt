import com.example.lexiapp.domain.model.TextToRead
import com.example.lexiapp.domain.service.ChallengeReadingService
import com.example.lexiapp.domain.service.LetterService
import com.example.lexiapp.domain.service.OpenAICompletionsService
import com.example.lexiapp.domain.useCases.ChallengeReadingUseCases
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.*

@ExperimentalCoroutinesApi
class ChallengeReadingUseCasesTest {

    @RelaxedMockK
    lateinit var repositoryImpl: OpenAICompletionsService

    @RelaxedMockK
    lateinit var pendingReadingsRepositoryImpl: ChallengeReadingService

    @RelaxedMockK
    lateinit var LetterService: LetterService

    private lateinit var useCases: ChallengeReadingUseCases

    @Before
    fun setup() {
        repositoryImpl = mockk()
        pendingReadingsRepositoryImpl = mockk()
        LetterService = mockk()

        useCases = ChallengeReadingUseCases(repositoryImpl, pendingReadingsRepositoryImpl, LetterService)

        MockKAnnotations.init(this)
    }


    @Test
    fun `getChallengeReading emits the expected text to read`() = runBlocking {
        // Given
        val challengeWords = listOf("entrenamiento", "desafio", "juego")
        val challengeReading = "Este es un desafio de entrenamiento que parece un juego."
        val expectedTextToRead = TextToRead.Builder().id(6666).title("Desafío").text(challengeReading).build()

        coEvery { repositoryImpl.getChallengeReading(any(), any()) } returns flowOf(challengeReading)
        coEvery { LetterService.getWordsForChallengeReading(any(), any(), any()) } returns flowOf(challengeWords)

        // When
        val result = mutableListOf<TextToRead>()
        useCases.getChallengeReading().collect { result.add(it) }

        // Then
        assertEquals(1, result.size)
        assertEquals(expectedTextToRead.id, result[0].id)
        assertEquals(expectedTextToRead.title, result[0].title)
        assertEquals(expectedTextToRead.text, result[0].text)
    }



    @Test
    fun `updateOpenAiCompletionsLastUse updates the last_use field in the Firestore document`() = runBlocking {
        // Given
        val documentReference = mockk<DocumentReference>(relaxed = true)
        coEvery { pendingReadingsRepositoryImpl.getFirestoreOpenAICollectionDocumentReference(any()) } returns flowOf(documentReference)

        // When
        useCases.updateOpenAiCompletionsLastUse()

        // Then
        coVerify { documentReference.update("last_use", any<Date>()) }
    }


    @Test
    fun `updateOpenAiCompletionsLastUse calls update with expected arguments`() = runBlocking {
        // Given
        val documentReference = mockk<DocumentReference>(relaxed = true)
        coEvery { pendingReadingsRepositoryImpl.getFirestoreOpenAICollectionDocumentReference(any()) } returns flowOf(documentReference)

        // When
        useCases.updateOpenAiCompletionsLastUse()

        // Then
        verify { documentReference.update("last_use", any<Date>()) }
    }


    @Test
    fun `getLastUseOpenAICompletionDate cancels the flow collection`() = runBlocking {
        // Given
        val documentReference = mockk<DocumentReference>(relaxed = true)
        coEvery { pendingReadingsRepositoryImpl.getFirestoreOpenAICollectionDocumentReference(any()) } returns flowOf(documentReference)

        // When
        val job = useCases.getLastUseOpenAICompletionDate().launchIn(this)

        // Then
        job.cancel()
    }


}
