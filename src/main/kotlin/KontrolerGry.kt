import RezultatStrzalu.PUDLO
import SilnikStatki.Companion.listaRozmiarowStatkow
import StanGry.*

class KontrolerGry(private val silnik: SilnikGry, private val widok: WidokGry) {

    init {
        widok.wyswietlPowitanie()
        przygotujStatkiGracza()
        widok.wyswietlRozpoczecieGry()
        petlaGry()
    }

    private fun petlaGry() {
        while (true) {
            widok.czyscEkran(true)
            wyswietlDwiePlansze()

            if (!ruchGracza() || czyGraZakonczona()) break

            widok.czyscEkran(true)
            wyswietlDwiePlansze()

            if (!ruchKomputera() || czyGraZakonczona()) break
        }
    }

    private fun ruchGracza(): Boolean {
        while (true) {
            val ruch = widok.pobierzRuchGracza()
            val rezultat = silnik.oddajStrzal(ruch)
            widok.wyswietlRezultatStrzalu(rezultat)

            if (rezultat == PUDLO) break
        }
        return true
    }

    private fun ruchKomputera(): Boolean {
        while (true) {
            val (ruch, rezultat) = silnik.oddajStrzalKomputera()
            widok.wyswietlRuchKomputera(ruch)
            widok.wyswietlRezultatStrzalu(rezultat)

            if (rezultat == PUDLO) break
        }
        return true
    }

    private fun czyGraZakonczona(): Boolean = when (val stanGry = silnik.czyKoniecGry()) {
        W_TOKU -> false
        WYGRANA_GRACZA, WYGRANA_KOMPUTERA -> {
            widok.czyscEkran()
            wyswietlDwiePlansze()
            widok.wyswietlKomunikatKoncaGry(stanGry)
            true
        }
    }

    private fun przygotujStatkiGracza() {
        if (widok.czyAutomatycznieRozmiescicStatki()) {
            silnik.automatycznieRozmiescStatkiGracza()
            widok.czyscEkran()
            widok.wyswietlPlansze(silnik.pobierzWlasnaPlansze())
        } else {
            rozmiescStatkiRecznie()
        }
    }

    private fun rozmiescStatkiRecznie() {
        val dostepnePola = ('A'..'J').flatMap { kol -> (1..10).map { wiersz -> "$kol$wiersz" } }

        listaRozmiarowStatkow.forEachIndexed { index, dlugosc ->
            val statek = Statek(dlugosc)
            while (true) {
                val dane = widok.pobierzDaneStatku(
                    numerStatku = index + 1,
                    dostepnePola = dostepnePola,
                ) ?: continue
                val (pozycja, orientacja) = dane
                if (silnik.rozmiescStatekGracza(
                        statek = statek,
                        pozycjaStartowa = pozycja,
                        orientacja = orientacja,
                    )
                ) {
                    widok.czyscEkran()
                    widok.wyswietlPlansze(silnik.pobierzWlasnaPlansze())
                    break
                } else {
                    widok.wyswietlBladRozmieszczeniaStatku()
                }
            }
        }
    }

    private fun wyswietlDwiePlansze() {
        widok.wyswietlDwiePlansze(
            planszaPrzeciwnika = silnik.pobierzPlanszePrzeciwnika(),
            planszaGracza = silnik.pobierzWlasnaPlansze(),
            rozmiar = silnik.rozmiarPlanszy,
        )
    }
}
