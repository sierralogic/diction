(ns diction.example
  (:require [diction.core :refer [generate int! float! string! uuid! joda! element! document!
                                  enum! inherit! clone! double! long! vector! keyword! generate-random-uuid]
             :as diction]))

(string! ::label {:min 0 :max 8})

(string! ::description {:min 0 :max 128})

(long! ::unit-count {:min 0 :max 999})
(double! ::unit-cost {:min 0.01 :max 99.99})

(string! ::tag {:min 2 :max 12})

(vector! ::tags ::tag)

(keyword! ::badge {:max 8})

(joda! ::created)

(double! ::percent-upcharge {:min 0.0 :max 1.0})

(double! ::fee-upcharge {:min 0.0 :max 999.99})

(uuid! ::id)

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

