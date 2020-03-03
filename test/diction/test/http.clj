(ns diction.test.http
  (:require [clj-time.core :as t]
            [clojure.test :refer :all]
            [diction.core :as diction]
            [diction.http :refer :all]))

(def route-foobar "/foo/:bar")

(def payld-validation-routes
  {route-foobar {:post :doc}})

(def param-validation-routes
  {route-foobar {:get :params
                 :post :params}})

(payload-validation-routes! payld-validation-routes)
(parameter-validation-routes! param-validation-routes)

(diction/string! :foo)
(diction/long! :ans)
(diction/string! :bar)
(diction/boolean! :flag)

(diction/document! :doc [:foo] [:ans :flag])

(diction/document! :params [:bar] [:baz :meh])

(def good-payload {:ans 42 :foo "bar"})
(def bad-payload {:ans 42 :foo 33})
(def good-params {:bar "baz"})
(def bad-params {:bar -42})

(def post-foo-bar-request-good
  {:compojure/route {1 route-foobar}
   :request-method :post
   :body good-payload
   :params good-params})

(def post-foo-bar-request-bad
  {:compojure/route {1 route-foobar}
   :request-method :post
   :body bad-payload
   :params bad-params})

(deftest testing-payload-validation-routes

  (testing "simple route payload test"
    (is (nil? (:status ((validate-payload identity) post-foo-bar-request-good))))
    (is (= 400 (:status ((validate-payload identity) post-foo-bar-request-bad))))
    )

  )

(deftest testing-paramaters-validation-routes

  (testing "simple route parameters test"
    (is (nil? (:status ((validate-parameters identity) post-foo-bar-request-good))))
    (is (= 400 (:status ((validate-parameters identity) post-foo-bar-request-bad))))
    )

  )
