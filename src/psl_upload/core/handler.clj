(ns psl-upload.core.handler
  (:require [compojure
             [core :refer :all]
             [route :as route]]
            [ring.middleware
             [content-type :refer [wrap-content-type]]
             [defaults :refer [wrap-defaults site-defaults]]
             [json :refer [wrap-json-body wrap-json-response]]]
            [psl-upload.core
             [response :refer [get-upload post-upload]]]))

(defroutes app-routes
  (let [upload-uri "/upload"]
    (->
     (GET "/upload" [] (get-upload))
     (wrap-content-type))
    (POST upload-uri [params] (post-upload params)))
  (route/not-found "Not Found"))

(defroutes app-routes
  (->
   (GET "/upload" [] (get-upload))
   (wrap-content-type))
  (POST "/upload" [file] (post-upload file))
  (GET "/test" [] (psl-upload.core.response/post-test))
  (route/not-found "Not Found"))


(def app
  (->
   #'app-routes
   (wrap-defaults site-defaults)
   (wrap-json-response)))

