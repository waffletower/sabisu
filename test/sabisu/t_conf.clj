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
                       :murlocs "17"}]
              :sartre []}) => {:url "https://mymmo.com"
                               :type :sandbox
                               :mobs 645
                               :lairs [{:where "dungeon"
                                        :hobgoblins 11}
                                       {:where "underground"
                                        :murlocs 17}]
                               :sartre []})

(s/def ::dunes integer?)
(s/def ::bunnies integer?)
(s/def ::raisinets integer?)

(let [spec (s/spec
            (s/keys :req-un [::dunes ::bunnies ::raisinets]))
      ds {:dunes 1977
          :bunnies 5000
          :raisinets 5000}]
  (facts "merge-options"
    (fact "reject non-integers for integer values"
      (merge-options spec ds {:dunes "boullion-cube"} {}) => nil
      (merge-options spec ds {:bunnies "barnacle"} {}) => nil
      (merge-options spec ds {:raisinets "bionic"} {}) => nil)

    (fact "map precedence left to right as in merge"
      (:dunes (merge-options spec ds {:dunes "4242"} {})) => 4242
      (:bunnies (merge-options spec ds {:bunnies "1000"} {})) => 1000
      (:raisinets (merge-options spec ds {:raisinets "2000"} {})) => 2000)

    (fact "lack of environment does not modify result"
      (:dunes (merge-options spec (assoc ds :dunes 4242) {} {})) => 4242)))

(facts "system-options"
  (macroexpand
   '(system-options
     dante
     [[:worker string? "Ralph"]
      [:tool string? "Hose"]
      [:shed coll? ["Maul"]]
      [:box coll? []]]
     {}))
  => '(do
        (clojure.spec.alpha/def :sabisu.t-conf/worker string?)
        (clojure.spec.alpha/def :sabisu.t-conf/tool string?)
        (clojure.spec.alpha/def :sabisu.t-conf/shed coll?)
        (clojure.spec.alpha/def :sabisu.t-conf/box coll?)
        (clojure.core/declare dante)
        (clojure.core/declare dante-spec)
        (clojure.core/declare dante-defaults)
        (clojure.core/declare dante-options)
        (def dante-spec
          (clojure.spec.alpha/spec
           (clojure.spec.alpha/keys
            :req-un
            [:sabisu.t-conf/worker :sabisu.t-conf/tool :sabisu.t-conf/shed :sabisu.t-conf/box])))
        (def dante-defaults (clojure.core/hash-map :worker "Ralph" :tool "Hose" :shed ["Maul"] :box []))
        (clojure.core/defn
          dante-options
          []
          (sabisu.conf/create-options dante-spec dante-defaults {}))
        nil)

  (system-options
   dante
   [[:worker string? "Ralph"]
    [:tool string? "Hose"
     :count int? 11]]
   {}) => nil
  (:worker dante-defaults) => "Ralph"
  (:tool dante-defaults) => "Hose"
  (dante-options) => {:worker "Ralph"
                      :tool "bag-of-tricks"
                      :count 12}
  (provided
   (get-env) => {:tool "bag-of-tricks"
                 :count "12"})
  (:name (meta #'dante-options)) => 'dante-options)
