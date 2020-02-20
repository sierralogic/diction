(ns diction.documentation
  (:require [clojure.string :as str]
            [diction.core :as diction])
  (:import (java.util Date)))

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

(defn ->markdown-old
  "Converts the element `element-id` to markdown with optional `level` (how
  nested the elements are in the context of the start), and accumulator `acc`."
  ([element-id] (->markdown-old element-id 0 nil))
  ([element-id level] (->markdown-old element-id level nil))
  ([element-id level acc]
   (if-let [elem (diction/info element-id)]
     (let [lbl (get-in elem [:element :meta :label] (diction/labelize element-id))
           etype (when-let [typ (get-in elem [:element :type])] (diction/labelize typ))
           nlbl (str lbl (when etype (str " (" etype ")")))
           ss (bullet-line acc level nlbl)
           document? (diction/document? elem)
           child-elements (when document? (document-children-element-ids elem))
           next-level (inc level)]
       (if document?
         (str ss (reduce #(str (->markdown-old %2 next-level %))
                         nil
                         child-elements))
         ss))
     (str acc "\n" (indent level) "- Element '" element-id "' not found."))))

(def exclude-element-fields [:gen-f :valid-f :optional-un-m :required-un-m :required-m :optional-m :regex])

(defn ->markdown
  ([] (->markdown nil))
  ([title]
   (let [dd (diction/data-dictionary)
         ntitle (or title "Data Dictionary")
         generated (Date.)
         header (str "# " ntitle "\n*generated: " generated "*\n\n")
         {:keys [summary fields documents]} dd
         md (reduce #(let [eid (:id %2 "unk")
                           lbl (diction/labelize (:label %2 eid))
                           typ (diction/labelize (:type %2 "unk"))
                           lblt (str lbl) ;  " (" typ ")")
                           hdr (str % "### " lblt "\n")
                           kys (sort (keys %2))
                           sflds (reduce (fn [a k]
                                           (str a "- " (diction/labelize k) " : " (get %2 k) "\n"))
                                         ""
                                         kys)]
                       (str hdr sflds "\n"))
                    (str header "## Summary\n\n")
                    summary)

         dmds (reduce #(let [eid (:id %2 "unk")
                             elem (:element %2)
                             nelem (apply dissoc (cons elem exclude-element-fields))
                             lbl (diction/labelize (:label %2 eid))
                             sks (sort (keys nelem))
                             hdr (str % "### " lbl "\n")
                             dmd (reduce (fn [a k]
                                           (str a "- " (diction/labelize k) " : " (get nelem k) "\n"))
                                         ""
                                         sks)]
                         (str hdr dmd "\n"))
                      (str md "## Documents\n\n")
                      documents)
         fmds (reduce #(let [eid (:id %2 "unk")
                             elem (:element %2)
                             nelem (apply dissoc (cons elem exclude-element-fields))
                             lbl (diction/labelize (:label %2 eid))
                             sks (sort (keys nelem))
                             hdr (str % "### " lbl "\n")
                             dmd (reduce (fn [a k]
                                           (str a "- " (diction/labelize k) " : " (get nelem k) "\n"))
                                         ""
                                         sks)]
                         (str hdr dmd "\n"))
                      (str dmds "## Fields\n\n")
                      fields)]
     fmds)))
