fun main() {
    val silnik = SilnikStatki()
    val widok = WidokKonsolowy(silnik)

    Rozgrywka(silnik, widok).przeprowadzRozgrywke()
}
