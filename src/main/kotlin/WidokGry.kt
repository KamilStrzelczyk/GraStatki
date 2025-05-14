interface WidokGry {
    fun wyswietlPowitanie()
    fun wyswietlRozpoczecieGry()
    fun wyswietlPlansze(plansza: String)
    fun wyswietlDwiePlansze(planszaPrzeciwnika: List<List<Char?>>, planszaGracza: List<List<Char>>)
    fun pobierzRuchGracza(): String
    fun wyswietlRezultatStrzalu(rezultat: RezultatStrzalu)
    fun wyswietlKomunikatKoncaGry()
    fun pobierzDaneStatku(numerStatku: Int, dostepnePola: List<String>): Pair<String, Orientacja>?
    fun wyswietlBladRozmieszczeniaStatku()
    fun czyscEkran(czekajUzytkownika: Boolean = false)
    fun wyswietlPlansze(plansza: List<List<Char?>>)
    fun wyswietlRuchKomputera(pozycja: String)
    fun pobierzCzyAutomatycznieRozmiescicStatki(): Boolean
}
