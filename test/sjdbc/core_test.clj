(ns sjdbc.core-test
  (:require [clojure.test :refer :all]
            [sjdbc.core :as sjdbc]))


;; load the db driver
(import '(org.hsqldb jdbcDriver))

(let [conn (sjdbc/open "jdbc:hsqldb:mem:mymemdb" "SA" "" {})]

  (deftest test-conn-exec-query []
                             (sjdbc/exec conn "CREATE TABLE test(id int, name varchar(200))")
                             (sjdbc/exec conn "INSERT INTO test (id, name) VALUES(?, ?)" 1 "abc")
                             (is (count (sjdbc/query conn "SELECT * FROM test")) 1)
                             (is (:name (first (sjdbc/query conn "SELECT * FROM test"))) "abc")
                             (is (:id (first (sjdbc/query conn "SELECT * FROM test"))) 1)))