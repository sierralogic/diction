(ns diction.model
  (:require [clojure.string :as str]
            [diction.core :as diction]
            [diction.documentation :as doc]))

(def diction-types
  "Core diction data element types."
  [:double :document :map :entity :enum :float :int :integer
   :joda :date :datetime :keyword :kw :long :text :string
   :uuid :vector :list :tuple :poly-vector])

(def diction-literals
  "Core diction literal data element types."
  [:double :float :int :integer :joda :date :datetime :keyword
   :kw :long :text :string :uuid ])

(def diction-collections
  "Core diction collection/list data element types."
  [:document :map :entity :enum
   :vector :list :tuple :poly-vector])

(def dictionary
  "Core diction data elements."
  [

   {:id :diction-element/id
    :type :keyword
    :meta {:description "The data element id."
           :sensible-values [:element1 :element2 :element99]}}

   {:id :diction-element/type
    :type :keyword
    :meta {:description "The data element type (e.g. `:string` or `:double`)."
           :sensible-values diction-types}}

   {:id :diction-element/min
    :type :integer
    :meta {:description "The minimum length or value of a field."
           :sensible-values [0 1 10 50]}
    :min 0
    :max 1000}

   {:id :diction-element/max
    :type :integer
    :meta {:description "The maximum length or value of a field."
           :sensible-values [5 10 50]}
    :min 0
    :max 1000}

   {:id :diction-element/sensible-min
    :clone :diction-element/min
    :meta {:description "The minimum length or value of a sensible field."}}

   {:id :diction-element/sensible-max
    :clone :diction-element/max
    :meta {:description "The maximum length or value of a sensible field."}}

   {:id :diction-element/field
    :clone :diction-element/id}

   {:id :diction-element/fields
    :type :vector
    :meta {:sensible-min 1
           :sensible-max 6}
    :vector-of :diction-element/field}

   {:id :diction-element/required-un
    :clone :diction-element/fields
    :meta {:description "Required unqualified elements (un-namespaced ids) for a document/entity."
           :sensible-values [[:id :name :dob] [:id :label :active]]}}

   {:id :diction-element/required
    :clone :diction-element/fields
    :meta {:description "Required qualified elements (namespaced ids) for a document/entity."
           :sensible-values [[:ns/id :ns/name :ns/dob] [:ns/id :ns/label :ns/active]]}}

   {:id :diction-element/optional-un
    :clone :diction-element/fields
    :meta {:description "Optional unqualified elements (un-dnamespaced ids) for a document/entity."
           :sensible-values [[:alt-phone :tags :twitter :linkedin] [:email :alt-email :address2]]}}

   {:id :diction-element/optional
    :clone :diction-element/fields
    :meta {:description "Optional qualified elements (namespaced ids) for a document/entity."
           :sensible-values [[:ns/alt-phone :ns/tags :ns/twitter :ns/linkedin] [:ns/email :ns/alt-email :ns/address2]]}}

   {:id :diction-element/tag
    :meta {:description "A textual tag element."
           :sensible-values ["tag1" "tag2" "tag3"
                             "tag4" "tag5" "tag6"
                             "tag99"]}
    :type :string
    :min 1
    :max 32}

   {:id :diction-element/tags
    :meta {:description "A list of tags element."
           :sensible-min 1
           :sensible-max 4}
    :type :set
    :set-of :diction-element/tag
    :min 1
    :max 100}

   {:id :diction-element/description
    :type :string
    :meta {:description "Description of element."
           :sensible-values ["description1" "description2" "description99"]}
    :min 0
    :max 100}

   {:id :diction-element/regex-pattern
    :type :string
    :meta {:description "Regular expression pattern as a string."
           :sensible-values ["\\d{7}" "\\d\\s[abc]"]}}

   {:id :diction-element/regex
    :type :any
    :meta {:description "Regular expression as a Pattern object."
           :sensible-values [(re-pattern "\\d{7}")]}}

   {:id :diction-element/label
    :type :string
    :meta {:description "Label of an element."
           :sensible-values ["label1" "label2" "label99"]}
    :min 1
    :max 100}

   {:id :diction-element/any
    :type :any
    :meta {:description "Any element type.  Used for supporting unknown types."
           :sensible-values [nil 0 1.2 "any" true false :any-kw]}}

   {:id :diction-element/sensible-value
    :meta {:description "Sensible value used for generated, human-readable documentation of the element."}
    :clone :diction-element/any}

   {:id :diction-element/sensible-values
    :type :vector
    :vector-of :diction-element/sensible-value
    :meta {:description "Sensible values of the data element for generated, human-readable documentation."
           :sensible-min 1
           :sensible-max 4}}

   {:id :diction-element/meta
    :type :document
    :meta {:description "Metadata of the element with important hints and settings."}
    :optional-un [:diction-element/description
                  :diction-element/label
                  :diction-element/sensible-values
                  :diction-element/sensible-min
                  :diction-element/sensible-max
                  :diction-element/tags]}

   {:id :diction-element/enum
    :type :keyword
    :meta {:description "An enumerated data element, meaning only explicit list of values allowed."
           :sensible-values diction-literals}}

   {:id :diction-element/vector-of
    :type :keyword
    :meta {:description "A vector/list data element of single type."
           :sensible-values diction-literals}}

   {:id :diction-element/poly-vector-of
    :type :keyword
    :meta {:description "A vector/list data element of one or more types."
           :sensible-values diction-literals}}

   {:id :diction-element/set-of
    :type :keyword
    :meta {:description "A set data element (with no duplicated) of a single type."
           :sensible-values diction-literals}}

   {:id :diction-element/tuple
    :type :keyword
    :meta {:description "A tuple (list) of a single type."
           :sensible-values diction-literals}}

   {:id :diction-element/data-element
    :type :document
    :meta {:description "The declarative data element map used to register a data element."}
    :required-un [:diction-element/id :diction-element/type]
    :optional-un [:diction-element/required :diction-element/required-un
                  :diction-element/optional :diction-element/optional-un
                  :diction-element/meta :diction-element/min :diction-element/max
                  :diction-element/vector-of :diction-element/set-of :diction-element/poly-vector-of
                  :diction-element/enum :diction-element/tuple
                  :diction-element/regex-pattern :diction-element/regex]}
   ])

(defn filter-out-diction-core-elements
  "Filters out diction elements given element `x`."
  [x]
  (if-let [id (:id x)]
    (if (keyword? id)
      (if-let [ns (namespace id)]
        (and (not= ns "diction")
             (not= ns "diction.core"))
        true)
      true)
    true))

(defn generate
  "Imports the core diction data element dictionary and then generates `elements.html`
  and `elements.md` core data element documentation in root folder."
  []
  (diction/imports! dictionary)
  (spit "elements.html"
        (doc/->html filter-out-diction-core-elements
                    {:title "Diction Declarative Data Elements"}))
  (spit "elements.md"
        (doc/->markdown filter-out-diction-core-elements
                        "Diction Declarative Data Elements")))
