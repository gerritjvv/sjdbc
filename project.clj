(defproject sjdbc "0.1.6"
            :description "Very simple JDBC library allows you to write simple SQL as is and send to jdbc, all connections are pooled by default."
            :url "https://github.com/gerritjvv/sjdbc"
            :license {:name "Eclipse Public License"
                      :url  "http://www.eclipse.org/legal/epl-v10.html"}
            :javac-options ["-target" "1.7" "-source" "1.7" "-Xlint:-options"]

            :eval-in :leiningen
            :plugins [[lein-ancient "0.6.15"]]

            :resource-paths ["lib/RedshiftJDBC41-1.1.2.0002.jar"]
            :dependencies [
			                     [com.jolbox/bonecp "0.8.0.RELEASE"]
                           [org.clojure/java.jdbc "0.7.9"]
                           [hsqldb/hsqldb "1.8.0.10" :scope "test"]
                           [org.clojure/clojure "1.10.0"]])
