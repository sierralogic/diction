(ns diction.documentation
  (:require [clojure.string :as str]
            [diction.core :as diction]
            [diction.util :refer [labelize] :as util])
  (:import (java.util Date)))

(def kv-sep ": ")
(def bullet "- ")

(def spacing 2)

(def spaces (str/join (repeat 500 " ")))

(defn ->str
  "Converts `x` to string."
  [x]
  (if (keyword? x)
    (-> x str (subs 1))
    (-> x
        str)))

(defn ->anchor
  "Converts `x` to anchor name."
  [x]
  (-> x
      ->str
      str/trim
      str/lower-case
      (str/replace " " "_")
      (str/replace "/" "_")
      (str/replace "-" "_")))

(defn local-link
  [x]
  (str "<a href=\"#" (->anchor x) "\">" (->str x) "</a>"))

(defn indent
  "Generates spaces for indenting line to level `n`."
  [n]
  (subs spaces 0 (* n spacing)))

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

(def exclude-element-fields [:gen-f :valid-f :optional-un-m :required-un-m :required-m :optional-m :regex])

(def reference-keys #{:fields :vector-of :set-of :tuple :id
                      :required-fields :required-unqualified-fields
                      :required :required-un
                      :optional-fields :optional-unqualified-fields
                      :optional :optional-un
                      :referenced-by})

(defn element-bullet
  "Generate element bullet for key `k` and value `v` and optional set `ref-ks`
  that add local link refs if key `k` in `ref-ks`."
  ([k v] (element-bullet nil k v))
  ([ref-ks k v]
   (let [pre (str bullet "**" (labelize k) "**" kv-sep)
         xref-ks (or ref-ks reference-keys)]
     (if (contains? xref-ks k)
       (if (util/vls? v)
         (str pre (reduce (fn [a2 vv]
                            (str a2 " " (local-link vv)))
                          "["
                          v)
              " ]")
         (str pre (local-link v)))
       (str pre v)))))

(defn ->markdown-elements
  "Generate markdown elements `elems` given existing string `body` and
  `title` of the section."
  [elems body title]
  (reduce #(let [eid (:id %2 "unk")
                 elem (:element %2)
                 nelem (apply dissoc (cons elem exclude-element-fields))
                 lbl (labelize (:label %2 eid))
                 sks (sort (keys nelem))
                 hdr (str % "<a name=\"" (->anchor eid) "\"></a>\n\n### " lbl "\n\n")
                 dmd (reduce (fn [a k]
                               (str a (element-bullet k (get nelem k)) "\n"))
                             ""
                             sks)]
             (str hdr dmd "\n"))
          (str body "## " title "\n\n")
          elems))


(defn ->markdown
  "Convert existing data dictionary into markdown with optional `title`."
  ([] (->markdown nil))
  ([title]
   (let [dd (diction/data-dictionary)
         ntitle (or title "Data Dictionary")
         generated (Date.)
         header (str "# " ntitle "\n*generated: " generated "*\n\n")
         {:keys [summary fields documents]} dd
         md (reduce #(let [eid (:id %2 "unk")
                           lbl (labelize (:label %2 eid))
                           hdrx (str % "<a href=\"#" (->anchor eid) "\">\n\n### " lbl "\n</a>\n\n")
                           hdr (str % "\n### " lbl "\n\n")
                           kys (sort (keys %2))
                           sflds (reduce (fn [a k]
                                           (str a (element-bullet k (get %2 k)) "\n"))
                                         ""
                                         kys)]
                       (str hdr sflds "\n"))
                    (str header "## Summary\n\n")
                    summary)
         dmds (->markdown-elements documents md "Documents")
         fmds (->markdown-elements fields dmds "Fields")]
     fmds)))
