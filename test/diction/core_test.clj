(ns diction.core-test
  (:require [clj-time.core :as t]
            [clojure.test :refer :all]
            [diction.core :refer :all]))

(def valid-values {:int (int 42)
                   :long 4242
                   :float (float 9.99)
                   :double 99.99
                   :string "this is a string"
                   :map {:foo :bar}
                   :vector [1 2 3]
                   :list '(10 20 30)
                   :set #{100 200 300}
                   :joda (t/now)
                   :keyword :foo})

(defn assert-valid
  "Convenience function to assert if element `element-id` value `v` is valid."
  [element-id v]
  (is (valid? element-id v)))

(defn assert-invalid
  "Convenience function to assert if element `element-id` value `v` is NOT valid."
  [element-id v]
  (is (not (valid? element-id v))))

(defn test-invalid-values-by-type
  "Tests for invalid element types minus the check `element-id` type `type`."
  [element-id type]
  (is (nil? (reduce-kv #(if (valid? element-id %3)
                          (assoc % %2 (str "Value '" %3
                                           "' should be an invalid '" type
                                           "' for element '" element-id "'."))
                          %)
                       nil
                       (dissoc valid-values type)))))

(def default-generation-validation-cycles 100)

(defn test-generation-validation-loop
  "Test validation of generated element values for element `element-id` using `gen-f` of
  the registered element given option loop cycles `cycles`.  `cycles` defaults to 100."
  ([element-id] (test-generation-validation-loop element-id nil))
  ([element-id cycles]
   (is (nil? (reduce (fn [_ _]
                       (if-let [ex (explain element-id (generate element-id))]
                         (reduced ex)
                         (do
                           (is true)
                           nil)))
                     nil
                     (range (or cycles default-generation-validation-cycles)))))))

