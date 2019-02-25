# diction

Diction is a Clojure library similar to spec functionality with added functionality and some batteries included.

## Status of Diction

Diction is currently headed for first release some time in end Q1/start Q2 2019.

## Rationale

Diction supports most of the functionality of `spec` but with with built-in types, convenience functions, off-line
data definitions, import/export, and some other rich features for which `spec` was never really designed/intended for.
 
## Features

- data dictionary
  - batteries-included baked-in default types
    - primitives: string, int, long, float, double
    - collections: enum, vector
    - rich types: uuid, joda
    - composite: documents/entities
  - declarative and/or rich definition
  - basic validation
  - descriptive explanation of validation failures
  - generation
  - grooming
  - metadata
    - audit
    - personal identifiable information (PII) tracking at element level
    - labels and descriptions
    - custom
- flexibility
  - leverages functions, not macros, to allow for runtime diction processing
  - diction elements may be defined completely declaratively (data/declarative vs. code/programmatic)
  - allows for off-line storage of diction definitions in files
  - exports diction definitions to files
  - imports diction definitions from files  
- custom diction types
  - supports custom diction elements
  - create custom diction validation functions
  - create custom diction value generator functions
- extensible data rules (function-based)
  - custom rich functional/programmatic rules registered to elements
  - fires upon validation
- generative function testing
  - register function, argument diction(s), and result diction
  - generative function testing using the data generated by the diction elements

## Planned Features

- integration with document-oriented NoSQL stores (MongoDB, AWS DocumentDB, Codax, ?)
- predicate logic in declarative diction element definitions (simliar to `spec`)
- rich meta data handling for compliance and data element awareness

## Example Code

