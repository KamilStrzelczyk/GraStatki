fun main() {
    val silnik = SilnikStatki()
    val widok = WidokKonsolowy(false)

    KontrolerGry(silnik, widok)
}
