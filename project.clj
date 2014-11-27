(defproject psl-upload "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.2.0"]
                 [ring/ring-defaults "0.1.2"]
                 [ring/ring-json "0.3.1"]
                 [org.clojure/java.jdbc "0.3.6"]
                 [org.xerial/sqlite-jdbc "3.7.2"]
                 [hiccup "1.0.5"]
                 [markdown-clj "0.9.58" :exclusions [org.clojure/clojure]]]
  :plugins [[lein-ring "0.8.13"]]
  :ring {:handler psl-upload.core.handler/app
         :nrepl {:start? true :port 5555}}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}})
