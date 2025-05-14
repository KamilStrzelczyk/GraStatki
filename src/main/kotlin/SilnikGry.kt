interface SilnikGry {
    val rozmiarPlanszy: Int
    fun rozpocznijNowaGre()
    fun oddajStrzal(pozycja: String): RezultatStrzalu
    fun oddajStrzalKomputera(): Pair<String, RezultatStrzalu>
    fun pobierzPlanszePrzeciwnika(): List<List<Char?>>
    fun pobierzWlasnaPlansze(): List<List<Char>>
    fun czyKoniecGry(): Boolean
    fun rozmiescStatekGracza(statek: Statek, pozycjaStartowa: String, orientacja: Orientacja): Boolean
    val statkiGracza: List<Statek>
    val statkiKomputera: List<Statek>
    var liczbaZatopionychStatkowGracza: Int
    var liczbaZatopionychStatkowKomputera: Int
    fun automatycznieRozmiescStatkiGracza()
}
