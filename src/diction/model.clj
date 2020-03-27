(ns diction.model
  (:require [clojure.string :as str]
            [diction.core :as diction]
            [diction.documentation :as doc]))

(def diction-types [:double :document :map :entity :enum :float :int :integer
                    :joda :date :datetime :keyword :kw :long :text :string
                    :uuid :vector :list :tuple :poly-vector])

(def diction-literals [:double :float :int :integer :joda :date :datetime :keyword
                       :kw :long :text :string :uuid ])

(def diction-collections [:document :map :entity :enum :vector :list :tuple :poly-vector])

(def dictionary
  [

   {:id :diction-element/id
    :type :keyword
    :meta {:sensible-values [:element1 :element2 :element99]}}

   {:id :diction-element/type
    :type :keyword
    :meta {:sensible-values diction-types}}

   {:id :diction-element/min
    :type :integer
    :meta {:sensible-values [0 1 10 50]}
    :min 0
    :max 1000}

   {:id :diction-element/max
    :type :integer
    :meta {:sensible-values [5 10 50]}
    :min 0
    :max 1000}

   {:id :diction-element/sensible-min
    :clone :diction-element/min}

   {:id :diction-element/sensible-max
    :clone :diction-element/max}

   {:id :diction-element/field
    :clone :diction-element/id}

   {:id :diction-element/fields
    :type :vector
    :meta {:sensible-min 1
           :sensible-max 6}
    :vector-of :diction-element/field}

   {:id :diction-element/required-un
    :clone :diction-element/fields
    :meta {:sensible-values [:id :name :dob]}}

   {:id :diction-element/required
    :clone :diction-element/fields
    :meta {:sensible-values [:ns/id :ns/name :ns/dob]}}

   {:id :diction-element/optional-un
    :clone :diction-element/fields
    :meta {:sensible-values [:alt-phone :tags :twitter :linkedin]}}

   {:id :diction-element/optional
    :clone :diction-element/fields
    :meta {:sensible-values [:ns/alt-phone :ns/tags :ns/twitter :ns/linkedin]}}

   {:id :diction-element/tag
    :meta {:description "Smart tag"
           :sensible-values ["tag1" "tag2" "tag3"
                             "tag4" "tag5" "tag6"
                             "tag99"]}
    :type :string
    :min 1
    :max 32}

   {:id :diction-element/tags
    :meta {:description "Smart tags"
           :sensible-min 1
           :sensible-max 4}
    :type :set
    :set-of :diction-element/tag
    :min 1
    :max 100}

   {:id :diction-element/description
    :type :string
    :meta {:sensible-values ["description1" "description2" "description99"]}
    :min 0
    :max 100}

   {:id :diction-element/regex-pattern
    :type :string
    :meta {:sensible-values ["\\d{7}" "\\d\\s[abc]"]}}

   {:id :diction-element/regex
    :type :any
    :meta {:sensible-values [(re-pattern "\\d{7}")]}}

   {:id :diction-element/label
    :type :string
    :meta {:sensible-values ["label1" "label2" "label99"]}
    :min 1
    :max 100}

   {:id :diction-element/any
    :type :any
    :meta {:sensible-values [nil 0 1.2 "any" true false :any-kw]}}

   {:id :diction-element/sensible-value
    :clone :diction-element/any}

   {:id :diction-element/sensible-values
    :type :vector
    :vector-of :diction-element/sensible-value
    :meta {:sensible-min 1
           :sensible-max 4}}

   {:id :diction-element/meta
    :type :document
    :optional-un [:diction-element/description
                  :diction-element/label
                  :diction-element/sensible-values
                  :diction-element/sensible-min
                  :diction-element/sensible-max
                  :diction-element/tags]}

   {:id :diction-element/enum
    :type :keyword
    :meta {:sensible-values diction-literals}}

   {:id :diction-element/vector-of
    :type :keyword
    :meta {:sensible-values diction-literals}}

   {:id :diction-element/poly-vector-of
    :type :keyword
    :meta {:sensible-values diction-literals}}

   {:id :diction-element/set-of
    :type :keyword
    :meta {:sensible-values diction-literals}}

   {:id :diction-element/tuple
    :type :keyword
    :meta {:sensible-values diction-literals}}

   {:id :diction-element/data-element
    :type :document
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
  []
  (diction/imports! dictionary)
  (spit "data-elements.html"
        (doc/->html filter-out-diction-core-elements
                    {:title "Diction Declarative Data Elements"})))
