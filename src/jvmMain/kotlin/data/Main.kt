package scraping

import it.skrape.core.htmlDocument
import it.skrape.fetcher.extractIt
import it.skrape.fetcher.skrape
import it.skrape.selects.and
import it.skrape.selects.eachText
import it.skrape.selects.html5.div
import it.skrape.selects.html5.a
import it.skrape.selects.html5.td
import it.skrape.fetcher.HttpFetcher
import java.util.Date
import it.skrape.fetcher.response
import it.skrape.selects.html5.article
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import java.io.FileOutputStream

data class Event(
    val title: String,
    val date: String,
    val location: String
)

fun kulturnikScraper() {
    System.setProperty("webdriver.chrome.driver", "C:\\Users\\User\\Desktop\\Materijali_2_letnik\\PrincipiPJ\\vaje\\projektna_vaja_1\\Podatki_PPJ_1\\src\\jvmMain\\kotlin\\data\\chromedriver.exe")
    val options = ChromeOptions()
    options.setHeadless(true)
    val driver = ChromeDriver(options)
    val url = "https://dogodki.kulturnik.si/?where=Podravje"
    driver.get(url)
    val pageSource = driver.pageSource
    driver.close()
    val document: Document = Jsoup.parse(pageSource)
    for (element in document.select(".labels")) {
        val title = element.selectFirst(".url")?.text()
       // val date = element.attr(".dtstart")
        val location = element.selectFirst(".row atom_city")?.text()
        if (title != null) {
            println("$title, $location")
        }
    }
}

fun sngScraper() {
    System.setProperty("webdriver.chrome.driver", "C:\\Users\\User\\Desktop\\Materijali_2_letnik\\PrincipiPJ\\vaje\\projektna_vaja_1\\Podatki_PPJ_1\\src\\jvmMain\\kotlin\\data\\chromedriver.exe")
    val options = ChromeOptions()
    options.setHeadless(true)
    val driver = ChromeDriver(options)
    val url = "https://www.sng-mb.si/pogramme/?lang=en"
    driver.get(url)
    val pageSource = driver.pageSource
    driver.close()
    val document: Document = Jsoup.parse(pageSource)
    for (element in document.select("td.fc-daygrid-day")) {
        val title = element.selectFirst(".title")?.text()
        val date = element.attr("data-date")
        val location = element.selectFirst(".location")?.text()
        if (title != null) {
            println("$title, $location, $date")
        }
    }
}



fun main() {
    kulturnikScraper()
    println("======================================================================================")
    sngScraper()
}

