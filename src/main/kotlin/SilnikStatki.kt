class SilnikStatki : SilnikGry {
    private var obecnaStrategia: StrategiaStrzelania = StrategiaStrzelania()
    override val rozmiarPlanszy = 10
    private val planszaGracza = MutableList(rozmiarPlanszy) { MutableList(rozmiarPlanszy) { '.' } }
    private val planszaKomputera = MutableList(rozmiarPlanszy) { MutableList(rozmiarPlanszy) { '.' } }
    override val statkiGracza = mutableListOf<Statek>()
    override val statkiKomputera = mutableListOf<Statek>()
    override var liczbaZatopionychStatkowGracza = 0
    override var liczbaZatopionychStatkowKomputera = 0


    init {
        rozmiescWieleStatkow(listaRozmiarowStatkow, planszaKomputera, statkiKomputera)
    }

    override fun rozpocznijNowaGre() {
        planszaGracza.forEach { it.replaceAll { '.' } }
        planszaKomputera.forEach { it.replaceAll { '.' } }
        statkiGracza.clear()
        statkiKomputera.clear()
        liczbaZatopionychStatkowGracza = 0
        liczbaZatopionychStatkowKomputera = 0
        rozmiescWieleStatkow(listaRozmiarowStatkow, planszaKomputera, statkiKomputera)
        obecnaStrategia.resetuj()
    }

    override fun automatycznieRozmiescStatkiGracza() {
        planszaGracza.forEach { it.replaceAll { '.' } }
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
            val orientacja = if (Math.random() < 0.5) Orientacja.POZIOMA else Orientacja.PIONOWA
            val wiersz = (0 until rozmiarPlanszy).random()
            val kolumna = (0 until rozmiarPlanszy).random()

            if (czyMoznaUmiescic(statek, wiersz, kolumna, orientacja, plansza)) {
                statek.pola.clear()
                repeat(statek.dlugosc) { i ->
                    val r = wiersz + if (orientacja == Orientacja.PIONOWA) i else 0
                    val c = kolumna + if (orientacja == Orientacja.POZIOMA) i else 0
                    plansza[r][c] = 'S'
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
            val r = wiersz + if (orientacja == Orientacja.PIONOWA) i else 0
            val c = kolumna + if (orientacja == Orientacja.POZIOMA) i else 0
            r in 0 until rozmiarPlanszy &&
                    c in 0 until rozmiarPlanszy &&
                    plansza[r][c] == '.'
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
            val r = row + if (orientacja == Orientacja.PIONOWA) i else 0
            val c = col + if (orientacja == Orientacja.POZIOMA) i else 0
            planszaGracza[r][c] = 'S'
            statek.pola.add(r to c)
        }
        statkiGracza.add(statek)
        return true
    }

    override fun oddajStrzal(pozycja: String): RezultatStrzalu {
        val (row, col) = parsePozycja(pozycja) ?: return RezultatStrzalu.POZA_PLANSZA
        return wykonajStrzalNaPlanszy(row, col, planszaKomputera, statkiKomputera) {
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
            'S' -> {
                plansza[row][col] = 'X'
                val trafiony = statki.find { statek -> statek.pola.contains(row to col) }
                trafiony?.let {
                    it.trafienia.add(row to col)
                    if (it.czyZatopiony()) {
                        onZatopiony()
                        RezultatStrzalu.ZATOPIONY
                    } else RezultatStrzalu.TRAFIONY
                } ?: RezultatStrzalu.TRAFIONY
            }

            '.' -> {
                plansza[row][col] = 'O'
                RezultatStrzalu.PUDLO
            }

            else -> RezultatStrzalu.PUDLO
        }
    }

    override fun pobierzPlanszePrzeciwnika(): List<List<Char?>> {
        return planszaKomputera.map { wiersz ->
            wiersz.map { if (it == 'S') null else it }
        }
    }

    override fun pobierzWlasnaPlansze(): List<List<Char>> {
        return planszaGracza.map { it.toList() }
    }

    override fun czyKoniecGry(): Boolean {
        return liczbaZatopionychStatkowKomputera == statkiKomputera.size ||
                liczbaZatopionychStatkowGracza == statkiGracza.size
    }

    companion object {
        val listaRozmiarowStatkow = listOf(4, 3, 3, 2, 2, 2, 1, 1, 1, 1)
    }
}
