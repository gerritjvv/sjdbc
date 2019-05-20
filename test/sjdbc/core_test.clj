(ns sjdbc.core-test
  (:require [clojure.test :refer :all]
            [sjdbc.core :as sjdbc])
  (:import (java.sql ResultSet)
           (org.hsqldb jdbcDriver)))


;; load the db driver


(let [conn (sjdbc/open "jdbc:hsqldb:mem:mymemdb" "SA" "" {})]

  (deftest test-conn-exec-query []
                             (Class/forName "org.hsqldb.jdbcDriver" )

                             (sjdbc/exec conn "CREATE TABLE test(id int, name varchar(200))")
                             (sjdbc/exec conn "INSERT INTO test (id, name) VALUES(?, ?)" 1 "abc")
                             (is (count (sjdbc/query conn "SELECT * FROM test")) 1)
                             (is (:name (first (sjdbc/query conn "SELECT * FROM test"))) "abc")
                             (is (:id (first (sjdbc/query conn "SELECT * FROM test"))) 1)
                             (let [counter (atom 0)]
                               (sjdbc/query-with-rs conn "SELECT * FROM test" (fn [^ResultSet rs]
                                                                                   (.setFetchSize rs (int 10))
                                                                                   (while (.next rs)
                                                                                     (swap! counter inc))))
                               (is (= @counter 1)))
                             (let [counter (atom 0)]
                                  (sjdbc/query-streaming-rs conn "SELECT * FROM test"
                                                            (fn [^ResultSet rs]
                                                              (while (.next rs)
                                                                (swap! counter inc))))
                                  (is (= @counter 1)))))
