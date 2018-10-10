(defproject waffletower/syringe "0.4.0-SNAPSHOT"
  :description "Clojure code suitable for dev profile injection"
  :url "https://github.com/waffletower/syringe"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [midje "1.9.1"]
                 [mvxcvi/puget "1.0.1"]]
  :deploy-repositories [["clojars-https" {:url "https://clojars.org/repo"
                                          :username :env/clojars_user
                                          :password :env/clojars_password}]]
  :profiles {:uberjar {:aot :all}
             :dev {:dependencies [[midje "1.9.1"]]
                   :plugins [[lein-midje "3.2.1"]]}})
