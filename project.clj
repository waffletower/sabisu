(defproject waffletower/sabisu "1.0.0"
  :description "micro-service configuration library"
  :url "https://github.com/waffletower/sabisu"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/spec.alpha "0.3.218"]
                 [org.clojure/tools.logging "1.2.4"]
                 [com.taoensso/timbre "6.0.1"]
                 [environ "1.2.0"]
                 [waffletower/serum "0.9.1"]]
  :deploy-repositories [["clojars-https" {:url "https://clojars.org/repo"
                                          :username :env/clojars_user
                                          :password :env/clojars_password}]]
  :profiles {:uberjar {:aot :all}
             :dev {:dependencies [[midje "1.9.9"]]
                   :plugins [[lein-midje "3.2.1"]]}})
