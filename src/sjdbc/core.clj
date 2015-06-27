(ns sjdbc.core
  (:require [clojure.java.jdbc :as jdbc])
  (:import (javax.sql DataSource)
           (com.jolbox.bonecp BoneCPDataSource)
           (java.io Closeable)
           (java.sql Connection ResultSet Statement)))

;;use a Closeable defrecord to allow for usage with-open and other types of resource closing functions
(defrecord Conn [^DataSource datasource]
  Closeable
  (close [_] (.close datasource)))

(declare get-pooled-connection)

(defn open
  ([jdbc-url options]
    (open jdbc-url nil nil options))
  ([jdbc-url user pwd options]
   {:pre [(string? jdbc-url) (map? options)]}
   (->Conn (get-pooled-connection jdbc-url user pwd options))))


(defn close [{:keys [^DataSource datasource]}]
  {:pre [datasource]}
  (.close datasource))

(defn
  query
  "Return a sequence with each item as a map and the keys are equal to the column names
   If "
  ([conn sql]
    (query conn sql identity))
  ([conn sql row-f]
    (jdbc/query conn [sql] :row-fn row-f))
  ([conn sql row-f {:keys [fetch-size] :or {fetch-size 100000}}]
   (with-open [stmt (jdbc/prepare-statement conn sql :fetch-size fetch-size)]
     (jdbc/query conn [stmt] :row-fn row-f))))

(defn query-with-rs
  "Use for large queries, calls the function f with (f rs:ResultSet)"
  [conn sql f]
  (jdbc/db-query-with-resultset conn [sql] f))


(defn query-streaming-rs
  "Used for mysql when the data set expected is huge, by default mysql will load all the resultsets into memory first
   then respond, but on large datasets this is not efficient, see http://dev.mysql.com/doc/connector-j/en/connector-j-reference-implementation-notes.html.
   This implementation will call the function f with the ResultSet, each record needs to be read with the shortest delay possible"
  [{:keys [^DataSource datasource]} sql f]
  (with-open [^Connection conn (.getConnection datasource)]
    (with-open [^Statement stmt (.createStatement conn ResultSet/TYPE_FORWARD_ONLY ResultSet/CONCUR_READ_ONLY)]
      (.setFetchSize stmt Integer/MIN_VALUE)
      (with-open [^ResultSet rs (.executeQuery stmt sql)]
        (f rs)))))

(defn
  exec
  "Perform a non select SQL operation, optionaly you can use the sql parameter syntax e.g for inserts
    (exec conn \"INSERT INTO test (id, name) VALUES(?, ?)\" 1 \"abc\")"
  ([conn sql]
    (jdbc/execute! conn [sql]))
  ([conn sql & params]
    (jdbc/execute! conn (cons sql params))))

(defn- get-pooled-connection [^String jdbc-url ^String user ^String pwd {:keys [partition-size min-pool-size max-pool-size] :or {partition-size 2 min-pool-size 1 max-pool-size 10}}]
  (doto
    (BoneCPDataSource.)
    (.setJdbcUrl jdbc-url)
    (.setUser user)
    (.setPassword pwd)
    (.setPartitionCount (int partition-size))
    (.setMaxConnectionsPerPartition (int max-pool-size))
    (.setMinConnectionsPerPartition (int min-pool-size))))