(ns diction.http
  (:require [diction.core :as diction]))

; (compojure.core/wrap-routes validate-payload)
; (compojure.core/wrap-routes validate-parameters)

;; Payload/Parameters Route Dictions and Invalid Response Function

(def default-payload-validaton-routes
  {"/diction-echo" {:post :diction/foobar}})

(def payload-validation-routes (atom default-payload-validaton-routes))
(defn payload-validation-routes!
  "Set the `payload-validation-routes` map with `pvrs`."
  [pvrs]
  (reset! payload-validation-routes pvrs))

(def default-parameter-validaton-routes
  {"/diction-ping" {:get :diction/foobar}})

(def parameter-validation-routes (atom default-parameter-validaton-routes))
(defn parameter-validation-routes!
  "Set the `parameter-validation-routes` map with `pvrs`."
  [pvrs]
  (reset! parameter-validation-routes pvrs))

(defn simple-bad-request-response
  "Simple bad request due to invalid payload or parameters given validation
  error messages `body`."
  [body]
  {:status 400
   :body body})

(def bad-request-response-f (atom simple-bad-request-response))
(defn bad-request-response-f! [f] (reset! bad-request-response-f f))

;; Utiltities

(defn ->payload-element-id
  "Converts `route` to payload element ID given the `:method` and `:uri`
  in the `route`.  Returns the payload validation element ID (if found), or `nil`."
  [{:keys [method uri] :as route}]
  (get-in @payload-validation-routes [uri method]))

(defn ->parameter-element-id
  "Converts `route` to parameter element ID given the `:method` and `:uri`
  in the `route`.  Returns the parameter validation element ID (if found), or `nil`."
  [{:keys [method uri] :as route}]
  (get-in @parameter-validation-routes [uri method]))

(defn ->route
  "Convert HTTP `request` to route map with `:method` and `:uri` keys."
  [request]
  (let [{:keys [request-method context]} request
        compojure-route (get-in request [:compojure/route 1])
        uri (str context compojure-route)]
    {:method request-method
     :uri uri}))

;; Validatation routes

(defn validate
  "Validation of request payload wrapped function."
  [route-lookup-f request-key v-type handler]
  (fn [request]
    (if-let [element-id (-> request
                            ->route
                            route-lookup-f)]
      (let [v (get request request-key)]
        (if-let [invalid-messages (diction/explain element-id v)]
          (@bad-request-response-f {:error (str v-type " validation failed for element '"
                                                element-id "'. [failure count="
                                                (count invalid-messages) "]")
                                    request-key v
                                    :element element-id
                                    :failures invalid-messages})
          (handler request)))
      (handler request))))

(def validate-payload (partial validate ->payload-element-id :body "Payloadx"))
(def validate-parameters (partial validate ->parameter-element-id :params "Parameterx"))
