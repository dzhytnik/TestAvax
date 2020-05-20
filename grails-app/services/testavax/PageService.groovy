package testavax

import grails.gorm.transactions.Transactional
import groovy.sql.Sql

import java.time.LocalDateTime

@Transactional
class PageService {

    def saveReport(String url, Map map, long processingTimeMs, int threadsCount) {
        def sql = Sql.newInstance("jdbc:h2:~/links", "sa", "sa", "org.h2.Driver")


        def sqlString = '''
                INSERT INTO child_links (link, char_count) VALUES (:link, :charCount)
            '''

        sql.withBatch (20, sqlString) { ps ->
            map.each { k,v -> ps.addBatch(link: k, charCount: v)}
        }

        sqlString = '''
                INSERT INTO url_reports (url, sublinks_char_count, created_at, processing_time_ms, threads_count)
                VALUES (?, ?, ?, ?, ?)
            '''

        sql.execute sqlString, [url, map.values().sum(), LocalDateTime.now(), processingTimeMs, threadsCount]

    }

    def getReports() {
        def sql = Sql.newInstance("jdbc:h2:~/links", "sa", "sa", "org.h2.Driver")

        def arrUrls = []
        def arrCharCounts = []
        def arrThreadCounts = []
        def arrProcTimes = []
        sql.eachRow('SELECT * FROM url_reports') { row ->
            arrUrls.add("$row.url")
            arrThreadCounts.add("$row.threads_count" as int)
            arrProcTimes.add("$row.processing_time_ms" as int)
            arrCharCounts.add("$row.sublinks_char_count" as int)

        }
        return [arrUrls: arrUrls, arrCharCounts: arrCharCounts, arrThreadCounts: arrThreadCounts,
                arrProcTimes: arrProcTimes]
    }


}
