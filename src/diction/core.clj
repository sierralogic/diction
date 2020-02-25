(ns diction.core
  (:require [clj-time.coerce :as c]
            [clj-time.core :as t]
            [clojure.edn :as edn]
            [clojure.pprint :as pp]
            [clojure.set :as set]
            [clojure.string :as str]
            [clojure.test :refer [function?]]
            [clojure.test.check.generators :as gen]
            [diction.util :refer [->str vls?] :as util]
            [miner.strgen :as sg])
  (:import (org.joda.time DateTime)))

(def dictionary (atom nil))
(defn dictionary! [d] (reset! dictionary d))


;(defn clear-dictionary!
;  "Clears the `dictionary` completely to `nil`.  You must re-register the
;  dictionary after."
;  []
;  (dictionary! nil))

(def sensible (atom true))
(defn sensible! [s] (reset! sensible s))

(def ^:dynamic *force-sensible* nil)
(def ^:dynamic *generate-all-fields* nil)

(declare initialize-diction-elements!)
(declare explain)
(declare generate)
(declare generate-sensibly)

(defn clear-dictionary!
  "Clear dictionary entries."
  []
  (reset! dictionary nil)
  (initialize-diction-elements!))

(defn lookup
  "Lookup of element by element id `id`."
  [id]
  (get @dictionary id))

(def info lookup)
(def help lookup)

(defn <-meta
  [id]
  (when-let [lu (lookup id)]
    (get-in lu [:element :meta])))

(def req-key :required)
(def req-un-key :required-un)
(def opt-key :optional)
(def opt-un-key :optional-un)

;;; Generative Function Testing ==============================================================

(def functions (atom nil))

(defn function!
  "Registers function id `id` for function `f` and vector of element ids for the arguments
  `arugment-element-ids`, and result element id `result-element-id`."
  ([{:keys [id f argument-element-ids result-element-id]}] (function! id f argument-element-ids result-element-id))
  ([function-id f argument-element-ids result-element-id]
   (swap! functions assoc function-id {:id function-id
                                       :f f
                                       :arguments argument-element-ids
                                       :result result-element-id})))

(def default-test-function-count 10)

(defn functions!
  "Registers list of function `fs`."
  [fs]
  (reduce #(conj (or % []) (function! %2))
          nil
          fs))

(defn generate-test-arguments
  "Generates test "
  [arguments]
  (reduce #(conj (or % []) (generate %2))
          nil
          arguments))

(defn test-function
  "Generatively tests registered function `function-id` with optional test count `test-function-count`.
  Returns `nil` is all generative test function calls pass.  Otherwise, return failures."
  ([function-id] (test-function function-id nil))
  ([function-id test-function-count]
   (when-let [func (get @functions function-id)]
     (let [ct (or test-function-count default-test-function-count)
           result-element-id (:result func)
           f (:f func)
           arguments (:arguments func)
           fail (reduce (fn [_ n]
                          (let [res (apply f (generate-test-arguments arguments))]
                            (when-let [expln (explain result-element-id res)]
                              (reduced expln))))
                        nil
                        (range ct))]
       (when-not (empty? fail) fail)))))

(defn test-all-functions
  "Generatively test all registered functions with optional test function count `test-function-count`."
  ([] (test-all-functions nil))
  ([test-function-count]
   (reduce #(if-let [test-result (test-function %2 test-function-count)]
              (assoc % %2 test-result)
              %)
           nil
           (keys @functions))))

;;; Validation Rules =======================================================================

(def validation-rules (atom nil))

(defn validation-rule!
  "Registers a validation rule for element id `element-id`, rule id `id`, rule function `rule-f`, and optional
  conext map `ctx`."
  ([{:keys [element-id rule-id rule-f ctx]}] (validation-rule! element-id rule-id rule-f ctx))
  ([element-id rule-id rule-f] (validation-rule! element-id rule-id rule-f nil))
  ([element-id rule-id rule-f ctx]
   (swap! validation-rules update element-id #(assoc % rule-id (merge {:id rule-id
                                                                       :element-id element-id
                                                                       :rule-f rule-f}
                                                                      (when ctx {:ctx ctx}))))))
(defn remove-validation-rule!
  "Removes validation rule `rule-id` from element `element-id`."
  [element-id rule-id]
  (swap! validation-rules update element-id #(dissoc % rule-id)))

(defn clear-validation-rules!
  "Clears validation rules for element `element-id`."
  [element-id]
  (swap! validation-rules dissoc element-id))

(defn reset-validation-rules!
  "Resets the validation rules.  Clears all validation rules if no arguments or `x` is `nil`.  Otherwise,
  reset the validation rules to `x`."
  ([] (reset-validation-rules! nil))
  ([x]
   (reset! validation-rules x)))

;;; Decoration Rules ===========================================================================

(def decoration-rules (atom nil))

(defn decoration-rule!
  "Registers a decoration rule for element `element-id`, decoration rule id `rule-id`, rule function `rule-f`,
  and optional context map `ctx`."
  ([{:keys [element-id rule-id rule-f ctx]}] (decoration-rule! element-id rule-id rule-f ctx))
  ([element-id rule-id rule-f] (decoration-rule! element-id rule-id rule-f nil))
  ([element-id rule-id rule-f ctx]
   (swap! decoration-rules update element-id #(assoc % rule-id (merge {:id rule-id
                                                                       :element-id element-id
                                                                       :rule-f rule-f}
                                                                      (when ctx {:ctx ctx}))))))
(defn remove-decoration-rule!
  "Removes decoration rule `rule-id` from element `element-id`."
  [element-id rule-id]
  (swap! decoration-rules update element-id #(dissoc % rule-id)))

(defn clear-decoration-rules!
  "Clears decoration rules for element `element-id`."
  [element-id]
  (swap! decoration-rules dissoc element-id))

(defn reset-decoration-rules!
  "Resets the decoration rules.  Clears all decoration rules if no arguments or `x` is `nil`.  Otherwise,
  reset the decoration rules to `x`."
  ([] (reset-decoration-rules! nil))
  ([x]
   (reset! decoration-rules x)))

(defn decorate
  "Decorates element value `v` using decoration rules for element `element-id` with optional context map `ctx`."
  ([element-id v] (decorate element-id v nil))
  ([element-id v ctx]
   (if-let [entry (lookup element-id)]
     (reduce-kv (fn [a _ rule]
                  (if-let [af (:rule-f rule)]
                    (af a entry rule (merge (:ctx entry) (:ctx rule) ctx))
                    a))
                v
                (get @decoration-rules element-id))
     v)))

;;; Undecoration Rules ===========================================================================

(def undecoration-rules (atom nil))

(defn undecoration-rule!
  "Registers an undecoration rule for element `element-id`, undecoration rule id `rule-id`, rule function `rule-f`,
  and optional context map `ctx`."
  ([{:keys [element-id rule-id rule-f ctx]}] (undecoration-rule! element-id rule-id rule-f ctx))
  ([element-id rule-id rule-f] (undecoration-rule! element-id rule-id rule-f nil))
  ([element-id rule-id rule-f ctx]
   (swap! undecoration-rules update element-id #(assoc % rule-id (merge {:id rule-id
                                                                         :element-id element-id
                                                                         :rule-f rule-f}
                                                                        (when ctx {:ctx ctx}))))))
