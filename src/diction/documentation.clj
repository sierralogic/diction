(ns diction.documentation
  (:require [clojure.string :as str]
            [diction.core :as diction]))

(def spacing 2)

(def spaces (str/join (repeat 500 " ")))

(defn indent
  [n]
  (subs spaces 0 (* n spacing)))

(defn entity-report
  ([] (entity-report nil))
  ([entity-ids]

    ))

(defn metadata-report
  ([] (metadata-report nil))
  ([element-ids]
    ))

(defn bullet-line
  "Generates a bullet line by concatenating a line to the `acc` accumulator
  string with indentions to the appropriate `level` and the line string `s`."
  [acc level s]
  (str acc "\n" (indent level) "- " s))

(def document-element-keys [:required-un :optional-un :required :optional])

(defn document-children-element-ids
  "Retrieves children element ids from `element-info`."
  [element-info]
  (reduce #(concat % (get-in element-info [:element %2]))
          []
          document-element-keys))

(defn ->markdown
  "Converts the element `element-id` to markdown with optional `level` (how
  nested the elements are in the context of the start), and accumulator `acc`."
  ([element-id] (->markdown element-id 0 nil))
  ([element-id level] (->markdown element-id level nil))
  ([element-id level acc]
   (if-let [elem (diction/info element-id)]
     (let [lbl (get-in elem [:element :meta :label] (diction/labelize element-id))
           etype (when-let [typ (get-in elem [:element :type])] (diction/labelize typ))
           nlbl (str lbl (when etype (str " (" etype ")")))
           ss (bullet-line acc level nlbl)
           document? (diction/document? elem)
           child-elements (when document? (document-children-element-ids elem))
           next-level (inc level)]
       (println :doc/->md :lbl lbl :nlvl next-level :ss ss)
       (if document?
         (str ss (reduce #(str (->markdown %2 next-level %))
                         nil
                         child-elements))
         ss))
     (str acc "\n" (indent level) "- Element '" element-id "' not found."))))
