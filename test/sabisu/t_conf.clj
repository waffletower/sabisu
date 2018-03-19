(ns sabisu.t-conf
  (:require [clojure.spec.alpha :as s]
            [sabisu.conf :refer :all]
            [midje.sweet :refer :all]))

(fact "read-string-not-symbols"
  (read-string-not-symbols "3.14") => (roughly 3.14)
  (read-string-not-symbols "http://newdonk.net/") => "http://newdonk.net/"
  (read-string-not-symbols "2005") => 2005
  (read-string-not-symbols "map") => "map")

(fact "read-vals"
  (read-vals {:url "https://mymmo.com"
              :type :sandbox
              :mobs "645"
              :lairs [{:where "dungeon"
                       :hobgoblins "11"}
                      {:where "underground"
                       :murlocs "17"}]}) => {:url "https://mymmo.com"
                                             :type :sandbox
                                             :mobs 645
                                             :lairs [{:where "dungeon"
                                                      :hobgoblins 11}
                                                     {:where "underground"
                                                      :murlocs 17}]})
