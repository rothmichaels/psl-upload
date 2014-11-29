(ns psl-upload.core.middleware
  "The Middleware"
  (:require [ring.util.response :refer [response content-type]]
            [psl-upload.core
             [response :refer [access-denied]]
             [access :refer :all]]))


(defn wrap-access-validation
  "Wraps a handler with request middleware that confirms a valid access
  token is passed on non-GET requests. The access token field name is
  `access_token."
  [handler]
  (fn [request]
    (if (= :get (:request-method request))
      (handler request)
      (if-let [token (:access_token request)]
        (if (some #{token} *valid-access-tokens*)
          (handler request)
          (access-denied token-not-valid "Invalid access token."))
        (assoc-in (access-denied token-not-provided "No access token provided.")
                  [:body :request] request)))))


(defn wrap-access-validation
  "Wraps a handler with request middleware that confirms a valid access
  token is passed on non-GET requests. The access token field name is
  `access_token."
  [handler]
  (fn [request]
    (if-let [token (:access_token request)]
        (if (some #{token} *valid-access-tokens*)
          (handler request)
          (access-denied token-not-valid "Invalid access token."))
        (access-denied token-not-provided "No access token provided."))))



