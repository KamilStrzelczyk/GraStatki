import RezultatStrzalu.TRAFIONY
import RezultatStrzalu.ZATOPIONY

class StrategiaStrzelania {
    private val trafionePola = mutableListOf<Pair<Int, Int>>()
    private var ostatniTrafiony: Pair<Int, Int>? = null
    private var kierunek: Pair<Int, Int>? = null
    private val strzalyWykonane = mutableSetOf<String>()

    fun strzal(silnik: SilnikGry): String = ostatniTrafiony?.let { trafiony ->
        val rozmiar = silnik.rozmiarPlanszy
        if (kierunek != null) {
            probujStrzelWKierunku(trafiony, kierunek!!, rozmiar) ?: probujStrzelWKierunku(
                trafiony,
                Pair(-kierunek!!.first, -kierunek!!.second),
                rozmiar
            )
        } else {
            wybierzSasiedniStrzal(trafiony, rozmiar)
        }
    } ?: wybierzLosowyStrzal(silnik)

    private fun wybierzSasiedniStrzal(ostatnioTrafiony: Pair<Int, Int>, rozmiar: Int): String? {
        val (wiersz, kolumna) = ostatnioTrafiony
        val sasiedniePola = listOf(
            Pair(wiersz - 1, kolumna),
            Pair(wiersz + 1, kolumna),
            Pair(wiersz, kolumna - 1),
            Pair(wiersz, kolumna + 1)
        ).filter { (r, c) -> czyPoleWPlanszy(r, c, rozmiar) }

        for ((r, c) in sasiedniePola) {
            val pozycja = kolumnaWierszNaPozycje(c, r)
            if (pozycja !in strzalyWykonane) {
                strzalyWykonane.add(pozycja)
                return pozycja
            }
        }
        return null
    }

    private fun probujStrzelWKierunku(
        ostatnioTrafiony: Pair<Int, Int>,
        kierunek: Pair<Int, Int>,
        rozmiar: Int
    ): String? {
        var (wiersz, kolumna) = ostatnioTrafiony
        wiersz += kierunek.first
        kolumna += kierunek.second
        val pozycja = kolumnaWierszNaPozycje(kolumna, wiersz)

        return if (czyPoleWPlanszy(wiersz, kolumna, rozmiar) && pozycja !in strzalyWykonane) {
            strzalyWykonane.add(pozycja)
            pozycja
        } else {
            null
        }
    }

    fun zaktualizujPoStrzale(pozycja: String, rezultat: RezultatStrzalu) {
        val (kolumna, wiersz) = pozycjaNaKolumnaWiersz(pozycja)
        when (rezultat) {
            TRAFIONY -> {
                trafionePola.add(Pair(wiersz, kolumna))
                if (ostatniTrafiony == null) {
                    ostatniTrafiony = Pair(wiersz, kolumna)
                } else if (kierunek == null) {
                    kierunek = if (wiersz == ostatniTrafiony!!.first) {
                        Pair(0, if (kolumna > ostatniTrafiony!!.second) 1 else -1)
                    } else {
                        Pair(if (wiersz > ostatniTrafiony!!.first) 1 else -1, 0)
                    }
                }
            }

            ZATOPIONY -> {
                trafionePola.clear()
                ostatniTrafiony = null
                kierunek = null
            }

            else -> {
                // brak reakcji na pudło/poza planszą
            }
        }
    }

    private fun wybierzLosowyStrzal(silnik: SilnikGry): String {
        val rozmiar = silnik.rozmiarPlanszy
        while (true) {
            val kolumna = ('A' until 'A' + rozmiar).random()
            val wiersz = (1..rozmiar).random()
            val pozycja = "$kolumna$wiersz"
            if (pozycja !in strzalyWykonane) {
                strzalyWykonane.add(pozycja)
                return pozycja
            }
        }
    }

    private fun kolumnaWierszNaPozycje(kolumna: Int, wiersz: Int): String {
        return "${('A' + kolumna)}${wiersz + 1}"
    }

    private fun pozycjaNaKolumnaWiersz(pozycja: String): Pair<Int, Int> {
        val kolumna = pozycja[0].uppercaseChar() - 'A'
        val wiersz = pozycja.substring(1).toInt() - 1
        return Pair(kolumna, wiersz)
    }

    private fun czyPoleWPlanszy(wiersz: Int, kolumna: Int, rozmiarPlanszy: Int): Boolean {
        return wiersz in 0 until rozmiarPlanszy && kolumna in 0 until rozmiarPlanszy
    }
}
