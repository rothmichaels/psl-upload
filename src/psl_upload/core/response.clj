(ns psl-upload.core.response
  "Namespace for high level functions for creating Ring responses."
  (:require [ring.middleware.anti-forgery :refer [*anti-forgery-token*]]
            [ring.util
             [anti-forgery :refer [anti-forgery-field]]
             [response :refer [response content-type]]]
            [psl-upload.core
             [access :refer :all]]
            [psl-upload.io
             [core :refer [save-temp-file]]])
  (:use [hiccup core page])
  (:import [java.io.File]))

;;; ## GET requests

(def ^{:doc
       "JSON Content-Type string constant."}
  json-type-string "application/json; charset=utf-8")

(defn content-type-json
  "Sets the content type of a `response` to *application/json; charset=utf-8*."
  [response-body]
  (content-type response-body json-type-string))

(defn get-upload
  "GET requests to the upload URL return JSON.

  Currently the only data in the JSON return is
  is the anti-forgery token for the session."
  []
  (->
   (response
    {:anti-forgery-token *anti-forgery-token*})
   (content-type-json)))

(defn access-denied
  "Return an access denied JSON map with the provided `error` string and error code."
  [code error]
  (->
   (content-type-json
    {:status 403
     :headers {}
     :body {:error_code code
            :error_description error}})))

(defn link
  []
  (html5
   [:p [:a {:href "psl://upload"} "UPLOAD"]]))

(defmacro when-access-granted
  [request & body]
  `(if-let [token# (:access_token ~request)]
     (if (some #{token#} *valid-access-tokens*)
       (do ~@body)
       (access-denied token-not-valid "Invalid access token"))
     (assoc-in (access-denied token-not-provided "No access token provided")
               [:body :request] ~request)))

(defn post-upload
  "The good one"
  [{:keys [file device_id device_name] :as post-data}]
  (when-access-granted
   post-data
   (->>
    (save-temp-file (:tempfile file) device_name device_id)
    (hash-map :device_id device_id :file_name)
    (response)
    (content-type-json))))


(defn text-input
  "Creates a form text input field named `name`."
  [name]
  [:input {:name name :type "text" :size "20"}])

(defn post-test
  "For GETting a form for testing POST."
  []
  (html5
   [:h1 "Test Upload"]
   [:form {:action "/upload" :method "post" :enctype "multipart/form-data"}
    (anti-forgery-field)
    [:p "Device ID:" (text-input "device_id")]
    [:p "Device Name:" (text-input "device_name")]
    [:p "File Name:" (text-input "file_name")]
    [:input {:type "hidden" :name "access_token" :value "847a5af43d99bd5a35f2d70ae8276440"}]
    [:input {:type "file" :name "file" :size "20"}]
    [:input {:type "submit" :name "submit" :value "Upload"}]]))
