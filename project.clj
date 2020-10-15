(defproject waffletower/syringe "0.4.0"
  :description "Clojure code suitable for dev profile injection"
  :url "https://github.com/waffletower/syringe"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [mvxcvi/puget "1.3.1"]
                 [environ "1.2.0"]]
  :deploy-repositories [["clojars-https" {:url "https://clojars.org/repo"
                                          :username :env/clojars_user
                                          :password :env/clojars_password}]])
