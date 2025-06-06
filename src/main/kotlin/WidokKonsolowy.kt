import RezultatStrzalu.*
import SilnikStatki.Companion.ZNAK_PUSTEGO_POLA
import SilnikStatki.Companion.ZNAK_STATKU
import SilnikStatki.Companion.ZNAK_ZATOPIONEGO_STATKU
import SilnikStatki.Companion.listaRozmiarowStatkow
import StanGry.*

class WidokKonsolowy(
    private val uzywajKolorow: Boolean,
) : WidokGry() {
    override fun wyswietlPowitanie() {
        println("Witaj w grze w Statki!")
        println("Rozmieść swoje statki na planszy.")
        println("Podawaj pozycję w formacie np. A1, a kierunek jako P (poziomo) lub V (pionowo).")
        println("Plansza ma rozmiar od A1 do J10.")
    }

    override fun wyswietlRozpoczecieGry() {
        println("\nRozpoczęcie gry!")
    }

    override fun wyswietlPlansze(plansza: String) {
        println(plansza)
    }

    override fun wyswietlDwiePlansze(
        planszaPrzeciwnika: List<List<Char?>>,
        planszaGracza: List<List<Char>>,
        rozmiar: Int
    ) {
        wyswietlPlanszePoziomo(
            plansza1 = planszaPrzeciwnika,
            plansza2 = planszaGracza,
            rozmiar = rozmiar,
        )
    }

    override fun pobierzRuchGracza(): String {
        print("Podaj swój strzał: ")
        return readlnOrNull() ?: ""
    }

    override fun wyswietlRezultatStrzalu(rezultat: RezultatStrzalu) {
        when (rezultat) {
            PUDLO -> println("Pudło!")
            TRAFIONY -> println("Trafiony!")
            ZATOPIONY -> println("Zatopiony!")
            POZA_PLANSZA -> println("Nieprawidłowa pozycja!")
        }
    }

    override fun wyswietlKomunikatKoncaGry(stanGry: StanGry) {
        when (stanGry) {
            WYGRANA_GRACZA -> println("Gratulacje! Zatopiles wszystkie statki przeciwnika!")
            WYGRANA_KOMPUTERA -> println("Przegrałeś! Wszystkie Twoje statki zostały zatopione.")
            W_TOKU -> {
                // Nie wyświetlaj nic, gdy gra jest w toku
            }
        }
    }

    override fun pobierzDaneStatku(numerStatku: Int, dostepnePola: List<String>): Pair<String, Orientacja>? {
        println("Rozmieść statek nr $numerStatku (długość: ${Statek(getDlugoscStatku(numerStatku)).dlugosc}).")
        print("Podaj pozycję startową (np. A1): ")
        val pozycja = readlnOrNull()?.uppercase() ?: return null
        print("Podaj kierunek (P - poziomo, V - pionowo): ")
        val kierunekStr = readlnOrNull()?.uppercase() ?: return null

        val orientacja = when (kierunekStr) {
            "P" -> Orientacja.POZIOMA
            "V" -> Orientacja.PIONOWA
            else -> return null
        }
        return Pair(pozycja, orientacja)
    }

    override fun wyswietlBladRozmieszczeniaStatku() {
        println("Błąd rozmieszczenia statku: Nie można umieścić statku w podanym miejscu.")
    }

    override fun czyscEkran(czekajUzytkownika: Boolean) {
        if (czekajUzytkownika) {
            println("Naciśnij Enter, aby przejść dalej...")
            readlnOrNull()
        }
        repeat(100) {
            println()
        }
    }

    override fun wyswietlPlansze(plansza: List<List<Char?>>) {
        println("Twoja plansza")
        print("   ")
        ('A'..'J').forEach { print("  $it") }
        println()
        plansza.forEachIndexed { rowIndex, row ->
            print(String.format("%2d ", rowIndex + 1))
            row.forEach {
                print("  ${kolorujZnak(it)}")
            }
            println()
        }
    }

    private fun wyswietlPlanszePoziomo(plansza1: List<List<Char?>>, plansza2: List<List<Char>>, rozmiar: Int) {
        val naglowek = "   " + ('A'..'J').joinToString("  ")
        println("Plansza przeciwnika:                   Twoja plansza:")
        println("$naglowek        $naglowek")

        for (i in 0 until rozmiar) {
            val wiersz1 = plansza1[i].joinToString("  ") { kolorujZnak(it) }
            val wiersz2 = plansza2[i].joinToString("  ") { kolorujZnak(it) }
            println(String.format("%2d %-25s    ||  %2d %s", i + 1, wiersz1, i + 1, wiersz2))
        }
    }

    private fun kolorujZnak(znak: Char?): String {
        return when (znak) {
            ZNAK_ZATOPIONEGO_STATKU -> if (uzywajKolorow) "\u001B[31mX\u001B[0m" else "X"
            ZNAK_STATKU -> if (uzywajKolorow) "\u001B[34mS\u001B[0m" else "S"
            else -> (znak ?: ZNAK_PUSTEGO_POLA).toString()
        }
    }

    private fun getDlugoscStatku(numerStatku: Int): Int = listaRozmiarowStatkow[numerStatku - 1]

    override fun wyswietlRuchKomputera(pozycja: String) {
        println("Ruch komputera: $pozycja")
    }

    override fun czyAutomatycznieRozmiescicStatki(): Boolean {
        print("Czy chcesz automatycznie rozmieścić swoje statki? (T/N): ")
        val odpowiedz = readlnOrNull()?.uppercase() ?: "N"
        return odpowiedz == "T"
    }
}
