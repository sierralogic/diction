(ns diction.test.guard
  (:require [clj-time.core :as t]
            [clojure.test :refer :all]
            [diction.core :as diction]
            [diction.guard :refer :all]))

(defn test-inc
  [x]
  (inc x))

(defn test-inc-second
  [x y]
  (inc y))

(def guard-inc (guard :diction/long test-inc))
(def guard-inc-second (guard :diction/long second test-inc-second))

(diction/string! ::foo)
(diction/long! ::ans)
(diction/boolean! ::flag)

(diction/document! :test/doc [::foo] [::ans ::flag])

(defn guard-fail-tuple
  [element-id f v xvf failures & args]
  (concat [element-id f v xvf failures] (vec args)))

(defn test-bump-ans
  [m]
  (update m :ans inc))

(def guard-bump-ans (guard :test/doc test-bump-ans))

(deftest guard-features

  (testing "default successes"
    (is (= 42 (guard-inc 41)))
    (is (= 99 (:ans (guard-bump-ans {:foo "bar" :ans 98}))))
    )

  (testing "successes with argument extract function"
    (is (= 42 (guard-inc-second 99 41)))
    )

  (testing "default failures"
    (is (= 400 (:status (guard-inc "number"))))
    )

  (testing "failures with bound fail function"
    (binding [*guard-fail-dynamic-f* guard-fail-tuple]
      (let [res (guard-inc "number")]
        (is (= :diction/long (first res)))
        (is (= "number" (nth res 2))))))

  )