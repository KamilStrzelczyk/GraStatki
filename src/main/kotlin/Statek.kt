data class Statek(val dlugosc: Int) {
    val pola = mutableListOf<Pair<Int, Int>>()
    val trafienia = mutableListOf<Pair<Int, Int>>()
    private var czyZatopiony = false

    fun czyZatopiony(): Boolean {
        czyZatopiony = trafienia.size == dlugosc
        return czyZatopiony
    }
}