(defn remove-undecoration-rule!
  "Removes undecoration rule `rule-id` from element `element-id`."
  [element-id rule-id]
  (swap! undecoration-rules update element-id #(dissoc % rule-id)))

(defn clear-undecoration-rules!
  "Clears undecoration rules for element `element-id`."
  [element-id]
  (swap! undecoration-rules dissoc element-id))

(defn reset-undecoration-rules!
  "Resets the undecoration rules.  Clears all undecoration rules if no arguments or `x` is `nil`.  Otherwise,
  reset the undecoration rules to `x`."
  ([] (reset-undecoration-rules! nil))
  ([x]
   (reset! undecoration-rules x)))

(defn undecorate
  "Undecorates element value `v` using undecoration rules for element `element-id` with optional context map `ctx`."
  ([element-id v] (undecorate element-id v nil))
  ([element-id v ctx]
   (if-let [entry (lookup element-id)]
     (reduce-kv (fn [a _ rule]
                  (if-let [af (:rule-f rule)]
                    (af a entry rule (merge (:ctx entry) (:ctx rule) ctx))
                    a))
                v
                (get @undecoration-rules element-id))
     v)))

;;; Utilities --------------------------------------------------------------------

(def years-millis (* 365 24 60 60 1000))

(def document-keys-ns #{req-key opt-key})
(def document-keys-un #{req-un-key opt-un-key})

(def document-keys (set/union document-keys-ns document-keys-un))

(defn document?
  "Determines if `m` element is a document/map."
  [m]
  (not (empty? (set/intersection document-keys
                                 (into #{} (-> m :element keys))))))

(defn document-or-field
  "Returns either :documents or :fields based on if `m` is a document/map."
  [m]
  (if (document? m) :documents :fields))

;;; Generators -----------------------------------------------------------------------

(def uuid-regex-pattern "^[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}$")
(def uuid-regex (re-pattern uuid-regex-pattern))
(def uuid-regex-legacy #"^[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}$")

(defn generate-regex-string
  "Generates a string using the regular expression `regex`.  Basic regex is supported only."
  [regex]
  (-> regex
      sg/string-generator
      (gen/sample 1)
      first))

(defn generate-regex-keyword
  "Generates a keyword using the regular expression `regex`.  Basic regex is supported only."
  [regex]
  (-> regex
      sg/string-generator
      (gen/sample 1)
      first
      keyword))

(def random-chars "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVXYZ 1234567890-_=+")
(def default-random-count 64)

(def random-kw-chars "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVXYZ1234567890")

(defn generate-random-string
  "Generates a random string given optional minimum length `min` and maximum lenghts `max`.
  Default min is 0.  Default max is 64."
  ([] (generate-random-string nil nil))
  ([max] (generate-random-string max nil))
  ([min max] (generate-random-string min max nil))
  ([min max random-str]
   (let [rs (or random-str random-chars)
         rsc (count rs)
         to (if (number? max) max default-random-count)
         from (if (number? min) min 0)]
     (reduce (fn [a _] (str a (nth rs (rand-int rsc))))
             ""
             (range (+ from (rand-int (inc (- to from)))))))))

(defn generate-random-keyword
  "Generates a random keyword given optional minimum length `min` and maximum lenghts `max`.
  Default min is 0.  Default max is 64."
  ([] (generate-random-keyword nil nil))
  ([max] (generate-random-keyword max nil))
  ([min max] (generate-random-keyword min max nil))
  ([min max random-str]
   (keyword (generate-random-string min max (or random-str random-kw-chars)))))

(def default-gen-joda-min (- (System/currentTimeMillis) (* 10 years-millis)))
(def default-gen-joda-max (+ (System/currentTimeMillis) (* 10 years-millis)))

(defn generate-random-joda
  "Generates random int numbers given optional minimum `min` and maximum `max`.
  Default min is Integer/MIN_VALUE.  Default max is Integer/MAX_VALUE."
  ([] (generate-random-joda nil nil))
  ([max] (generate-random-joda nil max))
  ([min max]
   (let [norm-max (or (when max (if (util/joda? max) (c/to-long max) max))
                      default-gen-joda-max)
         norm-min (or (when min (if (util/joda? min) (c/to-long min) min))
                      default-gen-joda-min)
         df (try
              (- norm-max norm-min)
              (catch Exception _
                years-millis))]
     (try
       (DateTime. (long (+ norm-min (* df (rand)))))
       (catch Exception e
         (println :e e)
         (t/now))))))

(defn generate-random-boolean
  "Generates a randome boolean `true` or `false`."
  []
  (odd? (System/currentTimeMillis)))

(def default-gen-int-min Integer/MIN_VALUE)
(def default-gen-int-max Integer/MAX_VALUE)

(defn generate-random-int
  "Generates random int numbers given optional minimum `min` and maximum `max`.
  Default min is Integer/MIN_VALUE.  Default max is Integer/MAX_VALUE."
  ([] (generate-random-int nil nil))
  ([max] (generate-random-int nil max))
  ([min max]
   (if (or min max)
     (let [norm-max (or max default-gen-int-max)
           norm-min (or min default-gen-int-min)
           df (try
                (- norm-max norm-min)
                (catch Exception _
                  Integer/MAX_VALUE))]
       (try
         (int (+ norm-min (int (* df (rand)))))
         (catch Exception _
           norm-max)))
     (util/random-int))))

(def default-gen-long-min Long/MIN_VALUE)
(def default-gen-long-max Long/MAX_VALUE)

(defn generate-random-long
  "Generates random long numbers given optional minimum `min` and maximum `max`.
  Default min is Long/MIN_VALUE.  Default max is Long/MAX_VALUE."
  ([] (generate-random-long nil nil))
  ([max] (generate-random-long nil max))
  ([min max]
   (if (or min max)
     (let [norm-max (or max default-gen-long-max)
           norm-min (or min default-gen-long-min)
           df (try
                (- norm-max norm-min)
                (catch Exception _
                  Long/MAX_VALUE))]
       (try
         (long (+ norm-min (long (* df (rand)))))
         (catch Exception _
           norm-max)))
     (util/random-long))))

(def default-gen-float-min (float Float/MIN_VALUE))
(def default-gen-float-max (float Float/MAX_VALUE))

(defn generate-random-float
  "Generates random float numbers given optional minimum `min` and maximum `max`.
  Default min is Float/MIN_VALUE.  Default max is Float/MAX_VALUE."
  ([] (generate-random-float nil nil))
  ([max] (generate-random-float nil max))
  ([min max]
   (if (or min max)
     (let [norm-max (or max default-gen-float-max)
           norm-min (or min default-gen-float-min)
           df (try
                (- norm-max norm-min)
                (catch Exception _
                  (float Float/MAX_VALUE)))]
       (try
         (float (+ norm-min (* df (rand))))
         (catch Exception _
           (float norm-max))))
     (util/random-float))))

(def default-gen-double-min Double/MIN_VALUE)
(def default-gen-double-max Double/MAX_VALUE)

(defn generate-random-double
  "Generates random double numbers given optional minimum `min` and maximum `max`.
  Default min is Double/MIN_VALUE.  Default max is Double/MAX_VALUE."
  ([] (generate-random-double nil nil))
  ([max] (generate-random-double nil max))
  ([min max]
   (if (or min max)
     (let [norm-max (or max default-gen-double-max)
           norm-min (or min default-gen-double-min)
           df (try
                (- norm-max norm-min)
                (catch Exception _
                  Double/MAX_VALUE))]
       (try
         (double (+ norm-min (* df (rand))))
         (catch Exception _
           norm-max)))
     (util/random-double))))

(def default-gen-vector-min 0)
(def default-gen-vector-max 16)

(defn generate-random-vector
  "Generates a random vector of element `element-id` given optional minimum size `min` and maximum size `max`.
  Default min is 0.  Default max is 16."
  ([element-id] (generate-random-vector element-id nil nil))
  ([element-id max] (generate-random-vector element-id nil max))
  ([element-id min max] (generate-random-vector element-id min max nil))
  ([element-id min max meta]
   (let [sensible-min (when (or @sensible *force-sensible*) (:sensible-min meta))
         sensible-max (when (or @sensible *force-sensible*) (:sensible-max meta))
         norm-max (or sensible-max max default-gen-vector-max)
         norm-min (or sensible-min min default-gen-vector-min)
         df (- norm-max norm-min)]
     (reduce (fn [a _] (conj a (generate-sensibly element-id)))
             []
             (range (+ norm-min (rand-int (inc df))))))))

(defn generate-random-tuple
  "Generates a random tuple of elements `element-ids`"
  [element-ids]
  (reduce (fn [a eid]
            (conj a (generate-sensibly eid)))
          []
          element-ids))

(defn generate-random-poly-vector
  "Generates a random poly vector of elements `element-ids` given optional minimum size `min` and maximum size `max`.
  Default min is 0.  Default max is 16."
  ([element-ids] (generate-random-poly-vector element-ids nil nil nil))
  ([element-ids max] (generate-random-poly-vector element-ids nil max nil))
  ([element-ids min max] (generate-random-poly-vector element-ids min max nil))
  ([element-ids min max meta]
   (let [sensible-min (when @sensible (:sensible-min meta))
         sensible-max (when @sensible (:sensible-max meta))
         norm-max (or sensible-max max default-gen-vector-max)
         norm-min (or sensible-min min default-gen-vector-min)
         df (- norm-max norm-min)]
     (reduce (fn [a _] (conj a (reduce (fn [acc element-id]
                                         (conj acc (generate-sensibly element-id)))
                                       []
                                       element-ids)))
             []
             (range (+ norm-min (rand-int (inc df))))))))

(defn generate-random-set
  "Generates a random set of element `element-id` given optional minimum size `min` and maximum size `max`.
  Default min is 0.  Default max is 16."
  ([element-id] (generate-random-set element-id nil max nil))
  ([element-id max] (generate-random-set element-id nil max nil))
  ([element-id min max] (generate-random-set element-id min max nil))
  ([element-id min max meta]
   (let [sensible-min (when @sensible (:sensible-min meta))
         sensible-max (when @sensible (:sensible-max meta))
         norm-max (or sensible-max max default-gen-vector-max)
         norm-min (or sensible-min min default-gen-vector-min)
         df (- norm-max norm-min)]
     (reduce (fn [a _] (set/union a #{(generate-sensibly element-id)}))
             #{}
             (range (+ norm-min (rand-int (inc df))))))))

(defn generate-nested-elements
  "Generates nested elements `element-ids` with optional flags `unqualified?` for
  element id keys and `optional?` for the nested elements."
  ([element-ids] (generate-nested-elements element-ids false false))
  ([element-ids unqualified?] (generate-nested-elements element-ids unqualified? false))
  ([element-ids unqualified? optional?]
   (when-not (empty? element-ids)
     (reduce (fn [a element-id]
               (if (or (not optional?) (util/coin-toss?) *generate-all-fields*)
                 (let [k (if unqualified?
                           (if (keyword? element-id)
                             (keyword (name element-id))
                             element-id)
                           element-id)]
                   (assoc a k (generate-sensibly element-id)))
                 a))
             nil
             element-ids))))

(defn generate-random-map
  "Generates a random map."
  [element-id]
  (when-let [entry (lookup element-id)]
    (let [m (:element entry)]
      (into (sorted-map) (merge
                           (generate-nested-elements (req-key m))
                           (generate-nested-elements (req-un-key m) true)
                           (generate-nested-elements (opt-key m) false true)
                           (generate-nested-elements (opt-un-key m) true true))))))

;;; Validators --------------------------------------------------------------------------------

(defn validate-enum
  "Validates an enum type set `enum-s` against element value `v` given element id `id` and entry `entry` for better
  validation failure messages."
  [enum-s
   v id entry]
  (when-not (contains? enum-s v)
    [{:id id :entry entry
      :msg (str "Failed " id ": value '" v "' not in enum '" enum-s "'.")}]))

(defn validate-string
  "Validates a string value `v` given minimum length `min` (if non-nil), maximum length `max` (if non-nil),
  and regular expression `regex` (if non-nil).  The element id `id` of the string element as well as the element entry `entry`
  of the string element is also provided for better validation failure messages."
  [min max regex
   v id entry]
  (if-not (string? v)
    [{:id id :entry entry :v v
      :msg (str "Failed '" id "': value '" v "' is not a string.")}]
    (if-not (if regex (re-find regex v) true)
      [{:id id :entry entry :v v
        :msg (str "Failed '" id "': value '" v "' does not match regex '" regex "'.")}]
      (if-not (if min (>= (count v) min) true)
        [{:id id :entry entry :v v
          :msg (str "Failed '" id "': value '" v "' has too few characters. (min=" min ")")}]
        (when-not (if max (<= (count v) max) true)
          [{:id id :entry entry :v v
            :msg (str "Failed '" id "': value '" v "' has too many characters. (max=" max ")")}])))))

(defn validate-keyword
  "Validates a string value `v` given minimum length `min` (if non-nil), maximum length `max` (if non-nil),
  and regular expression `regex` (if non-nil).  The element id `id` of the string element as well as the element entry `entry`
  of the string element is also provided for better validation failure messages."
  [min max regex
   v id entry]
  (if-not (keyword? v)
    [{:id id :entry entry :v v
      :msg (str "Failed '" id "': value '" v "' is not a keyword.")}]
    (validate-string min max regex (->str v) id entry)))

(defn validate-vector-of
  "Validates the vector of element id `vector-of-element-id` given a mininum length `min`, maximum length `max`,
  element value `v`, parent element id `id`, and entry `entry`."
  [vector-of-element-id min max
   v id entry]
  (if-not (vector? v)
    [{:id id :entry entry :v v
      :msg (str "Failed '" id "': value '" v "' is not a vector.")}]
    (if-let [vofv (reduce #(if-let [ev (explain vector-of-element-id %2)]
                             (concat % ev)
                             %)
                          nil
                          v)]
      vofv
      (if-not (if min (>= (count v) min) true)
        [{:id id :entry entry :v v
          :msg (str "Failed '" id "': value '" v "' has too few elements [" (count v) "]. (min=" min ")")}]
        (when-not (if max (<= (count v) max) true)
          [{:id id :entry entry :v v
            :msg (str "Failed '" id "': value '" v "' has too many elements [" (count v) "]. (max=" max ")")}])))))

(defn validate-tuple-of
  "Validates the tuple of element ids `vector-of-element-ids` given
  element tuple value `v`, parent element id `id`, and entry `entry`."
  [vector-of-element-ids
   v id entry]
  (if-not (vector? v)
    [{:id id :entry entry :v v
      :msg (str "Failed '" id "': value '" v "' is not a tuple/vector.")}]
    (if (not= (count v) (count vector-of-element-ids))
      [{:id id :entry entry :v v
        :msg (str "Failed '" id "': value '" v "' has different element count. Expected "
                  (count vector-of-element-ids) " count not equal to actual tuple value count "
                  (count v) ".")}]
      (when-let [vofv (reduce (fn [acc ndx]
                                (if-let [ev (explain (util/safe-nth vector-of-element-ids ndx)
                                                     (util/safe-nth v ndx))]
                                  (concat acc ev)
                                  acc))
                              nil
                              (range (count vector-of-element-ids)))]
        vofv))))

(defn validate-poly-vector-of
  "Validates the poly vector of element ids `vector-of-element-ids` given a mininum length `min`, maximum length `max`,
  element poly vector value `v`, parent element id `id`, and entry `entry`."
  [vector-of-element-ids min max
   v id entry]
  (if-not (vector? v)
    [{:id id :entry entry :v v
      :msg (str "Failed '" id "': value '" v "' is not a vector.")}]
    (if-let [vofv (reduce (fn [acc ndx]
                            (if-let [ev (explain (util/safe-nth vector-of-element-ids ndx)
                                                 (util/safe-nth v ndx))]
                              (concat acc ev)
                              acc))
                          nil
                          (range (count v)))]
      vofv
      (if-not (if min (>= (count v) min) true)
        [{:id id :entry entry :v v
          :msg (str "Failed '" id "': value '" v "' has too few elements [" (count v) "]. (min=" min ")")}]
        (when-not (if max (<= (count v) max) true)
          [{:id id :entry entry :v v
            :msg (str "Failed '" id "': value '" v "' has too many elements [" (count v) "]. (max=" max ")")}])))))

(defn validate-set-of
  "Validates the set of element id `set-of-element-id` given a mininum length `min`, maximum length `max`,
  element value `v`, parent element id `id`, and entry `entry`."
  [set-of-element-id min max
   v id entry]
  (if-not (set? v)
    [{:id id :entry entry :v v
      :msg (str "Failed '" id "': value '" v "' is not a set.")}]
    (if-let [vofv (reduce #(if-let [ev (explain set-of-element-id %2)]
                             (concat % ev)
                             %)
                          nil
                          v)]
      vofv
      (if-not (if min (>= (count v) min) true)
        [{:id id :entry entry :v v
          :msg (str "Failed '" id "': value '" v "' has too few elements [" (count v) "]. (min=" min ")")}]
        (when-not (if max (<= (count v) max) true)
          [{:id id :entry entry :v v
            :msg (str "Failed '" id "': value '" v "' has too many elements [" (count v) "]. (max=" max ")")}])))))

(defn validate-class
  "Validates that the class `c` is the same as the class of `v` given optional element id `id` and entry `entry`."
  [c
   v id entry]
  (when (some? v)
    (when-not (= c (class v))
      [{:id id :entry entry :v v :c c
        :msg (str "Failed: value '" v "' class '" (class v) "' not as expected class '" c "'.")}])))

(defn validate-joda
  "Validates that a Joda datetime value `v` is valid given the optional minimum `min`, maximum `max`, element id
  `id`, and entry `entry`."
  [min max
   v id entry]
  (if-not (= util/joda-class (class v))
    [{:id id :entry entry :v v
      :msg (str "Failed '" id "': value '" v "' is not a Joda datetime.")}]
    (if-not (if min (t/after? v (if (util/joda? min) min (DateTime. min))) true)
      [{:id id :entry entry :v v
        :msg (str "Failed '" id "': value '" v "' is before than min. (min=" min "; " (DateTime. min) ")")}]
      (when-not (if max (t/before? v (if (util/joda? max) max (DateTime. max))) true)
        [{:id id :entry entry :v v
          :msg (str "Failed '" id "': value '" v "' is after than max. (max=" max "; " (DateTime. max) ")")}]))))

(defn validate-boolean
  "Validates if value `v` is boolean."
  [v id entry]
  (when-not (boolean? v)
    [{:id id :entry entry :v v
      :msg (str "Failed '" id "': value '" v "' is not a boolean.")}]))

(defn validate-int
  "Validates that an int number value `v` is valid given the optional minimum `min`, maximum `max`, element id
  `id`, and element `entry`."
  [min max
   v id entry]
  (if-not (util/strict-int? v)
    [{:id id :entry entry :v v
      :msg (str "Failed '" id "': value '" v "' is not an int number.")}]
    (if-not (if min (>= v min) true)
      [{:id id :entry entry :v v
        :msg (str "Failed '" id "': value '" v "' is less than min. (min=" min ")")}]
      (when-not (if max (<= v max) true)
        [{:id id :entry entry :v v
          :msg (str "Failed '" id "': value '" v "' is more than max. (max=" max ")")}]))))

(defn validate-long
  "Validates that a long number value `v` is valid given the optional minimum `min`, maximum `max`, element id
  `id`, and element `entry`."
  [min max
   v id entry]
  (if-not (util/strict-long? v)
    [{:id id :entry entry :v v
      :msg (str "Failed '" id "': value '" v "' is not a long number.")}]
    (if-not (if min (>= v min) true)
      [{:id id :entry entry :v v
        :msg (str "Failed '" id "': value '" v "' is less than min. (min=" min ")")}]
      (when-not (if max (<= v max) true)
        [{:id id :entry entry :v v
          :msg (str "Failed '" id "': value '" v "' is more than max. (max=" max ")")}]))))

(defn validate-float
  "Validates that a float number value `v` is valid given the optional minimum `min`, maximum `max`, element id
  `id`, and element `entry`."
  [min max
   v id entry]
  (if-not (util/strict-float? v)
    [{:id id :entry entry :v v
      :msg (str "Failed '" id "': value '" v "' is not a float number.")}]
    (if-not (if min (>= v min) true)
      [{:id id :entry entry
        :msg (str "Failed '" id "': value '" v "' is less than min. (min=" min ")")}]
      (when-not (if max (<= v max) true)
        [{:id id :entry entry :v v
          :msg (str "Failed '" id "': value '" v "' is more than max. (max=" max ")")}]))))

(defn validate-double
  "Validates that a double number value `v` is valid given the optional minimum `min`, maximum `max`, element id
  `id`, and element `entry`."
  [min max
   v id entry]
  (if-not (util/strict-double? v)
    [{:id id :entry entry :v v
      :msg (str "Failed '" id "': value '" v "' is not a double number.")}]
    (if-not (if min (>= v min) true)
      [{:id id :entry entry
        :msg (str "Failed '" id "': value '" v "' is less than min. (min=" min ")")}]
      (when-not (if max (<= v max) true)
        [{:id id :entry entry :v v
          :msg (str "Failed '" id "': value '" v "' is more than max. (max=" max ")")}]))))

(defn explain-nested-elements
  "Explain nested elements."
  ([nested-id parent-element-id v element-ids] (explain-nested-elements nested-id parent-element-id v element-ids false false))
  ([nested-id parent-element-id v element-ids unqualified?] (explain-nested-elements nested-id parent-element-id v element-ids unqualified? false))
  ([nested-id parent-element-id v element-ids unqualified? optional?]
   (reduce (fn [a element-id]
             (if-let [rv (get v (if unqualified?
                                  (if (keyword? element-id)
                                    (keyword (name element-id))
                                    element-id)
                                  element-id))]
               (if-let [rvr (explain element-id rv)]
                 (concat a (map #(util/stack-assoc % :parent-element-id parent-element-id) rvr))
                 a)
               (if-not optional?
                 (concat a [{:required-element-id element-id
                             :parent-element-id parent-element-id
                             :nested-element-id nested-id
                             :msg (str "Failed. Missing required element id '" element-id "'.")}])
                 a)))
           nil
           element-ids)))

(defn explain-map
  "Explains nested required element entries with the `nested-id`, listing of required element ids `required-element-ids`,
  element value `v`, element id `id`, and element `element`."
  [nested-id
   required-element-ids required-element-ids-un
   optional-element-ids optional-element-ids-un
   v id entry]
  (if (map? v)
    (let [rfv (explain-nested-elements nested-id id v required-element-ids)
          ruv (explain-nested-elements nested-id id v required-element-ids-un true)
          ofv (explain-nested-elements nested-id id v optional-element-ids false true)
          ouv (explain-nested-elements nested-id id v optional-element-ids-un true true)]
      (when-not (every? empty? [rfv ruv ofv ouv])
        (concat rfv ruv ofv ouv)))
    [{:id id :v v :parent-element-id [id] :entry entry
      :msg (str "Failed. Value '" v "' is not a map.")}]))

;;; Normalize Element Entry Types ------------------------------------------------------------------

(defn wrap-gen-f
  "Wraps generatior function `gen-f` with a function that ignores the element id `id` and element entry `entry` trailing
  paramsters when called by the framework to generate a element value. This may be used when the element value
  generator function does not need the `id` or `entry` to generate the value."
  [gen-f]
  (fn [_ _]
    (gen-f)))

(defn normalize-enum
  "Normalizes the enumeration entry type elements (if necessary) given element map `m`.  If not an enum type, passhtru
  the element map `m`."
  [m]
  (if-let [enm (:enum m)]
    (let [enum-s (into #{} enm)]
      (assoc m :gen-f (wrap-gen-f (partial rand-nth (vec enm)))
               :enum enum-s
               :valid-f (partial validate-enum enum-s)))
    m))

(defn normalize-class
  "Normalizes the class entry type elements (if necessary) given element map `m`.  If not a class type, passhtru
  the element map `m`."
  [m]
  (if-let [c (:class m)]
    (assoc m :valid-f (partial validate-class c))
    m))

(defn normalize-boolean
  "Normalizes the boolean type elements (if necessary) given element map `m`.  If not a boolean type, passhtru
  the element map `m`."
  [m]
  (if (= :boolean (:type m))
    (assoc m :gen-f (wrap-gen-f generate-random-boolean)
             :valid-f (partial validate-boolean))
    m))

(defn normalize-int
  "Normalizes the int number entry type elements (if necessary) given element map `m`.  If not a int number type, passhtru
  the element map `m`."
  [m]
  (if (= :int (:type m))
    (assoc m :gen-f (wrap-gen-f (partial generate-random-int (:min m) (:max m)))
             :valid-f (partial validate-int (:min m) (:max m)))
    m))

(defn normalize-long
  "Normalizes the long number entry type elements (if necessary) given element map `m`.  If not a int number type, passhtru
  the element map `m`."
  [m]
  (if (= :long (:type m))
    (assoc m :gen-f (wrap-gen-f (partial generate-random-long (:min m) (:max m)))
             :valid-f (partial validate-long (:min m) (:max m)))
    m))

(defn normalize-float
  "Normalizes the float number entry type elements (if necessary) given element map `m`.  If not a int number type, passhtru
  the element map `m`."
  [m]
  (if (= :float (:type m))
    (assoc m :gen-f (wrap-gen-f (partial generate-random-float (:min m) (:max m)))
             :valid-f (partial validate-float (:min m) (:max m)))
    m))

(defn normalize-double
  "Normalizes the double number entry type elements (if necessary) given element map `m`.  If not a int number type, passhtru
  the element map `m`."
  [m]
  (if (= :double (:type m))
    (assoc m :gen-f (wrap-gen-f (partial generate-random-double (:min m) (:max m)))
             :valid-f (partial validate-double (:min m) (:max m)))
    m))

(defn normalize-string
  "Normalizes the string entry type elements (if necessary) given element map `m`.  If not a string type, passhtru
  the element map `m`."
  [m]
  (if (= :string (:type m))
    (let [rgx-ptn (:regex-pattern m)
          rgx (if rgx-ptn (re-pattern rgx-ptn) (:regex m))]
      (merge (assoc m :gen-f (or                            ;(:gen-f m)
                               (if rgx
                                 (wrap-gen-f (partial generate-regex-string rgx))
                                 (wrap-gen-f (partial generate-random-string (:min m) (:max m) (:chars m)))))
                      :valid-f (or                          ; (:valid-f m)
                                 (partial validate-string (:min m) (:max m) rgx)))
             (when rgx {:regex rgx})))
    m))

(defn normalize-keyword
  "Normalizes the keyword entry type elements (if necessary) given element map `m`.  If not a string type, passhtru
  the element map `m`."
  [m]
  (if (= :keyword (:type m))
    (let [rgx-ptn (:regex-pattern m)
          rgx (when rgx-ptn (re-pattern rgx-ptn))]
      (merge (assoc m :gen-f (or                            ; (:gen-f m)
                               (if rgx
                                 (wrap-gen-f (partial generate-regex-keyword rgx))
                                 (wrap-gen-f (partial generate-random-keyword (:min m) (:max m)))))
                      :valid-f (or                          ; (:valid-f m)
                                 (partial validate-keyword (:min m) (:max m) rgx)))
             (when rgx {:regex rgx})))
    m))

(defn normalize-joda
  "Normalizes the joda entry type elements (if necessary) given element map `m`.  If not a string type, passhtru
  the element map `m`."
  [m]
  (if (= :joda (:type m))
    (assoc m :gen-f (wrap-gen-f (partial generate-random-joda (:min m) (:max m)))
             :valid-f (partial validate-joda (:min m) (:max m)))
    m))

(defn normalize-vector
  "Normalizes the vector-of entry type elements (if necessary) given element map `m`.  If not a vector-of type,
  passhtru the element map `m`."
  [m]
  (if-let [vof (:vector-of m)]
    (assoc m :gen-f (or (:gen-f m)
                        (wrap-gen-f (partial generate-random-vector
                                             vof
                                             (:min m)
                                             (:max m)
                                             (:meta m))))
             :valid-f (or (:valid-f m)
                          (partial validate-vector-of vof (:min m) (:max m))))
    m))

(defn normalize-tuple
  "Normalizes the poly-tuple-of entry type elements (if necessary) given element map `m`.
  If not a tuple type, passhtru the element map `m`."
  [m]
  (if-let [vof (:tuple m)]
    (assoc m :gen-f (or (:gen-f m)
                        (wrap-gen-f (partial generate-random-tuple
                                             vof)))
             :valid-f (or (:valid-f m)
                          (partial validate-tuple-of vof)))
    m))

(defn normalize-poly-vector
  "Normalizes the poly-vector-of entry type elements (if necessary) given element map `m`.
  If not a poly-vector-of type, passhtru the element map `m`."
  [m]
  (if-let [vof (:poly-vector-of m)]
    (assoc m :gen-f (or (:gen-f m)
                        (wrap-gen-f (partial generate-random-poly-vector
                                             vof
                                             (:min m)
                                             (:max m)
                                             (:meta m))))
             :valid-f (or (:valid-f m)
                          (partial validate-poly-vector-of vof (:min m) (:max m))))
    m))

(defn normalize-set
  "Normalizes the set entry type elements (if necessary) given element map `m`.  If not a set type,
  passhtru the element map `m`."
  [m]
  (if-let [vof (:set-of m)]
    (assoc m :gen-f (or (:gen-f m)
                        (wrap-gen-f (partial generate-random-set
                                             vof
                                             (:min m)
                                             (:max m)
                                             (:meta m))))
             :valid-f (or (:valid-f m)
                          (partial validate-set-of vof (:min m) (:max m))))
    m))

(defn capture-un-fields
  [unf]
  (reduce #(assoc % (-> %2 name keyword) %2)
          nil
          unf))

(defn normalize-map
  "Normalizes the required-optional/map entry type elements (if necessary) given entry map `m`.  If not a required-optiona/map type,
  passhtru the entry map `m`."
  [m]
  (let [required-element-ids (req-key m)
        required-element-ids-un (req-un-key m)
        optional-element-ids (opt-key m)
        optional-element-ids-un (opt-un-key m)
        req-un-m (capture-un-fields required-element-ids-un)
        opt-un-m (capture-un-fields optional-element-ids-un)]
    (if (or required-element-ids required-element-ids-un optional-element-ids optional-element-ids-un)
      (assoc m :gen-f (wrap-gen-f (partial generate-random-map (:id m)))
               :type :document
               :required-un-m req-un-m
               :optional-un-m opt-un-m
               :valid-f (partial explain-map
                                 (:id m)
                                 required-element-ids
                                 required-element-ids-un
                                 optional-element-ids
                                 optional-element-ids-un))
      m)))

(def type-normalizers (atom nil))

(defn type-normalizer!
  "Register register normalizer function `normalizer-f`."
  [normalizer-f]
  (swap! type-normalizers conj normalizer-f))

(defn type-normalizers!
  "Registers register normalizer fuction `normalizer-fs`."
  [normalizer-fs]
  (reduce #(conj (or % []) (type-normalizer! %2))
          nil
          normalizer-fs))

(def default-type-normalizers [normalize-enum
                               normalize-boolean
                               normalize-int
                               normalize-long
                               normalize-float
                               normalize-double
                               normalize-string
                               normalize-class
                               normalize-joda
                               normalize-keyword
                               normalize-vector
                               normalize-tuple
                               normalize-poly-vector
                               normalize-set
                               normalize-map])

(type-normalizers! default-type-normalizers) ; registers default type normalizer functions

(defn normalize-element
  "Generates entry settings for element id `id` given the original element map `original-element` (from the call argument),
  merged element from parent and original element `merged-element``, and optional `parent-id` and context map `ctx`."
  [parent-id id original-element ctx merged-element]
  (reduce #(apply %2 [%])
          merged-element
          @type-normalizers))

;;; Register Element Entries ------------------------------------------------------------------------

(defn element!
  "Registers element with id `id`, element map `element` with optional element context `ctx and parent element id `parent-id`.  The
   parent element may be used as a base for the element definition."
  ([{:keys [parent-id id element ctx]}] (element! parent-id id element ctx))
  ([id element] (element! nil id element nil))
  ([id element ctx] (element! nil id element ctx))
  ([parent-id id element ctx]
   (let [parent-entry (when parent-id (lookup parent-id))
         merged-element (merge (:element parent-entry)
                               {:id id}
                               (when parent-id {:parent-id parent-id})
                               element)
         normalized-element (normalize-element parent-id id element ctx merged-element)
         merged-ctx (merge (:ctx parent-entry)
                           ctx)
         entry (merge {:id id
                       :element normalized-element}
                      (when merged-ctx {:ctx merged-ctx}))]
     (swap! dictionary assoc id entry)
     entry)))

;(defn polite-assoc
;  "Politely associate key `k` with value `v` to map `m` iff there is no existing entry for
;  `k` and `v` in `m`."
;  [m k v]
;  (when (and k v)
;    (if-let [xv (get m k)]
;      m
;      (assoc m k v))))

(defn resolve-unqualified
  "Resolve unqualified element keys given entitiy maps `es` and the
  `key` of either `required-un` or `optional-un`."
  [key es]
  (vals (reduce #(if-let [fs (get %2 key)]
                                      (reduce (fn [a ek]
                                                (util/polite-assoc a (name ek) ek))
                                              %
                                              fs)
                                      %)
                                   nil
                                   es)))

(defn resolve-req-opt-un
  "Resolves optional unqualified given the existing required unqualified `req-un`
  and the initial opertional unqualified `opt-un` so that there are no unqualified
  element keys in opertional that are also in the required unqualified."
  [req-un opt-un]
  (if (empty? req-un)
    opt-un
    (into #{} (let [req-un-names (into #{} (map name req-un))]
                (reduce #(if (contains? req-un-names (name %2))
                           %
                           (cons %2 %))
                        nil
                        opt-un)))))

(defn resolve-merged-entities
  "Resolve merged entites `e` and variadic entites `es`."
  [e & es]
  (let [ces (cons e es)
        merged-req (into #{} (apply concat (map req-key ces)))
        merged-req-un (into #{} (resolve-unqualified req-un-key ces))
        merged-opt (into #{} (apply concat (map opt-key ces)))
        merged-opt-un (into #{} (resolve-unqualified opt-un-key ces))
        resolved-opt (set/difference merged-opt merged-req)
        resolved-opt-un (resolve-req-opt-un merged-req-un merged-opt-un)
        merged (merge (when-not (empty? merged-req) {req-key merged-req})
                      (when-not (empty? merged-req-un) {req-un-key merged-req-un})
                      (when-not (empty? resolved-opt) {opt-key resolved-opt})
                      (when-not (empty? resolved-opt-un) {opt-un-key resolved-opt-un}))]
    merged))

(defn merge-entities!
  "Merges and registers parent id `parent-id` for element `id` with optional
  element map `element` and context map `ctx`."
  ([{:keys [parent-ids id element ctx]}] (merge-entities! parent-ids id element ctx))
  ([parent-ids id] (merge-entities! parent-ids id nil nil))
  ([parent-ids id element] (merge-entities! parent-ids id element nil))
  ([parent-ids id element ctx]
   (let [parent-entries (->> parent-ids
                             (map lookup)
                             (filter some?))
         parent-elements (->> parent-entries
                              (map :element)
                              (map (fn [x] (select-keys x [req-key req-un-key opt-key opt-un-key]))))
         parent-ctxs (->> parent-entries
                          (map :ctx)
                          (filter some?)
                          ((fn [ctxs]
                            (reduce (fn [a c]
                                      (merge a c))
                                    nil
                                    ctxs))))
         resolved-entities (apply resolve-merged-entities (cons element parent-elements))
         sm (merge {:id id} resolved-entities)
         merged-ctx (merge parent-ctxs ctx)]
     (element! id sm merged-ctx))))

(defn merge!
  "Merges and registers parent id `parent-id` for element `id` with optional
  element map `element` and context map `ctx`."
  ([{:keys [parent-id id element ctx]}] (merge! parent-id id element ctx))
  ([parent-id id] (merge! parent-id id nil nil))
  ([parent-id id element] (merge! parent-id id element nil))
  ([parent-id id element ctx]
   (let [parent-entry (lookup parent-id)
         parent-element (:element parent-entry)
         req (concat (req-key parent-element) (req-key element))
         req-un (concat (req-un-key parent-element) (req-un-key element))
         opt (concat (opt-key parent-element) (opt-key element))
         opt-un (concat (opt-un-key parent-element) (opt-un-key element))
         sm (merge parent-element
                   element
                   {:id id}
                   (when-not (empty? req) {req-key (vec (into #{} req))})
                   (when-not (empty? req-un) {req-un-key (vec (into #{} req-un))})
                   (when-not (empty? opt) {opt-key (vec (into #{} opt))})
                   (when-not (empty? opt-un) {opt-un-key (vec (into #{} opt-un))}))
         merged-ctx (merge (:ctx parent-entry) ctx)]
     (element! id sm merged-ctx))))

(defn inherit!
  "Register a child element given element id `id`, parent element id `parent-id`, element
  map `element` and context `ctx`."
  ([parent-id id] (element! parent-id id nil nil))
  ([parent-id id element] (element! parent-id id element nil))
  ([parent-id id element ctx]
   (element! parent-id id element ctx)))

(defn clone!
  "Clones `parent-id` to the new element id `id`."
  ([parent-id id] (clone! parent-id id nil))
  ([parent-id id ctx]
  (when-let [p (lookup parent-id)]
    (let [merged-meta (or (merge (get-in p [:element :meta]) (:meta ctx)) {})
          entry (-> p
                    (assoc :id id)
                    (assoc-in [:element :id] id)
                    (assoc-in [:element :meta] merged-meta))]
      (swap! dictionary assoc id entry)
      entry))))

(defn vector!
  "Register a vector element given element id `id`, vector element id `vector-of-element-id`, element
  map `element` and context `ctx`."
  ([id vector-of-element-id] (vector! id vector-of-element-id nil nil))
  ([id vector-of-element-id element] (vector! id vector-of-element-id element nil))
  ([id vector-of-element-id element ctx]
   (element! id (merge {:vector-of vector-of-element-id :type :vector} element) (merge ctx {:element vector-of-element-id}))))

(defn poly-vector!
  "Register a poly vector (polyglot of element ids) element given element id `id`,
  vector element ids `vector-of-element-ids`, element map `element` and context `ctx`."
  ([id vector-of-element-ids] (poly-vector! id vector-of-element-ids nil nil))
  ([id vector-of-element-ids element] (poly-vector! id vector-of-element-ids element nil))
  ([id vector-of-element-ids element ctx]
   (element! id (merge {:poly-vector-of vector-of-element-ids :type :poly-vector} element) (merge ctx {:elements vector-of-element-ids}))))

(defn tuple!
  "Register a tuple (polyglot of element ids) element given element id `id`,
  vector element ids `vector-of-element-ids`, element map `element` and context `ctx`."
  ([id vector-of-element-ids] (tuple! id vector-of-element-ids nil nil))
  ([id vector-of-element-ids element] (tuple! id vector-of-element-ids element nil))
  ([id vector-of-element-ids element ctx]
   (element! id (merge {:tuple vector-of-element-ids :type :tuple} element) (merge ctx {:elements vector-of-element-ids}))))

(defn set-of!
  "Register a set element given element id `id`, set element id `set-of-element-id`, element
  map `element` and context `ctx`."
  ([id set-of-element-id] (set-of! id set-of-element-id nil nil))
  ([id set-of-element-id element] (set-of! id set-of-element-id element nil))
  ([id set-of-element-id element ctx]
   (element! id (merge {:set-of set-of-element-id :type :set} element) (merge ctx {:element set-of-element-id}))))

(defn document!
  "Register a document/map element given element id `id`, required unqualified nested element ids `req-un`,
  optional nested unqualified element ids `opt-un`, required nested element ids `req`, optional nested element
  ids `opt-un`, element map `element` and context `ctx`."
  ([id req-un] (document! id nil req-un nil nil nil nil))
  ([id req-un opt-un] (document! id nil req-un nil opt-un nil nil))
  ([id req-un opt-un element] (document! id nil req-un nil opt-un element nil))
  ([id req req-un opt opt-un element ctx]
   (element! id
             (merge element
                    (when req {req-key req})
                    (when req-un {req-un-key req-un})
                    (when opt {opt-key opt})
                    (when opt-un {opt-un-key opt-un}))
             ctx)))

(defn document-type?
  "Determines if diction `entry` is document type or not."
  [entry]
  (let [t (get-in entry [:element :type])]
    (or (= t :document) (= t :map) (= t :entity))))


(defn document-field-element-ids
  "Generates the field elements for document element `id` for both
  namespaced and unqualified fields."
  [id]
  (when-let [entry (lookup id)]
    (when (document-type? entry)
      (let [flds (reduce #(if-let [ks (get-in entry [:element %2])]
                            (concat % ks)
                            %)
                         nil
                         document-keys-ns)
            flds-un (reduce #(if-let [ks (get-in entry [:element %2])]
                               (concat % (map (fn [x] (-> x name keyword)) ks))
                               %)
                            nil
                            document-keys-un)]
        (when-not (and (empty? flds) (empty? flds-un))
          (into #{} (concat flds flds-un)))))))

(def map! document!)
(def entity! document!)

(defn enum!
  "Register an enum element given element id `id`, enum set/list/vector `enums`, element
  map `element` and context `ctx`."
  ([id enums] (enum! id enums nil nil))
  ([id enums element] (enum! id enums element nil))
  ([id enums element ctx]
   (element! id (merge {:enum enums :type :enum} element) (merge ctx {:values enums}))))

;;; Base Elements ------------------------------------------------------------

(def diction-boolean :diction/boolean)

(def diction-int :diction/int)
(def diction-int-pos :diction/pos-int)
(def diction-int-neg :diction/neg-int)

(def diction-long :diction/long)
(def diction-long-pos :diction/pos-long)
(def diction-long-neg :diction/neg-long)

(def diction-float :diction/float)
(def diction-float-pos :diction/pos-float)
(def diction-float-neg :diction/neg-float)

(def diction-double :diction/double)
(def diction-double-pos :diction/pos-double)
(def diction-double-neg :diction/neg-double)

(def diction-string :diction/string)
(def diction-keyword :diction/keyword)
(def diction-uuid :diction/uuid)
(def diction-joda :diction/joda)

(def boolean! (partial inherit! diction-boolean))
(def int! (partial inherit! diction-int))
(def pos-int! (partial inherit! diction-int-pos))
(def neg-int! (partial inherit! diction-int-neg))

(def long! (partial inherit! diction-long))
(def pos-long! (partial inherit! diction-long-pos))
(def neg-long! (partial inherit! diction-long-neg))

(def float! (partial inherit! diction-float))
(def pos-float! (partial inherit! diction-float-pos))
(def neg-float! (partial inherit! diction-float-neg))

(def double! (partial inherit! diction-double))
(def pos-double! (partial inherit! diction-double-pos))
(def neg-double! (partial inherit! diction-double-neg))

(def string! (partial inherit! diction-string))
(def keyword! (partial inherit! diction-keyword))
(def uuid! (partial inherit! diction-uuid))
(def joda! (partial inherit! diction-joda))

(defn initialize-diction-elements!
  []

  (element! diction-boolean {:type :boolean})

  (element! diction-int {:type :int})
  (element! diction-int-pos {:type :int :min 0})
  (element! diction-int-neg {:type :int :min Integer/MIN_VALUE :max 0})

  (element! diction-long {:type :long})
  (element! diction-long-pos {:type :long :min 0 :max Long/MAX_VALUE})
  (element! diction-long-neg {:type :long :min Long/MIN_VALUE :max 0})

  (element! diction-float {:type :float})
  (element! diction-float-pos {:type :float :min 0.0 :max Float/MAX_VALUE})
  (element! diction-float-neg {:type :float :min Float/MIN_VALUE :max 0.0})

  (element! diction-double {:type :double})
  (element! diction-double-pos {:type :double :min 0.0 :max Double/MAX_VALUE})
  (element! diction-double-neg {:type :double :min Double/MIN_VALUE :max 0.0})

  (element! diction-string {:type :string :min 0 :max 64})
  (element! diction-keyword {:type :keyword :min 1 :max 64})
  (element! diction-uuid {:type :string :min 0 :max 36 :regex-pattern uuid-regex-pattern :meta {:label "UUID" :description "UUID String"}})
  (element! diction-joda {:type :joda :meta {:label "Joda Datetime" :description "Joda Date Time"}})

  (string! :diction/foo {:meta {:sensible-values ["bar" "baz" "bah"]}})
  (long! :diction/ans {:meta {:sensible-values [41 42 43 99]}})

  (document! :diction/foobar [:diction/foo] [:diction/ans])


  )


;;;; Element Functions --------------------------------------------------------------------------

(defn force-generate
  "Generates a valid value of element id `id`."
  [id]
  (when-let [entry (lookup id)]
    (when-let [gen-f (get-in entry [:element :gen-f])]
      (gen-f id entry))))

(defn random-sensible-value
  "Returns random sensible value (:element :meta :sensible-values) for
  element `id` and optional `generate-as-fallback?` flag.
  Returns `nil` if no sensible values are found and optional `genereate-as-fallback?`
  flag is `false` or `nil`.
  If no sensible values are found and optional `generate-as-fallback?` flag
  is `truthy`, then attempt to generate value for element `id`."
  ([id] (random-sensible-value id true))
  ([id generate-as-fallback?]
   (when-let [entry (lookup id)]
     (if-let [svs (when (or @sensible *force-sensible*) (get-in entry [:element :meta :sensible-values]))]
       (if-not (empty? svs)
         (nth svs (Math/floor (* (rand) (count svs))))
         (when generate-as-fallback? (force-generate id)))
       (when generate-as-fallback? (force-generate id))))))

(defn generate-sensibly
  "Generates a sensible value for element with `id` and optional `generate-as-fallback?`
  flag to use the generative function for the element if no sensible values
  exist in the meta of the element (:element :meta :sensible-values)."
  ([id] (generate-sensibly id true))
  ([id generate-as-fallback?]
   (random-sensible-value id generate-as-fallback?)))

(def generate generate-sensibly)

(defn explain
  "Explains validation failures for element `id` against element value `v` as a vector of maps with validation
  failure messages and info.  If no validation failure occurs, returns nil."
  [id v]
  (if-let [entry (lookup id)]
    (let [sv (when-let [vf (get-in entry [:element :valid-f])]
               (vf v id entry))]
      (when-not (empty? sv)
        (vec sv)))
    [{:id id :v v
      :msg (str "Element '" id "' does not exist.")}]))

(defn explain-all
  "Explains validation failures for element `id` against element value `v` as a vector of maps with validation
  failure messages and info.  If no validation failure occurs, returns nil."
  [id v]
  (if-let [entry (lookup id)]
    (let [sv (when-let [vf (get-in entry [:element :valid-f])] (vf v id entry))
          vrs (reduce-kv (fn [a _ rule]
                           (if-let [vrr ((:rule-f rule) v entry rule (merge (:ctx entry) (:ctx rule)))]
                             (concat a vrr)
                             a))
                         nil
                         (get @validation-rules id))]
      (when-not (and (empty? sv)
                     (empty? vrs))
        (vec (concat sv vrs))))
    [{:id id :v v
      :msg (str "Element '" id "' does not exist.")}]))

(defn valid?
  "Determines if the element value `v` of element `id` is valid."
  [id v]
  (empty? (explain id v)))

(defn valid-all?
  "Determines if the element value `v` of element `id` is valid."
  [id v]
  (empty? (explain-all id v)))

(declare groom)

(defn groom-vector
  ([parent-id id v] (groom-vector parent-id id v nil))
  ([parent-id id v ctx]
   (when-let [entry (lookup id)]
     (when-let [vot-id (or (get-in entry [:element :vector-of])
                           (get-in entry [:element :poly-vector-of]))]
       (reduce #(conj (or % []) (groom id vot-id %2 nil))
               nil
               v)))))

(defn field-element-id
  "Returns the namespaced (if available) field id for document element `id` given
  a field id `id`."
  [id fld-id]
  (if (namespace fld-id)
    fld-id
    (if-let [entry (lookup id)]
      (or (get-in entry [:element :required-un-m fld-id])
          (get-in entry [:element :optional-un-m fld-id])
          fld-id)
      fld-id)))

(defn groom
  "Grooms the `value` of element `id` to align with only the registered diction elements.
  If the `value` is a map, then groom acts recursively like `select-keys` to groom unregistered
  keys and only passthru registered diction element keys (required and optional)."
  ([id v] (groom nil id v nil))
  ([id v ctx] (groom nil id v nil))
  ([parent-id id v ctx]
   (when-let [entry (lookup id)]
     (when (valid? id v)
       (if (map? v)
         (let [flds (document-field-element-ids id)]
           (reduce #(if-let [nv (get v %2)]
                      (let [qfld (field-element-id id %2)]
                        (if-let [entry (lookup qfld)]
                          (assoc % %2 (groom id qfld nv ctx))
                          (assoc % %2 nv)))
                      %)
                   nil
                   flds))
         (if (vls? v)
           (groom-vector parent-id id v)
           v))))))

(initialize-diction-elements!)

;;; Data Dictionary ================================================================

(defn filter-out-diction-elements
  "Filters out diction elements given element `x`."
  [x]
  (if-let [id (:id x)]
    (if (keyword? id)
      (if-let [ns (namespace id)]
        (not (str/starts-with? ns "diction"))
        true)
      true)
    true))

(defn references
  "Determine the documents that reference the element `element-id`."
  [element-id]
  (let [docs (filterv #(= (get-in % [:element :type]) :document) (vals @dictionary))]
    (reduce (fn [a d]
              (let [elem (:element d)
                    refs (concat (:required elem) (:required-un elem) (:optional elem) (:optional-un elem))]
                (if (empty? refs)
                  a
                  (if (contains? (into #{} refs) element-id)
                    (conj (or a []) (:id d))
                    a))))
            nil
            docs)))

(defn summary-entry
  "Generates an index entry for element entry `entry`."
  [entry]
  (let [id (:id entry)
        element (:element entry)
        ctx (:ctx entry)
        type (:type element)
        meta (:meta element)
        lbl (:label meta)
        desc (:description meta)
        elem (:element ctx)
        elems (:elements ctx)
        help (:help meta)
        enum-values (:values ctx)
        required (:required element)
        required-un (:required-un element)
        optional (:optional element)
        optional-un (:optional-un element)
        referenced (references id)
        refs (vec (sort (concat required required-un optional optional-un)))
        audit (:audit meta)]
    (merge {:id (:id entry)}
           (when type {:type type})
           (when lbl {:label lbl})
           (when desc {:description desc})
           (when elem {:element elem})
           (when elems {:elements elems})
           (when referenced {:referenced-by referenced})
           (when (not (empty? refs)) {:fields refs})
           (when required {:required required})
           (when required-un {:required-unqualified required-un})
           (when optional {:optional optional})
           (when optional-un {:optional-unqualified optional-un})
           (when enum-values {:values enum-values})
           (when help {:help help})
           (when audit {:audit audit}))))

(defn annotate-summary
  [entry]
  (let [id (:id entry)
        element (:element entry)
        ctx (:ctx entry)
        type (:type element)
        meta (:meta element)
        lbl (:label meta)
        desc (:description meta)
        elem (:element ctx)
        elems (:elements ctx)
        help (:help meta)
        enum-values (:values ctx)
        required (:required element)
        required-un (:required-un element)
        optional (:optional element)
        optional-un (:optional-un element)
        referenced (references id)
        refs (vec (sort (concat required required-un optional optional-un)))
        audit (:audit meta)]
    (-> entry
        (assoc-in [:element :summary] (summary-entry entry))
        (util/polite-assoc-in [:element :referenced-by] referenced))))

(defn data-dictionary
  "Generates a data dictionary given current element definitions."
  []
  (let [sorted-elements (->> @dictionary
                             vals
                             (filter filter-out-diction-elements)
                             (sort-by :id))
        nelems (mapv annotate-summary sorted-elements)
        fds (group-by document-or-field nelems)
        summary (mapv summary-entry sorted-elements)]
    (merge {:summary summary}
           fds
           {:elements nelems})))

;;; Import / Export ========================================================

(def exclude-export-keys [:gen-f :valid-f :regex])

(defn generate-export-entry
  "Generates export entry map from element entry `entry`."
  [entry]
  (merge (assoc entry :element (apply dissoc (cons (:element entry) exclude-export-keys)))
         (when-let [parent-id (get-in entry [:element :parent-id])] {:parent-id parent-id})))

(defn export-entries
  "Generates export vector of element entries."
  []
  (reduce (fn [a entry]
            (conj (or a []) (generate-export-entry entry)))
          nil
          (->> @dictionary
               vals
               (filter filter-out-diction-elements))))

(defn export-entries-to-file!
  "Exports element entries to file `fn`."
  [fn]
  (spit fn (with-out-str (pp/pprint (export-entries)))))

(defn import-entries!
  "Imports element entries `entries`."
  [entries]
  (reduce #(conj (or % []) (element! %2))
          nil
          entries))

(defn import-entries-from-file!
  "Imports element entries from file `fn`."
  [fn]
  (import-entries! (edn/read-string (slurp fn))))

;;; Metadata ======================================================

(defn split-meta-query
  "Splits query map `q` into functions and non-functions based on
  values.  Function kvs are in {true : {}} and non-function kvs are in {false : {}}."
  [q]
  (reduce-kv #(update % (function? %3) (fn [x] (assoc x %2 %3)))
             nil
             q))

(defn meta-query
  "Query diction element metadata with map `meta-query-map` and keys
  `element-ids` (optional) : subset of element ids to search;
  `query` (required) : query map that will simple try to match the element meta data map"
  [{:keys [element-ids query query-f mask] :as meta-query-map}]
  (when-not (and (empty? query) (nil? query-f))
    (reduce #(if-let [entry (lookup %2)]
               (if-let [meta (get-in entry [:element :meta])]
                 (let [split-q (split-meta-query query)
                       query-lits (get split-q false)
                       query-fs (get split-q true)
                       qlks (keys query-lits)]
                   (if (and (or (empty? query-lits)
                                (= query-lits (select-keys meta qlks)))
                            (or (empty? query-fs)
                                (reduce-kv (fn [a k f]
                                             (if (try
                                                   (f (get meta k))
                                                   (catch Exception _
                                                     false))
                                               a
                                               (reduced false)))
                                           true
                                           query-fs))
                            (or (nil? query-f)
                                (try
                                  (query-f meta)
                                  (catch Exception _
                                    false)))
                            (not (and (empty? query-fs)
                                      (empty? query-lits)
                                      (nil? query-f))))
                     (conj (or % [])
                           (if mask
                             (merge {:id %2}
                                    (select-keys meta mask))
                             entry))
                     %))
                 %)
               %)
            nil
            (or element-ids (filter filter-out-diction-elements (keys @dictionary))))))


(string! ::fullname)
(string! ::first-name)
(string! ::middle-name)
(string! ::last-name)
(string! ::aka)

(joda! ::dob)
(joda! ::dod)

;; (entity! ::person [::last-name ::dob] [::first-name ::fullname ::middle-name ::aka ::dod])

(uuid! ::id)
(string! ::type)
(string! ::label)
(string! ::description)

(long! ::answer)
(long! ::count)
(double! ::metric)
(double! ::tau)

(element! ::person {:type :map
                    req-key [::last-name ::dob]
                    opt-key [::first-name ::fullname
                             ::middle-name ::aka ::dod]})
(element! ::handle {:type :map
                    req-key [::id]
                    opt-un-key [::label ::description
                                ::type]})
(element! ::muck {:type :map
                  req-un-key [::answer]
                  opt-key [::id ::label]})

(def func-types {double! #{:double}
                 element! #{:document :map :entity}
                 enum! #{:enum}
                 float! #{:float}
                 int! #{:int :integer}
                 joda! #{:joda :date :datetime}
                 keyword! #{:keyword :kw}
                 long! #{:long}
                 string! #{:text :string}
                 uuid! #{:uuid}
                 vector! #{:vector :list}})

(defn generate-lookups
  [lm]
  (reduce-kv #(reduce (fn [avk vk]
                        (assoc avk vk %2))
                      %
                      %3)
          nil
          lm))

(def type-func (generate-lookups func-types))

(def dev-export
  {:elements [{:id :yak
               :type :string
               :min 0
               :max 64}
              {:id :my-uuid
               :parent-id :diction/uuid}]})

(defn safe-ns
  [x]
  (try
    (or (namespace x) "")
    (catch Exception _
      "")))

;(defn import-elements!
;  [dm]
;  (when-let [es (:elements dm)]
;    (reduce #(let [{:keys [id parent-id ctx]} %2
;                   args [id %2 ctx]
;                   pargs (if parent-id (cons parent-id args) args)]
;               (conj (or % []) (apply element! pargs)))
;            nil
;            es)))

(def export-exclude-prefix "diction")

(defn export-elements!
  []
  (let [d (mapv #(-> %
                     second
                     :element
                     (dissoc :gen-f :valid-f))
                (filter #(-> %
                             first
                             safe-ns
                             (str/starts-with? export-exclude-prefix)
                             not)
                        @dictionary))]
    d))

(defn export-elements-to!
  [target]
  (spit target {:elements (export-elements!)}))

;(defn import-elements-from!
;  [source]
;  (import-elements! (-> source slurp edn/read-string)))

(defn clean
  [elem]
  (dissoc elem :type :id))

(defn import-document!
  "Import the document element `elem`."
  [elem]
  (let [{:keys [id required required-un optional optional-un]} elem]
    (document! id required required-un optional optional-un (clean elem) nil)))

(defn import-clone!
  "Import a clone element `elem`."
  [elem]
  (clone! (:clone elem) (:id elem) (clean elem)))

(defn import-element!
  "Import element `elem` with optional diction function `diction-f`."
  ([elem] (import-element! string! elem))
  ([diction-f elem]
   (diction-f (:id elem) elem)))

(defn import-ofs!
  "Import collection of `of-key` elements for element `elem` with the diction
  `register-f` function."
  [register-f of-key elem]
  (let [{:keys [id]} elem
        of (get elem of-key)]
    (register-f id of (clean elem))))

(def import-vector! (partial import-ofs! vector! :vector-of))
(def import-poly-vector! (partial import-ofs! poly-vector! :poly-vector-of))
(def import-set! (partial import-ofs! set-of! :set-of))
(def import-enum! (partial import-ofs! enum! :enum))
(def import-tuple! (partial import-ofs! tuple! :tuple))

(def import-string! (partial import-element! string!))
(def import-double! (partial import-element! double!))
(def import-pos-double! (partial import-element! pos-double!))
(def import-neg-double! (partial import-element! neg-double!))
(def import-long! (partial import-element! long!))
(def import-float! (partial import-element! float!))
(def import-pos-float! (partial import-element! pos-float!))
(def import-neg-float! (partial import-element! neg-float!))
(def import-boolean! (partial import-element! boolean!))

(def types->functions
  {:string import-string!
   :document import-document!
   :double import-double!
   :pos-double import-pos-double!
   :neg-double import-neg-double!
   :long import-long!
   :float import-float!
   :pos-float import-pos-float!
   :neg-float import-neg-float!
   :vector import-vector!
   :set import-set!
   :enum import-enum!
   :tuple import-tuple!
   :poly-vector import-poly-vector!
   :boolean import-boolean!
   :clone import-clone!})

(defn register-element!
  "Register element `elem` using declarative data."
  [elem]
  (let [nelem (if (:clone elem)
                (assoc elem :type :clone)
                elem)
        type (:type nelem :string)
        register-f (get types->functions type import-string!)]
    (register-f elem)))

(defn import!
  "Import element `elem` to diction data dictionary."
  [elem]
  (register-element! elem))

(defn imports!
  "Imports elements `elements` to diction data dictionary."
  [elements]
  (let [res (reduce #(conj (or % []) (import! %2))
                    nil
                    elements)]

    res))

(defn import-from-file!
  "Import elements from `source` file."
  [source]
  (imports! (-> source slurp edn/read-string)))