(deftest test-built-ins

  (testing "when core built-in numeric types are registered and validated"

    (int! ::ans)
    (assert-valid ::ans (int 42))
    (test-invalid-values-by-type ::ans :int)
    (test-generation-validation-loop ::ans)

    (int! ::rating {:min 1 :max 5})
    (test-generation-validation-loop ::rating)
    (assert-valid ::rating (int 1))
    (assert-valid ::rating (int 2))
    (assert-valid ::rating (int 3))
    (assert-valid ::rating (int 4))
    (assert-valid ::rating (int 5))
    (assert-invalid ::rating (int 0))
    (assert-invalid ::rating (int -1))
    (assert-invalid ::rating (int 6))
    (assert-invalid ::rating (int 11))

    (long! ::millis)
    (assert-valid ::millis (long (System/currentTimeMillis)))
    (test-invalid-values-by-type ::millis :long)
    (test-generation-validation-loop ::millis)

    (long! ::trust {:min 0 :max 111})
    (test-generation-validation-loop ::trust)
    (assert-valid ::trust 0)
    (assert-valid ::trust 1)
    (assert-valid ::trust 50)
    (assert-valid ::trust 75)
    (assert-valid ::trust 100)
    (assert-valid ::trust 111)
    (assert-invalid ::trust 112)
    (assert-invalid ::trust -112)
    (assert-invalid ::trust -1)
    (assert-invalid ::trust 12933)

    (float! ::price)
    (assert-valid ::price (float 9.99))
    (test-invalid-values-by-type ::price :float)
    (test-generation-validation-loop ::price)

    (float! ::ph {:min 7.0 :max 9.9})
    (test-generation-validation-loop ::ph)
    (assert-valid ::ph (float 7.0))
    (assert-valid ::ph (float 8.32))
    (assert-valid ::ph (float 9.9))
    (assert-invalid ::ph (float 6.99))
    (assert-invalid ::ph (float 9.999))
    (assert-invalid ::ph (float -1.1))
    (assert-invalid ::ph (float 3444.23))

    (double! ::distance)
    (assert-valid ::distance 999.99)
    (test-invalid-values-by-type ::distance :double)
    (test-generation-validation-loop ::distance)

    (double! ::base {:min 1.2 :max 6.9})
    (test-generation-validation-loop ::base)
    (assert-valid ::base 1.2)
    (assert-valid ::base 3.32)
    (assert-valid ::base 6.9)
    (assert-invalid ::base 6.99)
    (assert-invalid ::base 1.1)
    (assert-invalid ::base -1.1)
    (assert-invalid ::base 3444.23)

    )

  (testing "when core build-in string types are registered and validated"

    (string! ::label)
    (assert-valid ::label "this is a label")
    (test-invalid-values-by-type ::label :string)
    (test-generation-validation-loop ::label)

    (string! ::tag {:min 2 :max 10})
    (test-generation-validation-loop ::tag)

    (assert-valid ::tag "ab")
    (assert-valid ::tag "tag")
    (assert-valid ::tag "123456789")
    (assert-valid ::tag "1234567890")

    (assert-invalid ::tag "a")
    (assert-invalid ::tag "1234567890x")

    (string! ::hexx {:regex-pattern "^[a-f0-9]{1,8}$" :rzzegexx #"^[a-f0-9]{1,8}$"})
    (assert-valid ::hexx "ab")
    (assert-valid ::hexx "ab2")
    (assert-valid ::hexx "ab3")
    (assert-valid ::hexx "1")
    (assert-valid ::hexx "99abd")

    (assert-invalid ::hexx "99abdz")

    (test-generation-validation-loop ::hexx)

    )

  (testing "when keyword element is registered and validated"

    (keyword! ::badge)
    (assert-valid ::badge ::foo)
    (test-invalid-values-by-type ::badge :keyword)
    (test-generation-validation-loop ::badge)

    (keyword! ::smaller-keyword {:min 1 :max 4})
    (assert-valid ::smaller-keyword :yep)
    (assert-invalid ::smaller-keyword :not-even-close)
    (test-generation-validation-loop ::smaller-keyword)

    )

  (testing "when enum element is registered and validated"

    (enum! ::status #{:pending :active :suspended :archived})

    (assert-valid ::status :pending)
    (assert-valid ::status :active)
    (assert-valid ::status :suspended)
    (assert-valid ::status :archived)

    (assert-invalid ::status :not-even-in-status)
    (assert-invalid ::status :suspended-yet-still-in-our-hearts)

    (test-invalid-values-by-type ::status :enum)
    (test-generation-validation-loop ::status)

    )

  (testing "when vector element is registered and validated"

    (enum! ::color #{:red :blue :green :puce :purple})
    (vector! ::colors ::color)

    (assert-valid ::colors [:red])
    (assert-invalid ::colors [:yellow])

    (test-invalid-values-by-type ::colors :vector)
    (test-generation-validation-loop ::colors)

    )

  (testing "when joda element is registered and validated"

    (joda! ::created)

    (assert-valid ::created (t/now))
    (assert-valid ::created (t/plus (t/now) (t/years 20)))

    (test-invalid-values-by-type ::created :joda)
    (test-generation-validation-loop ::created)


    )

  (testing "when map/document/entity element is registered and validated"

    (int! ::i1)
    (long! ::l1)
    (float! ::f1)
    (double! ::d1)
    (string! ::s1)
    (vector! ::v1 ::s1)
    (enum! ::e1 #{:a :b :c})

    (document! ::doc1 [::i1 ::f1 ::s1 ::e1] [::v1 ::l1 ::d1])

    (assert-valid ::doc1 {:i1 (int 4)
                          :f1 (float 6.28)
                          :s1 "this is a string"
                          :e1 :a
                          :v1 ["this" "is" "string" "items"]
                          :l1 333
                          :d1 3.14})


    (assert-invalid ::doc1 {:i1   (int 4)
                            :f1 (float 6.28)
                            :s1 "this is a string"
                            :e1 :z ;; changed this
                            :v1 ["this" "is" "string" "items"]
                            :l1 333
                            :d1 3.14})

    (test-invalid-values-by-type ::doc1 :map)
    (test-generation-validation-loop ::doc1)
    )

  )
