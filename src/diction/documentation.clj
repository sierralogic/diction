(ns diction.documentation
  (:require [clojure.string :as str]
            [diction.core :as diction]
            [diction.util :refer [labelize ->json] :as util])
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

(def exclude-element-fields [:meta :gen-f :valid-f :optional-un-m :required-un-m :required-m :optional-m :regex :summary])

(def reference-keys #{:fields :vector-of :set-of :tuple :id
                      :required-fields :required-unqualified-fields
                      :required :required-un
                      :reference
                      :optional-fields :optional-unqualified-fields
                      :optional-unqualified
                      :required-unqualified
                      :optional :optional-un
                      :referenced-by})

(defn doc-value
  "Wrap value `x` with double-quotes if `x` is string."
  [x]
  (if (string? x)
    (str "\"" x "\"")
    (->str x)))

(defn decorate-value
  "Decorate value `v`.  Currently passes through maps, but comma-delimits collections."
  ([v] (decorate-value nil v))
  ([vf v]
   (when-not (nil? v)
     (let [nvf (or vf doc-value)]
       (if (util/vls? v)
         (let [nv (vec v)]
           (str (reduce (fn [a2 n]
                          (str a2 (if (> n 0) ", " " ") (nvf (util/safe-nth nv n))))
                        "["
                        (range (count nv)))
                " ]"))
         (nvf v))))))

(def decorate-local-link-value (partial decorate-value local-link))

(defn element-bullet
  "Generate element bullet for key `k` and value `v` and optional set `ref-ks`
  that add local link refs if key `k` in `ref-ks`."
  ([k v] (element-bullet nil k v))
  ([ref-ks k v]
   (let [pre (str bullet "**" (util/replace-labels (labelize k)) "**" kv-sep)
         xref-ks (or ref-ks reference-keys)]
     (if (contains? xref-ks k)
       (str pre (decorate-local-link-value v))
       (str pre (decorate-value v))))))

(defn index-bullet
  "Generate index bullet for key `id` and optional label `lbl`."
  ([id] (index-bullet id nil))
  ([id lbl]
   (str bullet (decorate-local-link-value id)
        (when lbl (str " (" lbl ")")))))

(defn element-bullet-legacy
  "Generate element bullet for key `k` and value `v` and optional set `ref-ks`
  that add local link refs if key `k` in `ref-ks`."
  ([k v] (element-bullet-legacy nil k v))
  ([ref-ks k v]
   (let [pre (str bullet "**" (labelize k) "**" kv-sep)
         xref-ks (or ref-ks reference-keys)]
     (if (contains? xref-ks k)
       (if (util/vls? v)
         (str pre (reduce (fn [a2 n]
                            (str a2 (if (> n 0) ", " " ") (local-link (util/safe-nth v n))))
                          "["
                          (range (count v)))
              " ]")
         (str pre (local-link v)))
       (str pre v)))))

(defn map->markdown
  "Convert meta `meta` for element `id` with optional `level`."
  ([m] (map->markdown nil m))
  ([level m]
  (when (map? m)
    (let [idt (indent (or level 1))]
      (reduce-kv (fn [a k v]
                   (str a idt (element-bullet k v) "\n"))
                 "\n"
                 m)))))

(defn index->markdown
  "Generate index markdown using data dictionary `fields`."
  [fields]
  (reduce #(let [{:keys [element id]} %2
                 lbl (get-in element [:meta :label])]
             (str % (index-bullet id lbl) "\n"))
          ""
          fields))

(defn generate-all
  "Generates an element `element-id` with forced `sensible-values` to `true`
  and `generate-all-fields` to `true`."
  [element-id]
  (binding [diction/*force-sensible* true
            diction/*generate-all-fields* true]
    (diction/generate element-id)))

(defn ->markdown-elements
  "Generate markdown elements `elems` given existing string `body` and
  `title` of the section."
  [elems body title]
  (reduce #(let [eid (:id %2 "unk")
                 elem (:element %2)
                 nelem (apply dissoc (cons elem exclude-element-fields))
                 lbl (labelize (:label %2 eid))
                 sks (sort (keys nelem))
                 meta (:meta elem)
                 metas (when meta
                         (str bullet "**" (labelize :meta) "**" kv-sep
                              (map->markdown meta)))
                 hdr (str % "<a name=\"" (->anchor eid) "\"></a>\n\n### " lbl "\n\n")
                 dmd (reduce (fn [a k]
                               (str a (element-bullet k (get nelem k)) "\n"))
                             ""
                             sks)
                 document? (= (:type elem) :document)
                 doc-note (when document? " (required and optional field(s))")
                 examplex (binding [diction/*force-sensible* true
                                   diction/*generate-all-fields* true]
                           (diction/generate eid))
                 example (if document? (into (sorted-map) examplex)
                                       examplex)
                 normalized-example (->json example)
                 dmdx (str dmd
                           "- **Example**:" doc-note "\n"
                           "```json\n"
                           normalized-example
                           "\n```\n")]
             (str hdr metas dmdx "\n"))
          (str body "## " title "\n\n")
          elems))

(defn ->markdown
  "Convert existing data dictionary into markdown with optional `title`."
  ([] (->markdown nil))
  ([title]
   (let [dd (binding [diction/*force-sensible* true]
              (diction/data-dictionary))
         ntitle (or title "Data Dictionary")
         generated (Date.)
         headerx (str "# " ntitle "\n*generated: " generated "*\n\n")
         {:keys [summary fields documents]} dd
         document-index (str "## Document Index\n\n" (index->markdown documents) "\n")
         field-index (str "## Field Index\n\n" (index->markdown fields) "\n")
         header (str headerx document-index field-index "\n")
         md (reduce #(let [eid (:id %2 "unk")
                           lbl (labelize (:label %2 eid))
                           ; hdrx (str % "<a href=\"#" (->anchor eid) "\">\n\n### " lbl "\n</a>\n\n")
                           hdr (str % "\n### " lbl "\n\n")
                           kys (sort (keys %2))
                           sflds (reduce (fn [a k]
                                           (str a (element-bullet k (get %2 k)) "\n"))
                                         ""
                                         kys)]
                       (str hdr sflds "\n"))
                    (str header "## Summary\n\n")
                    summary)
         dmds (->markdown-elements documents header "Documents")
         fmds (->markdown-elements fields dmds "Fields")]
     fmds)))
