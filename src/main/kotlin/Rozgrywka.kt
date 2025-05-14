import RezultatStrzalu.PUDLO

class Rozgrywka(private val silnik: SilnikGry, private val widok: WidokGry) {

    fun przeprowadzRozgrywke() {
        przygotujStatkiGracza()
        widok.wyswietlRozpoczecieGry()

        while (true) {
            widok.czyscEkran(true)
            widok.wyswietlDwiePlansze(silnik.pobierzPlanszePrzeciwnika(), silnik.pobierzWlasnaPlansze())

            if (!ruchGracza()) break
            if (czyGraZakonczona()) break

            widok.czyscEkran(true)
            widok.wyswietlDwiePlansze(silnik.pobierzPlanszePrzeciwnika(), silnik.pobierzWlasnaPlansze())

            if (!ruchKomputera()) break
            if (czyGraZakonczona()) break
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

    private fun czyGraZakonczona(): Boolean {
        if (silnik.czyKoniecGry()) {
            widok.czyscEkran()
            widok.wyswietlDwiePlansze(silnik.pobierzPlanszePrzeciwnika(), silnik.pobierzWlasnaPlansze())
            widok.wyswietlKomunikatKoncaGry()
            return true
        }
        return false
    }

    private fun przygotujStatkiGracza() {
        widok.wyswietlPowitanie()
        val czyAuto = widok.pobierzCzyAutomatycznieRozmiescicStatki()
        if (czyAuto) {
            silnik.automatycznieRozmiescStatkiGracza()
            widok.czyscEkran()
            widok.wyswietlPlansze(silnik.pobierzWlasnaPlansze())
        } else {
            rozmiescStatkiRecznie()
        }
    }

    private fun rozmiescStatkiRecznie() {
        val konfiguracjaStatkow = listOf(4, 3, 3, 2, 2, 2, 1, 1, 1, 1)
        val dostepnePola = ('A'..'J').flatMap { kol -> (1..10).map { wiersz -> "$kol$wiersz" } }

        konfiguracjaStatkow.forEachIndexed { index, dlugosc ->
            val statek = Statek(dlugosc)
            while (true) {
                val dane = widok.pobierzDaneStatku(index + 1, dostepnePola) ?: continue
                val (pozycja, orientacja) = dane
                if (silnik.rozmiescStatekGracza(statek, pozycja, orientacja)) {
                    widok.czyscEkran()
                    widok.wyswietlPlansze(silnik.pobierzWlasnaPlansze())
                    break
                } else {
                    widok.wyswietlBladRozmieszczeniaStatku()
                }
            }
        }
    }
}
