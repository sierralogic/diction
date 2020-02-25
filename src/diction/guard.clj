(ns diction.guard
  (:require [diction.core :refer [explain generate] :as diction]))

(defn default-guard-fail-f
  "Default `guard-fail-f` function with signature [eid f v xvf failures & args]."
  [element-id f v xvf failures & args]
  {:status 400
   :body {:error (str "Guard validation failures for '" element-id "' for function '" f "'.")
          :f f
          :v v
          :extract xvf
          :failures failures
          :element element-id
          :args args}})

(def guard-fail-f (atom default-guard-fail-f))
(defn guard-fail-f!
  "Sets the guard fail function atom `guard-fail-f` to `f`.
  The `guard-fail-f` has a signature [element-id guarded-f arg-value-validated
  extract-arg-f failures & args]."
  [f]
  (reset! guard-fail-f f))

(defn guard
  "Generates a diction validation guard function calling function `f` with
  variadic arguments given a diction `element-id` successful validation and
  calling the atom function variable `guard-fail-f` if validation fails.
  The optional `v-extract-f` is used to extract the value to be validated
  against the `element-id` from the `args` vector; defaults to `first`."
  ([element-id f] (guard element-id nil f))
  ([element-id v-extract-f f]
   (fn [& args]
     (let [xvf (or v-extract-f first)
           v (xvf args)]
       (if-let [failures (explain element-id v)]
         (apply @guard-fail-f (concat [element-id f v xvf failures] args))
         (apply f args))))))
