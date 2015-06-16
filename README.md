# sjdbc

A clojure jdbc library that doesn't get in your way, and that defaults to using connection pooling.


[![Clojars Project](http://clojars.org/sjdbc/latest-version.svg)](http://clojars.org/sjdbc)

## Usage

### Connect

```clojure

(require '[sjdbc.core :as sjdbc])

(def conn (sjdbc/open "jdbc url" pool-options))
(jsdbc/query conn "select * from testtable")
  
;; or with authentication
(def conn (sjdbc/open "jdbc url" "user" "password" pool-options))
(jsdbc/query conn "select * from testtable")

;;close the connection pool when no more queries are going to be made
(jsdbc/close conn)
  

```


### Connection Pooling

DB access should always be performed using db pooling, this is way sjdbc has it backed in as standard and 
will always use a pooled connection.


## License

Copyright © 2015 gerritjvv

Distributed under the Eclipse Public License either version 1.0