```clojure
;; defines label as a string with a minimum length of 0 and max length of 8
;; w/ built-in validation and generation
(string! ::label {:min 0 :max 8})

;; defines unit-count as a long (default whole numbers in Clojure) with a min of 0 and max of 999.
;; w/ built-in validation and generation
(long! ::unit-count {:min 0 :max 999})

;; defines unit-cost as a double (default real numbers in Clojure) with a min of 0.01 and max of 99.99.
;; w/ built-in validation and generation
(double! ::unit-cost {:min 0.01 :max 99.99})

(string! ::tag {:min 2 :max 12})

;; defines tags as a vector of diction element `::tag` with min item count of 0 and max of 64.
;; w/ built-in validation and generation
(vector! ::tags ::tag {:min 0 :max 8})

;; defines badge as a keyword diction element with max length of 8.
;; w/ built-in validation and generation
(keyword! ::badge {:max 8})

;; defines created as a joda datetime element
;; w/ built-in validation and generation
(joda! ::created)

;; defines id as a specialized string type uuid
;; w/ built-in validation and generation
(uuid! ::id)

;; defines cost-bases as an enum of keywords
;; w/ built-in validation and generation
(enum! ::cost-basis #{:flat :per-unit})

(string! ::description {:min 0 :max 128})
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

;; defintes an item diction element with required un-namespaced
;; ::id ... ::unit-cost, and optional un-namespaced ::badge ... ::additional-charges
;; note: the document allows for nest documents (like ::additional-charges)
(document! ::item
           [::id ::label ::unit-count ::unit-cost]
           [::badge ::tags ::description ::additional-charges])

```
---
```clojure
(generate ::item)
;=>
{:id "da97d0b6-9c1d-495b-5367-c23da019dc01",
 :label "EXJIJS",
 :unit-count 444,
 :unit-cost 75.0581280630676,
 :badge :VHObB,
 :tags ["8zO" "y6n" "q4oH" "+ bb" "ny7LZO6" "ru" "F4vi8nNSo5o"],
 :description "19Ti7Ekh6wassjon3RJ=jmBhsX-bLygGAy6pcGl3F6KM3",
 :additional-charges {:fees [{:label "Ut",
                              :fee-upcharge 193.5841860568779,
                              :cost-basis :flat,
                              :description "HwGpoD7o96pGsM7N2mqq7To2fHfq7=ekjHxO+KYK-rQE=NFy-49Q7uIFGl gIaT24I7CXf6D1XCMlYQ9sVKX Z2r4u35cPvmMmMz44og=Gyirque9D=x=S7-Fd L"}
                             {:label "fD7AXP",
                              :fee-upcharge 350.57367256651224,
                              :cost-basis :per-unit,
                              :description "PswklPNDFbJS0l9QrBCnpT6I=PEM41Hq5B9luas2er167BnJRdO72 GjNIhvs07uPcNDwBr2azvf_cuVLQ3TEYmLRpT2z2U"}
                             {:label "", 
                              :fee-upcharge 286.83015039087127, 
                              :cost-basis :flat}]}}

```
---
```clojure
(explain ::tag "t")
;=>
[{:id :diction.example/tag,
  :entry {:id :diction.example/tag,
          :element {:id :diction.example/tag,
                    :type :string,
                    :min 2,
                    :max 12,
                    :gen-f #object[diction.core$wrap_gen_f$fn__4572
                                   0x37e5f92c
                                   "diction.core$wrap_gen_f$fn__4572@37e5f92c"],
                    :valid-f #object[clojure.core$partial$fn__5565 0x424f7cf2 "clojure.core$partial$fn__5565@424f7cf2"],
                    :parent-id :diction/string}},
  :v "t",
  :msg "Failed ':diction.example/tag': value 't' has too few characters. (min=2)"}]
```
---
```clojure
(explain ::item {:id "da97d0b6-9c1d-495b-5367-c23da019dc01",
                 :label "EXJIJS",
                 :unit-count 33,
                 :unit-cost 75.0581280630676,
                 :badge :VHObB,
                 :tags ["8dsdfasdfs" "y6n" "q4oH" "+ bb" "ny7LZO6" "ru" "F4vi8nNSo5o"],
                 :description "19Ti7Ekh6wassjon3RJ=jmBhsX-bLygGAy6pcGl3F6KM3",
                 :additional-charges {:fees [{:label "Ut",
                                              :fee-upcharge 193.5841860568779,
                                              :cost-basis :flat,
                                              :description "HwGpoD7o96pGsM7N2mqq7To2fHfq7=ekjHxO+KYK-rQE=NFy-49Q7uIFGl gIaT24I7CXf6D1XCMlYQ9sVKX Z2r4u35cPvmMmMz44og=Gyirque9D=x=S7-Fd L"}
                                             {:label "fD7AXP",
                                              :fee-upcharge 350.57367256651224,
                                              :cost-basis :per-unit-bad-xxx,  ;; bad cost-basis enum
                                              :description "PswklPNDFbJS0l9QrBCnpT6I=PEM41Hq5B9luas2er167BnJRdO72 GjNIhvs07uPcNDwBr2azvf_cuVLQ3TEYmLRpT2z2U"}
                                             {:label "",
                                              :fee-upcharge 286.83015039087127,
                                              :cost-basis :flat}]}})
;=>
[{:id :diction.example/cost-basis,
  :entry {:id :diction.example/cost-basis,
          :element {:id :diction.example/cost-basis,
                    :enum #{:flat :per-unit},
                    :gen-f #object[diction.core$wrap_gen_f$fn__5476
                                   0x6d164c9e
                                   "diction.core$wrap_gen_f$fn__5476@6d164c9e"],
                    :valid-f #object[clojure.core$partial$fn__5561 0x5be3e4b7 "clojure.core$partial$fn__5561@5be3e4b7"]}},
  :msg "Failed :diction.example/cost-basis: value ':per-unit-bad-xxx' not in enum '#{:flat :per-unit}'.",
  :parent-element-id [:diction.example/additional-charges-fee :diction.example/additional-charges :diction.example/item]}]

```
---
```clojure
(explain ::item {:id "da97d0b6-9c1d-495b-5367-c23da019dc01",
                 :label "EXJIJS",
                 :unit-count 33,
                 :unit-cost 75.0581280630676,
                 :badgex :VHObB,
                 :tags ["8dsdfasdfs" "y6n" "q4oH" "+ bb" "ny7LZO6" "ru" "F4vi8nNSo5o"],
                 :description "19Ti7Ekh6wassjon3RJ=jmBhsX-bLygGAy6pcGl3F6KM3",
                 :additional-charges {:fees [{:label "Ut",
                                              :fee-upcharge 193.5841860568779,
                                              :cost-basis :flat,
                                              :description "HwGpoD7o96pGsM7N2mqq7To2fHfq7=ekjHxO+KYK-rQE=NFy-49Q7uIFGl gIaT24I7CXf6D1XCMlYQ9sVKX Z2r4u35cPvmMmMz44og=Gyirque9D=x=S7-Fd L"}
                                             {:label "fD7AXP",
                                              :fee-upcharge 350.57367256651224,
                                              :cost-basis :per-unit,  ;; bad cost-basis enum
                                              :description "PswklPNDFbJS0l9QrBCnpT6I=PEM41Hq5B9luas2er167BnJRdO72 GjNIhvs07uPcNDwBr2azvf_cuVLQ3TEYmLRpT2z2U"}
                                             {:label "",
                                              :fee-upcharge 286.83015039087127,
                                              :cost-basis :flat}]}})
;=> 
[{:msg "Item must have a :badge field if the :tags field is present."}]



```

## License

Copyright © 2019 SierraLogic LLC

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
