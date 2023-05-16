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

fun eventimScraper() {
    System.setProperty(
        "webdriver.chrome.driver",
        "C:\\Users\\User\\Desktop\\Materijali_2_letnik\\PrincipiPJ\\vaje\\projektna_vaja_1\\Podatki_PPJ_1\\src\\jvmMain\\kotlin\\data\\service\\chromedriver.exe"
    )
    val options = ChromeOptions()
    //  options.setHeadless(true)
    val driver = ChromeDriver(options)
    val url = "https://www.eventim.si/si/venues/maribor/city.html"
    driver.get(url)
    val pageSource = driver.pageSource
    driver.close()
    val document: Document = Jsoup.parse(pageSource)

    println("====================== Data from Eventim ======================")

    val hashMapInstitutionLocation: MutableMap<String, String> = mutableMapOf()
    for (element in document.select(".js-modulehelper--city--venueList-venue")) {
        val nameInstitution = element.selectFirst(".m-searchListItem__title")!!.text().split(',')[0]
        val street = element.selectFirst(".m-searchListItem__venue")!!.text()

        hashMapInstitutionLocation[nameInstitution] = street
    }

    for (element in document.select(".m-eventListItem")) {
        val title = element.selectFirst(".m-eventListItem__title")?.text()

        if (title != null) {
            val dateStr = (element.selectFirst("meta[itemprop='startDate']"))!!.attr("content")
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
            val date = format.parse(dateStr)
            val startDate = LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault())

            val institution = element.selectFirst(".m-eventListItem__venue")!!.text().split(',')[0]
            val city = element.selectFirst(".m-eventListItem__address")!!.text()
            val street = hashMapInstitutionLocation[institution]!!
            val location = Location(institution, city, street)

            val event = Event(title, startDate, location)
            println(event)
        }
    }
    println("==============================================================")
}
