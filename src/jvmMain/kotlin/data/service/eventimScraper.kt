package data.service

import UI.HttpClient
import data.model.Event
import data.model.Location
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

fun eventimScraperEvents(): List<Event> {
    System.setProperty(
        "webdriver.chrome.driver",
        "C:\\Users\\User\\Desktop\\Materijali_2_letnik\\PrincipiPJ\\vaje\\projektna_vaja_1\\PrincipiPJ\\src\\jvmMain\\kotlin\\chromedriver.exe"
    )
    val events: MutableList<Event> = mutableListOf()
    val options = ChromeOptions()
    options.setHeadless(true)
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

    var descriptionAndImage: MutableMap<String, String> = mutableMapOf()
    for (element in document.select(".m-eventListItem")) {
        val title = element.selectFirst(".m-eventListItem__title")?.text()

        if (title != null) {
            val dateStr = (element.selectFirst("meta[itemprop='startDate']"))!!.attr("content")
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")
            val offsetDateTime = OffsetDateTime.parse(dateStr, formatter)
            val localDate = offsetDateTime.toLocalDate()
            val localTime = offsetDateTime.toLocalTime()
            val localDateTime = LocalDateTime.of(localDate, localTime)


            val institution = element.selectFirst(".m-eventListItem__venue")!!.text().split(',')[0]
            val city = element.selectFirst(".m-eventListItem__address")!!.text()
            val street = hashMapInstitutionLocation[institution]!!
            val location = Location(institution, city, street)
//            val lantLong = HttpClient.getLocationLatAndLong(location)
//            location.x = lantLong[0]
//            location.y = lantLong[1]

            val descImgPlace = element.selectFirst(".m-eventListItem")
            val descImgUrl = "https://www.eventim.si" + descImgPlace!!.attr("href")
            descriptionAndImage = eventimScraperDescriptionAndImage(descImgUrl)

            val descUrl = descriptionAndImage["desc"]
            val imageUrl = descriptionAndImage["image"]

            val event = Event(title, localDateTime, location, descUrl!!, imageUrl!!)
            events.add(event)
            println(event.toJson())
        }
    }
    return events
}

fun eventimScraperDescriptionAndImage(url: String): MutableMap<String, String> {
    val descriptionAndImage: MutableMap<String, String> = mutableMapOf()
    System.setProperty(
        "webdriver.chrome.driver",
        "C:\\Users\\User\\Desktop\\Materijali_2_letnik\\PrincipiPJ\\vaje\\projektna_vaja_1\\PrincipiPJ\\src\\jvmMain\\kotlin\\chromedriver.exe"
    )
    val options = ChromeOptions()
    options.setHeadless(true)
    val driver = ChromeDriver(options)
    driver.get(url)
    val pageSource = driver.pageSource
    driver.close()
    val document: Document = Jsoup.parse(pageSource)

    val descriptionEventDiv = document.selectFirst(".m-panel")
    val imageEventUrl = document.selectFirst(".img-responsive")

    descriptionAndImage["desc"] = if (descriptionEventDiv == null) {
        "There is not description for this event :("
    } else {
        descriptionEventDiv.text()
    }

    descriptionAndImage["image"] = if (imageEventUrl == null) {
        "https:" + document.selectFirst(".o-mainHeader__logoImg")!!.attr("src")
    } else {
        "https:" + imageEventUrl.attr("src")
    }

    return descriptionAndImage

}
