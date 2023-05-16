package data.service

import data.model.Event
import data.model.Location
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId

fun sngScraper() {
    System.setProperty(
        "webdriver.chrome.driver",
        "C:\\Users\\User\\Desktop\\Materijali_2_letnik\\PrincipiPJ\\vaje\\projektna_vaja_1\\Podatki_PPJ_1\\src\\jvmMain\\kotlin\\data\\service\\chromedriver.exe"
    )
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
            val dateStr = element.attr("data-date")
            val format = SimpleDateFormat("yyyy-MM-dd")
            val date = format.parse(dateStr)
            val startDate = LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault())

            val location = Location("SNG", "Maribor", " Slovenska ulica 27")

            val event = Event(title, startDate, location)
            println(event)
        }
    }
    println("==============================================================")
}