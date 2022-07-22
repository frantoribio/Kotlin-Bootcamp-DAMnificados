package repositories.tenistas

import config.AppConfig
import config.DataBase
import entities.RaquetaDao
import entities.TenistaDao
import models.Tenista
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.*
import java.time.LocalDate

@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Para el beforeAll y afterAll
internal class TenistasRepositoryImplTest {

    private val tenistasRepository: TenistasRepository = TenistasRepositoryImpl(TenistaDao, RaquetaDao)

    private val tenista = Tenista(
        nombre = "Tenista Test",
        ranking = 3,
        fechaNacimiento = LocalDate.parse("1985-06-04"),
        añoProfesional = 2005,
        altura = 185,
        peso = 80,
        ganancias = 10000000.0,
        manoDominante = Tenista.ManoDominante.from("Izquierda"),
        tipoReves = Tenista.TipoReves.from("dos manos"),
        puntos = 6789
    )

    @BeforeAll
    fun setUp() {
        DataBase.init(AppConfig.DEFAULT)
    }

    @AfterAll
    fun tearDown() {
        DataBase.dropTables()
    }

    @BeforeEach
    fun beforeEach() {
        DataBase.clearTables()
    }


    @Test
    fun findAll() {
        val res = tenistasRepository.findAll()

        assert(res.isEmpty())
    }

    @Test
    fun findById() = transaction {
        val insert = TenistaDao.new(tenista.id) {
            nombre = tenista.nombre
            ranking = tenista.ranking
            fechaNacimiento = tenista.fechaNacimiento
            añoProfesional = tenista.añoProfesional
            altura = tenista.altura
            peso = tenista.peso
            ganancias = tenista.ganancias
            manoDominante = tenista.manoDominante.mano
            tipoReves = tenista.tipoReves.tipo
            puntos = tenista.puntos
        }

        val res = tenistasRepository.findById(tenista.id)

        assert(res == tenista)
    }

    @Test
    fun findByIdNoExiste() {
        val res = tenistasRepository.findById(tenista.id)

        assert(res == null)
    }

    @Test
    fun saveInsert() {
        val res = tenistasRepository.save(tenista)

        assert(res == tenista)
    }

    @Test
    fun saveUpdate() = transaction {
        val insert = TenistaDao.new(tenista.id) {
            nombre = tenista.nombre
            ranking = tenista.ranking
            fechaNacimiento = tenista.fechaNacimiento
            añoProfesional = tenista.añoProfesional
            altura = tenista.altura
            peso = tenista.peso
            ganancias = tenista.ganancias
            manoDominante = tenista.manoDominante.mano
            tipoReves = tenista.tipoReves.tipo
            puntos = tenista.puntos
        }

        val res = tenistasRepository.save(tenista)

        assert(res == tenista)
    }

    @Test
    fun delete() = transaction {
        val insert = TenistaDao.new(tenista.id) {
            nombre = tenista.nombre
            ranking = tenista.ranking
            fechaNacimiento = tenista.fechaNacimiento
            añoProfesional = tenista.añoProfesional
            altura = tenista.altura
            peso = tenista.peso
            ganancias = tenista.ganancias
            manoDominante = tenista.manoDominante.mano
            tipoReves = tenista.tipoReves.tipo
            puntos = tenista.puntos
        }

        val res = tenistasRepository.delete(tenista)

        assert(res)
    }

    @Test
    fun deleteNoExiste() {
        val res = tenistasRepository.delete(tenista)

        assert(!res)
    }
}