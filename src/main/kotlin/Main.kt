fun main() {
    val silnik = SilnikStatki()
    val widok = WidokKonsolowy(
        silnik = silnik,
        uzywajKolorow = false,
    )

    Rozgrywka(silnik, widok).przeprowadzRozgrywke()
}
