(ns sabisu.conf
  (:require [clojure.spec.alpha :as s]
            [clojure.edn :as edn]
            [environ.core :refer [env]]
            [clojure.tools.logging :as ctl]
            [taoensso.timbre :as timbre]
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

(defn merge-options [options-spec defaults input-options {:keys [logging]}]
  (let [mo (merge defaults (read-vals input-options))]
    (if (s/valid? options-spec mo)
      mo
      (let [msg (format
                  "merged options do not adhere to options-spec: %s"
                  (s/explain-str options-spec mo))]
        (case logging
          :timbre (timbre/error msg)
          (ctl/error msg))))))

;; mockable environment accessor
(defn get-env []
  env)

(defn create-options [options-spec defaults config-map]
  (merge-options options-spec defaults (get-env) config-map))

(defmacro system-options
  "'system-options' is an initialization macro which generates specs for the options of a component system.
  it requires the following arguments:
  'system-name' a string or symbol used to create symbols for products of the macro (see below)
  'coll' a collection of three value collections [unqualified-key validation-function default-value]
  system-options will create the following:
  a map containing all options keys and default values '{system-name}-defaults,'
  individual specs for each option specified '{namespace-name}/{option-keyname}',
  a hashmap spec containing all options '{system-name}-spec',
  and a merged options map function '{system-name}-options' which is used to obtain system options
  resulting from the merge of defaults and environment variables"
  [system-name coll config-map]
  (let [sn (symbol system-name)
        ss (symbol (format "%s-spec" system-name))
        ds (symbol (format "%s-defaults" system-name))
        fs (symbol (format "%s-options" system-name))
        [option-keys defaults option-specs]
        (rotate
          (map
            (fn [[option-sym valid-fn default]]
              (let [unqualified (keyword (name option-sym))
                    qualified (keyword (str *ns*) (name option-sym))]
                [qualified [unqualified default] `(s/def ~qualified ~valid-fn)]))
            coll))]
    `(do
       ~@option-specs
       (declare ~sn)
       (declare ~ss)
       (declare ~ds)
       (declare ~fs)
       (def ~ss (s/spec (s/keys :req-un [~@option-keys])))
       (def ~ds (hash-map ~@(mapcat identity defaults)))
       (defn ~fs [] (create-options ~ss ~ds ~config-map))
       nil)))
