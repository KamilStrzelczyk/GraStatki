abstract class WidokGry {
    abstract fun wyswietlPowitanie()
    abstract fun wyswietlRozpoczecieGry()
    abstract fun wyswietlPlansze(plansza: String)
    abstract fun pobierzRuchGracza(): String
    abstract fun wyswietlRezultatStrzalu(rezultat: RezultatStrzalu)
    abstract fun wyswietlKomunikatKoncaGry(stanGry: StanGry)
    abstract fun pobierzDaneStatku(numerStatku: Int, dostepnePola: List<String>): Pair<String, Orientacja>?
    abstract fun wyswietlBladRozmieszczeniaStatku()
    abstract fun czyscEkran(czekajUzytkownika: Boolean = false)
    abstract fun wyswietlPlansze(plansza: List<List<Char?>>)
    abstract fun wyswietlRuchKomputera(pozycja: String)
    abstract fun czyAutomatycznieRozmiescicStatki(): Boolean
    abstract fun wyswietlDwiePlansze(
        planszaPrzeciwnika: List<List<Char?>>,
        planszaGracza: List<List<Char>>,
        rozmiar: Int
    )
}
