abstract class SilnikGry {
    abstract val rozmiarPlanszy: Int
    abstract fun oddajStrzal(pozycja: String): RezultatStrzalu
    abstract fun oddajStrzalKomputera(): Pair<String, RezultatStrzalu>
    abstract fun pobierzPlanszePrzeciwnika(): List<List<Char?>>
    abstract fun pobierzWlasnaPlansze(): List<List<Char>>
    abstract fun czyKoniecGry(): StanGry
    abstract fun rozmiescStatekGracza(statek: Statek, pozycjaStartowa: String, orientacja: Orientacja): Boolean
    abstract val statkiGracza: List<Statek>
    abstract val statkiKomputera: List<Statek>
    abstract var liczbaZatopionychStatkowGracza: Int
    abstract var liczbaZatopionychStatkowKomputera: Int
    abstract fun automatycznieRozmiescStatkiGracza()
}
