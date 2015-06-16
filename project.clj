(defproject sjdbc "0.1.0-SNAPSHOT"
            :description "FIXME: write description"
            :url "http://example.com/FIXME"
            :license {:name "Eclipse Public License"
                      :url  "http://www.eclipse.org/legal/epl-v10.html"}
            :javac-options ["-target" "1.7" "-source" "1.7" "-Xlint:-options"]

            :eval-in :leiningen

            :resource-paths ["lib/RedshiftJDBC41-1.1.2.0002.jar"]
            :dependencies [
                           [com.jolbox/bonecp "0.8.0.RELEASE"]
                           [org.clojure/java.jdbc "0.3.6"]
                           [hsqldb/hsqldb "1.8.0.10" :scope "test"]
                           [org.clojure/clojure "1.7.0-RC1"]])
