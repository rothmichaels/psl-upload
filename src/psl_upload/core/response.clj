(ns psl-upload.core.response
  "Namespace for high level functions for creating Ring responses."
  (:require [ring.middleware.anti-forgery :refer [*anti-forgery-token*]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [ring.util.response :refer [response content-type]])
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
  "POST upload requests."
  [params]
  (do
    (println params)
    (comment (->
               (response
                (reduce #(assoc %1 %2 (params %2)) {} [:file :file_name :device_id :device_name])) 
               
               (content-type-json)))
    (-> (response params) (content-type-json))))

(defn post-upload
  [{:keys [file file_name device_id device_name] :as post-data}]
  (->
   (response (assoc post-data :file (.getPath (:tempfile file))))
   (content-type-json)))

(defn post-upload
  [file]
  (content-type-json (response {:thefile (.getPath (:tempfile file))})))

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