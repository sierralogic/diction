(ns diction.example
  (:require [diction.core :refer [int! float! string! uuid! joda! element! document!
                                  enum! inherit! clone! vector! keyword! generate-random-uuid]:as diction]))

;;; inventory

(uuid! ::id)
(int! ::unit-count {:min 0 :max 999})
(float! ::unit-cost {:min 0.0 :max 999.99 :decimal-places 2})
(string! ::label {:min 0 :max 8})
(string! ::description {:min 0 :max 128})
(string! ::tag {:min 2 :max 12})
(vector! ::tags ::tag)
(keyword! ::badge {:max 8})
(joda! ::created)

(float! ::percent-upcharge {:min 0.0 :max 1.0})
(float! ::fee-upcharge {:min 0.0 :max 999.99})
(enum! ::cost-basis #{:flat :per-unit})

(document! ::additional-charges-fee
           [::label ::fee-upcharge ::cost-basis]
           [::description])
(document! ::additional-charges-percent
           [::label ::percent-upcharge ::cost-basis]
           [::description])

(vector! ::feesx ::additional-charges-fee)
(vector! ::percents ::additional-charges-percent)

(clone! ::feesx ::fees)

(document! ::additional-charges
           []
           [::fees ::percents])

(document! ::item
           [::id ::label ::unit-count ::unit-cost]
           [::badge ::tags ::description ::additional-charges])
