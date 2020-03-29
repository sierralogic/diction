(ns diction.util
  (:require [cheshire.core :as cheshire]
            [cheshire.generate :refer [add-encoder encode-str remove-encoder]]
            [clj-time.core :as t]
            [clojure.string :as str]
            [clojure.walk :as walk])
  (:import (java.util Random UUID)))

(def json-encode-to-string-classes
  [java.util.regex.Pattern
   org.joda.time.DateTime])

(def add-encoders-result (reduce #(conj %
                                        (add-encoder %2
                                                     (fn [c jsonGenerator]
                                                       (.writeString jsonGenerator (str c)))))
                                 nil
                                 json-encode-to-string-classes))

(def joda-class (class (t/now)))

(defn polite-assoc
  "Politely associates in map `m` with key `k` the value `v` iff
  both the key `k` and `v` are non-nil AND there is no existing value
  in the map `m` located at key `k`."
  [m k v]
  (try
    (if (and (not (nil? k)) (not (nil? v)))
      (if (nil? (get m k))
        (assoc m k v)
        m)
      m)
    (catch Exception _
      m)))

(defn polite-assoc-in
  "Politely associates in map `m` with nested keys `ks` the value `v` iff
  both the keys `ks` and `v` are non-nil AND there is no existing value
  in the map `m` located at the keys `ks`."
  [m ks v]
  (try
    (if (and (not (empty? ks)) (not (nil? v)))
      (if (nil? (get-in m ks))
        (assoc-in m ks v)
        m)
      m)
    (catch Exception _
      m)))

(defn strict-int?
  "Determines if `x` is a strict `int`."
  [x]
  (instance? Integer x))

(defn strict-long?
  "Determines if `x` is strict `long`."
  [x]
  (instance? Long x))

(defn strict-float?
  "Determines if `x` is a strict `float`."
  [x]
  (instance? Float x))

(defn strict-double?
  "Determines if `x` is a strict `double`."
  [x]
  (instance? Double x))

(defn joda?
  "Determine if `x` is a Joda DateTime instance."
  [x]
  (when x
    (= joda-class (class x))))

(defn vls?
  "Determines if `x` is a vector, set, or list."
  [x]
  (or (sequential? x) (set? x)))

(defn stack-assoc
  "Associates key `k` and value `v` to map `m`."
  [m k v]
  (assoc m k (if-let [cv (get m k)]
               (if (vls? cv)
                 (conj cv v)
                 [cv v])
               [v])))

(defn ->str
  "Convert `x` to string."
  [x]
  (when x
    (if (keyword? x)
      (-> x str (subs 1))
      (str x))))

(defn ->kw
  "Convert `xs to keyword."
  [x]
  (if (keyword? x)
    x
    (-> x
        str
        keyword)))

(def rndm (Random.))

(defn ->json
  "Convert map `m` to JSON string."
  [m]
  (cheshire/generate-string m {:pretty true}))

(defn ->edn
  "Convert JSON string `s` to map."
  [s]
  (when s
    (cheshire/parse-string s true)))

(defn apply-f-to-keys
  "Recursively transforms all map keys from keywords to strings."
  {:added "1.1"}
  [f m]
  (walk/postwalk (fn [x] (if (map? x) (into {} (map f x)) x)) m))

(defn ->snake
  "Convert `s` to snake case."
  [s]
  (-> s
      ->str
      str/lower-case
      (str/replace "-" "_")))

(defn ->skewer
  "Convert `s` to skewer case."
  [s]
  (-> s
      ->str
      str/trim
      str/lower-case
      (str/replace " " "-")
      (str/replace "_" "-")))

(defn ->skewer-kw
  "Converts `x` to skewer keyword."
  [x]
  (-> x
      ->skewer
      keyword))

(defn walk-apply-key-f
  "Apply key function `kv` to key-value pair `kv`.  Used to walk map."
  [kf kv]
  (let [[k v] kv]
    [(kf k) v]))

(def snakify-keys (partial walk-apply-key-f ->snake))
(def skewer-keys (partial walk-apply-key-f ->skewer-kw))

(def snake-keys (partial apply-f-to-keys snakify-keys))
(def skewer-keys (partial apply-f-to-keys skewer-keys))


(def default-label-replacements {"Id" "ID"
                                 "Ids" "IDs"
                                 "Ssn" "SSN"
                                 "Num" "No."
                                 "Cd" "Code"
                                 "Svc" "Service"
                                 "Pct" "Percent"
                                 "Roi" "ROI"
                                 "Mo" "Month"
                                 "Yr" "Year"
                                 "Wk" "Week"
                                 "Loe" "LOE"
                                 "No" "No."
                                 "Ein" "EIN"
                                 "Required Un" "Required (Unqualified)"
                                 "Optional Un" "Optional (Unqualified)"})

(def label-replacements (atom default-label-replacements))

(defn label-replacements!
  "Sets the label replacements with `lrs` map.
  `lrs` should have the string key with only the first letter capitalized
  (`Id` no `id`), and the value the prettified word or abbreviation."
  [lrs]
  (reset! label-replacements lrs))

(defn replace-with
  "Returns value in replacement map `rs` if the key `v` exists
  in `rs`.  Otherwise, returns `v`."
  [rs v]
  (get rs v v))

(defn replace-labels
  "Replace label `v` with replacement, otherwise pass through `v`."
  [v]
  (replace-with @label-replacements v))

(defn normalize-words
  "Normalize each word in `s`."
  [s]
  (->> s
       ->str
       str/lower-case
       (#(str/split % #" "))
       (map str/capitalize)
       (map replace-labels)
       (str/join " ")))

(defn generate-random-uuid
  "Generates random UUID string."
  []
  (str (UUID/randomUUID)))

(defn labelize
  "Converts `x` to a label."
  [x]
  (-> x
      ->str
      (str/replace "-" " ")
      (str/replace "_" " ")
      normalize-words
      str/trim))

(defn labelize-element
  "Converts element name `x` to a label."
  [x]
  (let [sx (->str x)
        splt (str/split sx #"\/")
        lbls (map #(-> %
                       (str/replace "-" " ")
                       (str/replace "_" " ")
                       normalize-words
                       str/trim)
                  splt)]
    (str/join " / " lbls)))

(defn safe-nth
  "Safe nth against collection `c` and index `ndx`.  Returns `nil` if exception."
  [c ndx]
  (try
    (nth c ndx)
    (catch Exception _)))

(defn random-int
  "Generates random int."
  []
  (.nextInt rndm))

(defn random-long
  "Generates random long."
  []
  (.nextLong rndm))

(defn random-float
  "Generates random float."
  []
  (float (* (if (odd? (System/currentTimeMillis)) -1 1)
            (float (rand))
            (float (dec Float/MAX_VALUE)))))

(defn random-double
  "Generates random double."
  []
  (* (if (odd? (System/currentTimeMillis)) -1 1)
     (rand)
     (dec Double/MAX_VALUE)))

(defn coin-toss?
  "Equal chance for true or false."
  []
  (odd? (System/currentTimeMillis)))

(defn force-in-range
  "Force numeric value `v` in range with `min` and `max`."
  [min max v]
  (try
    (if min
      (if (< v min)
        min
        (if (> v max)
          max
          v))
      (if max
        (if (> v max)
          max
          v)
        v))
    (catch Exception _
      v)))

(defn polite-conj
  "Politely conjoins element `x` to list `v` iff `x` is non-nil."
  [v x & xs]
  (if (empty? xs)
    (if (nil? x)
      v
      (conj (or v []) x))
    (reduce #(polite-conj % %2)
            v
            (cons x xs))))
