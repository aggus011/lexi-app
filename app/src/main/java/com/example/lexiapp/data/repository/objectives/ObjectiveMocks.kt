import com.example.lexiapp.domain.model.Objective
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object ObjectiveMocks {

    fun getObjectiveMocks(): List<Objective> {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val formattedDate = currentDate.format(formatter)
        val allObjectives = listOf(
            Objective.Builder()
                .id(1L)
                .title("Jugar Palabra Correcta")
                .description("Descripción de Jugar Palabra Correcta")
                .progress(0)
                .goal(10)
                .game("CW")
                .type("play")
                .completed(false)
                .date(formattedDate.toString())
                .build(),
            Objective.Builder()
                .id(3L)
                .title("Jugar ¿Dónde está la Letra?")
                .description("Descripción de Jugar ¿Donde esta la Letra?")
                .progress(0)
                .goal(10)
                .game("WL")
                .type("play")
                .completed(false)
                .date(formattedDate.toString())
                .build(),
            Objective.Builder()
                .id(4L)
                .title("Escanear un Texto Propio")
                .description("Descripción de Escanear un Texto Propio")
                .progress(0)
                .goal(1)
                .game("SCAN")
                .type("play")
                .completed(false)
                .date(formattedDate.toString())
                .build(),
            Objective.Builder()
                .id(5L)
                .title("Jugar Lectura Desafío")
                .description("Descripción de Jugar Lectura Desafío")
                .progress(0)
                .goal(5)
                .game("RC")
                .type("play")
                .completed(false)
                .date(formattedDate.toString())
                .build(),
            Objective.Builder()
                .id(6L)
                .title("Acertar en Palabra Correcta")
                .description("Descripción de Acertar en Palabra Correcta")
                .progress(0)
                .goal(5)
                .game("CW")
                .type("hit")
                .completed(false)
                .date(formattedDate.toString())
                .build(),
            Objective.Builder()
                .id(7L)
                .title("Acertar en Lectura Desafío")
                .description("Descripción de Acertar en Lectura Desafío")
                .progress(0)
                .goal(3)
                .game("RC")
                .type("hit")
                .completed(false)
                .date(formattedDate.toString())
                .build(),
            Objective.Builder()
                .id(8L)
                .title("Acertar en ¿Dónde está la Letra?")
                .description("Descripción de Acertar en ¿Donde esta la Letra?")
                .progress(0)
                .goal(5)
                .game("WL")
                .type("hit")
                .completed(false)
                .date(formattedDate.toString())
                .build(),
            Objective.Builder()
                .id(9L)
                .title("Jugar ¡Vamos a Leer!")
                .description("Descripción de Jugar ¡Vamos a Leer!")
                .progress(0)
                .goal(3)
                .game("LR")
                .type("play")
                .completed(false)
                .date(formattedDate.toString())
                .build(),
            Objective.Builder()
                .id(10L)
                .title("Acertar en ¡Vamos a Leer!")
                .description("Descripción de Acertar en ¡Vamos a Leer!")
                .progress(0)
                .goal(2)
                .game("LR")
                .type("hit")
                .completed(false)
                .date(formattedDate.toString())
                .build()
        )

        fun getRandomObjectiveByGame(game: String): Objective {
            val objectivesByGame = allObjectives.filter { it.game == game }
            return objectivesByGame.randomOrNull() ?: Objective.Builder().build()
        }

        val games = listOf("CW", "RC", "WL", "SCAN", "LR").shuffled().take(4)
        val objectives = games.map { getRandomObjectiveByGame(it) }

        return objectives
    }
}
