@file:Suppress("NAME_SHADOWING")

package data.service

import UI.HttpClient
import data.model.Event
import data.model.Location
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


fun sngScraperEvents(): List<Event> {
    System.setProperty(
        "webdriver.chrome.driver",
        "C:\\Users\\User\\Desktop\\Materijali_2_letnik\\PrincipiPJ\\vaje\\projektna_vaja_1\\PrincipiPJ\\src\\jvmMain\\kotlin\\chromedriver.exe"
    )
    val events: MutableList<Event> = mutableListOf()
    val options = ChromeOptions()
    options.setHeadless(true)
    val driver = ChromeDriver(options)
    val url = "https://www.sng-mb.si/pogramme/?lang=en"
    driver.get(url)
    val pageSource = driver.pageSource
    driver.close()
    val document: Document = Jsoup.parse(pageSource)

    println("====================== Data from SNG Maribor ======================")
    val imageUrl = "https://www.sng-mb.si/content/themes/default/assets/images/sng-full-logo.svg"
    var descriptionAndImage: MutableMap<String, String> = mutableMapOf()

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
//            val lantLong = HttpClient.getLocationLatAndLong(location)
//            location.x = lantLong[0]
//            location.y = lantLong[1]

            val descPlace = element.selectFirst(".calendar-event")
            val descriptionUrl = descPlace!!.attr("href")
            descriptionAndImage = sngScraperDescriptionAndImage(descriptionUrl)

            val descUrl = descriptionAndImage["desc"]
            val imageUrl = descriptionAndImage["image"]

            val event = Event(title, localDateTime, location, descUrl!!, imageUrl!!)
            events.add(event)
            println(event.toJson())
        }
    }
    return events
}

fun sngScraperDescriptionAndImage(url: String): MutableMap<String, String> {
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

    val descriptionEventDiv = document.selectFirst(".opis_title")!!.text()
    var imgEventUrl = document.selectFirst(".header.simple")!!.attr("style").split('(').last()
    imgEventUrl = imgEventUrl.substring(1, imgEventUrl.length - 2)

    descriptionAndImage["desc"] = descriptionEventDiv
    descriptionAndImage["image"] = imgEventUrl

    return descriptionAndImage
}
