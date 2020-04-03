(ns diction.http
  (:require [diction.core :as diction]))

;; Payload/Parameters Route Dictions and Invalid Response Function

(def ^:private default-payload-validaton-routes
  {"/diction-echo" {:post :diction/foobar}})

(def payload-validation-routes
  "Atom with payload validation routes.\n\n```\n{\"path\" {:method :data-element-validation-id}}\n```"
  (atom default-payload-validaton-routes))

(defn payload-validation-routes!
  "Set the `payload-validation-routes` map with `pvrs`."
  [pvrs]
  (reset! payload-validation-routes pvrs))

(def ^:private default-parameter-validaton-routes
  {"/diction-ping" {:get :diction/foobar}})

(def parameter-validation-routes
  "Atom with parameter validation routes.\n\n```\n{\"path\" {:method :parameter-data-element-validation-id}}\n```"

  (atom default-parameter-validaton-routes))

(defn parameter-validation-routes!
  "Sets the `parameter-validation-routes` map with `pvrs`."
  [pvrs]
  (reset! parameter-validation-routes pvrs))

(defn simple-bad-request-response
  "Simple bad request due to invalid payload or parameters given validation
  error messages `body`."
  [body]
  {:status 400
   :body body})

(def bad-request-response-f
  "Atom for bad request response function."
  (atom simple-bad-request-response))

(defn bad-request-response-f!
  "Sets the bad request response function to `f`.
  (bad-request-response-f! (fn [x] {:status 400 :body x}))"
  [f]
  (reset! bad-request-response-f f))

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

;; Validation routes

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

(def validate-payload
  "Partial function to validate payload."
  (partial validate ->payload-element-id :body "Payload"))

(def validate-parameters
  "Partial function to validation parameters."
  (partial validate ->parameter-element-id :params "Parameter"))
