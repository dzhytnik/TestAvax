package testavax

import groovy.sql.Sql

class BootStrap {

    def init = { servletContext ->
        initDatabase()
    }
    def destroy = {
    }

    def initDatabase() {
        def sql = Sql.newInstance("jdbc:h2:~/links", "sa", "sa", "org.h2.Driver")

        def createTblChildLinks = '''
            CREATE TABLE IF NOT EXISTS child_links (
              id INT PRIMARY KEY AUTO_INCREMENT,
              link VARCHAR(500),
              char_count INT
            )
            '''

        sql.execute(createTblChildLinks)

        def createTblReports = '''
            CREATE TABLE IF NOT EXISTS url_reports (
              id INT PRIMARY KEY AUTO_INCREMENT,
              url VARCHAR(500),
              sublinks_char_count BIGINT,
              created_at TIMESTAMP,
              processing_time_ms INT,
              threads_count INT
            )
            '''

        sql.execute(createTblReports)
    }
}
