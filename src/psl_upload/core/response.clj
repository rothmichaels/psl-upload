(ns psl-upload.core.response
  "Namespace for high level functions for creating Ring responses."
  (:require [ring.middleware.anti-forgery :refer [*anti-forgery-token*]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [ring.util.response :refer [response content-type]]
            [psl-upload.io.core :refer [save-temp-file]])
  (:use [hiccup core page])
  (:import [java.io.File]))

;;; ## GET requests

(def ^{:doc
       "JSON Content-Type string constant."}
  json-type-string "application/json; charset=utf-8")

(defn content-type-json
  "Sets the content type of a `response` to *application/json; charset=utf-8*."
  [response]
  (content-type response json-type-string))

(defn get-upload
  "GET requests to the upload URL return JSON.

  Currently the only data in the JSON return is
  is the anti-forgery token for the session."
  []
  (->
   (response
    {:anti-forgery-token *anti-forgery-token*})
   (content-type-json)))
 

(defn post-upload
  "The good one"
  [{:keys [file device_id device_name] :as post-data}]
  (->>
   (save-temp-file (:tempfile file) device_name device_id)
   (hash-map :device_id device_id :file_name)
   (response)
   (content-type-json)))


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
    [:input {:type "file" :name "file" :size "20"}]
    [:input {:type "submit" :name "submit" :value "Upload"}]]))
