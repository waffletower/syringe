(defproject waffletower/syringe "1.1.0"
  :description "Clojure code suitable for dev profile injection"
  :url "https://github.com/waffletower/syringe"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [org.clojure/java.classpath "1.0.0"]
                 [org.clojure/tools.namespace "1.4.4"]
                 [mvxcvi/puget "1.3.2"]]
  :deploy-repositories [["clojars-https" {:url "https://clojars.org/repo"
                                          :username :env/clojars_user
                                          :password :env/clojars_password}]])
