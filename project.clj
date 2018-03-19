(defproject waffletower/sabisu "0.1.0"
  :description "micro-service configuration library"
  :url "https://github.com/waffletower/sabisu"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/spec.alpha "0.1.143"]
                 [environ "1.1.0"]
                 [com.taoensso/timbre "4.10.0"]
                 [waffletower/serum "0.3.0"]]
  :deploy-repositories [["clojars-https" {:url "https://clojars.org/repo"
                                          :username :env/clojars_user
                                          :password :env/clojars_password}]]
  :profiles {:uberjar {:aot :all}
             :dev {:dependencies [[midje "1.9.0"]]
                   :plugins [[lein-midje "3.2.1"]]}})
