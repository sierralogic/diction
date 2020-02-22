(ns diction.example
  (:require [clojure.walk :as walk]
            [diction.core :refer [generate explain int! float! string! uuid! joda! element! document! validation-rule!
                                  explain-all meta-query groom valid-all? groom function! test-function test-all-functions
                                  lookup enum! inherit! clone! double! long! vector! keyword!]
             :as diction]
            [diction.util :as util]))

;;; Elements ==============================================================

;; defines label as a string with a minimum length of 0 and max length of 8
;; w/ built-in validation and generation
(string! ::label {:min 0 :max 8 :meta {:sensible-values ["labl1" "lable2" "another label"]}})

;; defines unit-count as a long (default whole numbers in Clojure) with a min of 0 and max of 999.
;; w/ built-in validation and generation
(long! ::unit-count {:min 0 :max 999})

;; defines unit-cost as a double (default real numbers in Clojure) with a min of 0.01 and max of 99.99.
;; w/ built-in validation and generation
(double! ::unit-cost {:min 0.01 :max 99.99})

(string! ::tag {:min 2 :max 6 :meta {:sensible-values ["tag1" "tag2" "taga" "tagb"]}})

;; defines tags as a vector of diction element `::tag` with min item count of 0 and max of 64.
;; w/ built-in validation and generation
(vector! ::tags ::tag {:min 0 :max 8})

;; defines badge as a keyword diction element with max length of 8.
;; w/ built-in validation and generation
(keyword! ::badge {:max 8 :meta {:sensible-values [:badge2 :badge4 :b6 :b8 :b234]}})

;; defines created as a joda datetime element
;; w/ built-in validation and generation
(joda! ::created)

;; defines id as a specialized string type uuid
;; w/ built-in validation and generation
(uuid! ::id)

;; defines cost-bases as an enum of keywords
;; w/ built-in validation and generation
(enum! ::cost-basis #{:flat :per-unit})

(string! ::description {:min 0 :max 128 :meta {:sensible-values ["here is a description" "another description" "yet another descriptive narrative"]}})
(double! ::percent-upcharge {:min 0.0 :max 1.0})
(double! ::fee-upcharge {:min 0.0 :max 999.99})

;; defines additional-charges-fee as a document
;; convenience function `document!`
;; takes the diction element id,
;; then the unqualified required diction element id(s),
;; then the unqualified optional diction element id(s)
(document! ::additional-charges-fee
           [::label ::fee-upcharge ::cost-basis]
           [::description])

(document! ::additional-charges-percent
           [::label ::percent-upcharge ::cost-basis]
           [::description])

(vector! ::fees-template ::additional-charges-fee)
(vector! ::percents ::additional-charges-percent)

;; clones diction element fees-template to new diction element fees
;; w/ built-in validation and generation
(clone! ::fees-template ::fees)

(document! ::additional-charges
           []
           [::fees ::percents])

;; defines an item diction element with required un-namespaced
;; ::id ... ::unit-cost, and optional un-namespaced ::badge ... ::additional-charges
;; note: the document allows for nest documents (like ::additional-charges)
(document! ::item
           [::id ::label ::unit-count ::unit-cost]
           [::badge ::tags ::description ::additional-charges])

;;; Rules ================================================================

(defn rule-item-if-tags-then-badge-required
  [value entry validation-rule context]
  (when (:tags value)
    (when-not (:badge value)
      [{:id (:element-id validation-rule)
        :rule-id (:id validation-rule)
        :msg (str "Item must have a :badge field if the :tags field is present.")}])))

(validation-rule! ::item ::rule-item-if-tags-then-badge rule-item-if-tags-then-badge-required)

(explain-all ::item nil)

;;; Functions ===========================================================

(defn sum-long-and-double
  [l d]
  (str (+ l d)))

(long! ::arg-long)
(double! ::arg-double)
(string! ::result-string)

(function! :sum-long-and-double
           sum-long-and-double
           [::arg-long ::arg-double]
           ::result-string)

(test-function :sum-long-double 999)

;;; Decoration Rules -==============================================================

;(defn sample-decoration-rule
;  [value entry decoration-rule ctx]
;  value
;  )

(defn decoration-rule-calc-item-inventory-retail-worth
  [v entry rule ctx]
  (assoc v :inventory-retail-worth (* (get v :unit-count 0) (get v :unit-cost 0.0))))

(defn decoration-rule-inventory-str
  [v entry rule ctx]
  (assoc v :inventory-str (str "There are "
                               (get v :unit-count "unk")
                               " "
                               (get v :label "unk")
                               " item(s) [" (get v :id "-") "] "
                               "with an inventory retail value of "
                               (* (get v :unit-count 0) (get v :unit-cost 0.0))
                               " USD.")))

(defn decoration-rule-keys-to-snake
  [v entry rule ctx]
  (util/snake-keys v))

(diction/decoration-rule! ::item
                          :calculate-item-inventory-retail-worth
                          decoration-rule-calc-item-inventory-retail-worth)

(diction/decoration-rule! ::item
                          :item-inventory-str
                          decoration-rule-inventory-str)

(diction/decoration-rule! ::item
                          :item-keys-to-strings
                          decoration-rule-keys-to-snake)

(defn undecoration-rule-keys-to-skewer
  [v entry rule ctx]
  (util/skewer-keys v))

(diction/undecoration-rule! ::item
                            :item-keys-to-skewer
                            undecoration-rule-keys-to-skewer)


;;; Meta Queries

(int! ::ans {:meta {:pii true :rank 3 :label "answer" :foo "bars"}})
(string! ::mercy {:meta {:pii true :rank 2 :label "merciful" :desc "what?"}})
(string! ::wonk {:meta {:pii false :rank 3 :label "wonky" :sensible-values ["Wink" "Wonky" "Willy" "Charlie"]}})
(double! ::tau {:meta {:label "tau baby" :rank 4 :pii true :foo false :sensible-values [43.2 44.3]}})

;; dev

(def xuid "d0b6879a-fd1a-4ec0-9708-8118d43286d9")
