package testavax

import groovyx.gpars.GParsPool
import org.jsoup.Jsoup

import java.util.stream.Collectors

class PageController {

    def pageService

    def index() {
        render (view: "index")
    }

    def urls() {
        def reports = pageService.getReports();
        def arrUrls = reports.arrUrls
        def arrCharCounts = reports.arrCharCounts
        def arrThreadCounts = reports.arrThreadCounts
        def arrProcTimes = reports.arrProcTimes
        render (contentType: 'application/json') {
            arrays(arrUrls: arrUrls, arrCharCounts: arrCharCounts,
                    arrThreadCounts: arrThreadCounts, arrProcTimes: arrProcTimes
            )
        }
    }

    def parse() {

        def start = System.currentTimeMillis()
        String pageUrl = params.pageUrl
        def doc = Jsoup.connect(pageUrl).get()
        def hrefs = doc.select("a[href]")
        def links = hrefs.stream()
                .map( {lnk -> lnk.attr("abs:href")})
                .map( {lnk -> removeAnchor(lnk)})
                .collect(Collectors.toList())

        def threadsCount = 4
        def outputMap =
            GParsPool.withPool( threadsCount ) {
                links.collect {
                    [it,
                     countCharactersInUrl(it)]
                }.stream().filter({k, v -> v > 0}).collect()
            }.collectEntries()
        def end = System.currentTimeMillis()
        def processingTimeMs = end - start
        pageService.saveReport pageUrl, outputMap, processingTimeMs, threadsCount
        render (view: "index", model: [outputMap : outputMap,
                                       minValue: outputMap.values().min(),
                                       maxValue: outputMap.values().max()])
    }

    def countCharactersInUrl(url) {
        try {
            return new URL(url).getText()
                    .chars()
                    .filter({ch -> Character.isLetter(ch)})
                    .count()
        }
        catch (Exception) {
            return 0
        }
    }

    def removeAnchor(String href) {
        int index = href.indexOf("#");
        if (index == -1) {
            return href;
        }
        return (href.substring(0, index));
    }
}
