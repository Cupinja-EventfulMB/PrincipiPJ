package data.service

import data.model.Event
import data.model.Location
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun sngScraper(): List<Event>{
    System.setProperty(
        "webdriver.chrome.driver",
        "C:\\Users\\User\\Desktop\\Materijali_2_letnik\\PrincipiPJ\\vaje\\projektna_vaja_1\\PrincipiPJ\\src\\jvmMain\\kotlin\\chromedriver.exe"
    )
    val events: MutableList<Event> = mutableListOf()
    val options = ChromeOptions()
    //options.setHeadless(true)
    val driver = ChromeDriver(options)
    val url = "https://www.sng-mb.si/pogramme/?lang=en"
    driver.get(url)
    val pageSource = driver.pageSource
    driver.close()
    val document: Document = Jsoup.parse(pageSource)

    println("====================== Data from SNG Maribor ======================")

    for (element in document.select("td.fc-daygrid-day")) {
        val title = element.selectFirst(".title")?.text()

        if (title != null) {
            val time = element.selectFirst(".time")?.text()
            val dateStr =
                if (time.isNullOrBlank()) element.attr("data-date") + "T00:00" else element.attr("data-date") + "T${
                    time.replace(
                        '.',
                        ':'
                    )
                }"
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
            val localDateTime = LocalDateTime.parse(dateStr, formatter)

            val location = Location("SNG", "Maribor", " Slovenska ulica 27")

            val event = Event(title, localDateTime, location)
            events.add(event)
            println(event)
        }
    }
    return events
}