(ns sabisu.conf
  (:require [clojure.spec.alpha :as s]
            [clojure.edn :as edn]
            [environ.core :refer [env]]
            [taoensso.timbre :as log]
            [serum.core :refer [attempt]]
            [serum.data :refer [proc-vals rotate]]))

(defn read-string-with-pred
  [pred s]
  (if (string? s)
    (let [ps (attempt (edn/read-string s) (fn [_] s))]
      (if (pred ps) ps s))
    s))

(defn read-string-not-symbols [s]
  (read-string-with-pred (comp not symbol?) s))

(defn read-vals [m]
  (proc-vals read-string-not-symbols m))
