import Orientacja.PIONOWA
import Orientacja.POZIOMA
import RezultatStrzalu.POZA_PLANSZA
import StanGry.*

class SilnikStatki : SilnikGry() {
    private var obecnaStrategia: StrategiaStrzelania = StrategiaStrzelania()
    override val rozmiarPlanszy = 10
    private val planszaGracza = MutableList(rozmiarPlanszy) {
        MutableList(rozmiarPlanszy) {
            ZNAK_PUSTEGO_POLA
        }
    }
    private val planszaKomputera = MutableList(rozmiarPlanszy) {
        MutableList(rozmiarPlanszy) {
            ZNAK_PUSTEGO_POLA
        }
    }
    override val statkiGracza = mutableListOf<Statek>()
    override val statkiKomputera = mutableListOf<Statek>()
    override var liczbaZatopionychStatkowGracza = 0
    override var liczbaZatopionychStatkowKomputera = 0

    init {
        rozmiescWieleStatkow(listaRozmiarowStatkow, planszaKomputera, statkiKomputera)
    }

    override fun automatycznieRozmiescStatkiGracza() {
        planszaGracza.forEach {
            it.replaceAll {
                ZNAK_PUSTEGO_POLA
            }
        }
        statkiGracza.clear()
        liczbaZatopionychStatkowGracza = 0
        rozmiescWieleStatkow(listaRozmiarowStatkow, planszaGracza, statkiGracza)
    }

    private fun rozmiescWieleStatkow(
        rozmiary: List<Int>,
        plansza: MutableList<MutableList<Char>>,
        listaStatkow: MutableList<Statek>
    ) {
        rozmiary.forEach { rozmiescStatek(Statek(it), plansza, listaStatkow) }
    }

    private fun rozmiescStatek(
        statek: Statek,
        plansza: MutableList<MutableList<Char>>,
        listaStatkow: MutableList<Statek>
    ) {
        while (true) {
            val orientacja = if (Math.random() < 0.5) POZIOMA else PIONOWA
            val wiersz = (0 until rozmiarPlanszy).random()
            val kolumna = (0 until rozmiarPlanszy).random()

            if (czyMoznaUmiescic(statek, wiersz, kolumna, orientacja, plansza)) {
                statek.pola.clear()
                repeat(statek.dlugosc) { i ->
                    val r = wiersz + if (orientacja == PIONOWA) i else 0
                    val c = kolumna + if (orientacja == POZIOMA) i else 0
                    plansza[r][c] = ZNAK_STATKU
                    statek.pola.add(r to c)
                }
                listaStatkow.add(statek)
                break
            }
        }
    }

    private fun czyMoznaUmiescic(
        statek: Statek,
        wiersz: Int,
        kolumna: Int,
        orientacja: Orientacja,
        plansza: List<List<Char>>
    ): Boolean {
        return (0 until statek.dlugosc).all { i ->
            val r = wiersz + if (orientacja == PIONOWA) i else 0
            val c = kolumna + if (orientacja == POZIOMA) i else 0
            r in 0 until rozmiarPlanszy &&
                    c in 0 until rozmiarPlanszy &&
                    plansza[r][c] == ZNAK_PUSTEGO_POLA
        }
    }

    override fun rozmiescStatekGracza(statek: Statek, pozycjaStartowa: String, orientacja: Orientacja): Boolean {
        val kol = pozycjaStartowa[0].uppercaseChar()
        val row = pozycjaStartowa.drop(1).toIntOrNull()?.minus(1) ?: return false
        val col = kol - 'A'

        if (row !in 0 until rozmiarPlanszy || col !in 0 until rozmiarPlanszy) return false
        if (!czyMoznaUmiescic(statek, row, col, orientacja, planszaGracza)) return false

        statek.pola.clear()
        repeat(statek.dlugosc) { i ->
            val r = row + if (orientacja == PIONOWA) i else 0
            val c = col + if (orientacja == POZIOMA) i else 0
            planszaGracza[r][c] = ZNAK_STATKU
            statek.pola.add(r to c)
        }
        statkiGracza.add(statek)
        return true
    }

    override fun oddajStrzal(pozycja: String): RezultatStrzalu {
        val (row, col) = parsePozycja(pozycja) ?: return POZA_PLANSZA
        return wykonajStrzalNaPlanszy(
            row = row,
            col = col,
            plansza = planszaKomputera,
            statki = statkiKomputera,
        ) {
            liczbaZatopionychStatkowKomputera++
        }
    }

    override fun oddajStrzalKomputera(): Pair<String, RezultatStrzalu> {
        val strzal = obecnaStrategia.strzal(this)
        val (row, col) = parsePozycja(strzal) ?: return oddajStrzalKomputera()

        val rezultat = wykonajStrzalNaPlanszy(row, col, planszaGracza, statkiGracza) {
            liczbaZatopionychStatkowGracza++
        }

        obecnaStrategia.zaktualizujPoStrzale(strzal, rezultat)
        return strzal to rezultat
    }

    private fun parsePozycja(pozycja: String): Pair<Int, Int>? {
        if (pozycja.length < 2) return null
        val kol = pozycja[0].uppercaseChar()
        val row = pozycja.drop(1).toIntOrNull()?.minus(1) ?: return null
        val col = kol - 'A'
        return if (row in 0 until rozmiarPlanszy && col in 0 until rozmiarPlanszy) row to col else null
    }

    private fun wykonajStrzalNaPlanszy(
        row: Int,
        col: Int,
        plansza: MutableList<MutableList<Char>>,
        statki: List<Statek>,
        onZatopiony: () -> Unit
    ): RezultatStrzalu {
        return when (plansza[row][col]) {
            ZNAK_STATKU -> {
                plansza[row][col] = ZNAK_ZATOPIONEGO_STATKU
                val trafiony = statki.find { statek -> statek.pola.contains(row to col) }
                trafiony?.let {
                    it.trafienia.add(row to col)
                    if (it.czyZatopiony()) {
                        onZatopiony()
                        RezultatStrzalu.ZATOPIONY
                    } else RezultatStrzalu.TRAFIONY
                } ?: RezultatStrzalu.TRAFIONY
            }

            ZNAK_PUSTEGO_POLA
                -> {
                plansza[row][col] = ZNAK_PUDLA
                RezultatStrzalu.PUDLO
            }

            else -> RezultatStrzalu.PUDLO
        }
    }

    override fun pobierzPlanszePrzeciwnika(): List<List<Char?>> {
        return planszaKomputera.map { wiersz ->
            wiersz.map { if (it == ZNAK_STATKU) null else it }
        }
    }

    override fun pobierzWlasnaPlansze(): List<List<Char>> {
        return planszaGracza.map { it.toList() }
    }

    override fun czyKoniecGry(): StanGry = when {
        liczbaZatopionychStatkowKomputera == statkiKomputera.size -> WYGRANA_GRACZA
        liczbaZatopionychStatkowGracza == statkiGracza.size -> WYGRANA_KOMPUTERA
        else -> W_TOKU
    }

    companion object {
        val listaRozmiarowStatkow = listOf(4, 3, 3, 2, 2, 2, 1, 1, 1, 1)
        const val ZNAK_STATKU = 'S'
        const val ZNAK_ZATOPIONEGO_STATKU = 'X'
        const val ZNAK_PUDLA = 'O'
        const val ZNAK_PUSTEGO_POLA = '.'
    }
}
