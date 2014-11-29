(ns psl-upload.core.handler
  (:require [compojure
             [core :refer :all]
             [route :as route]]
            [ring.middleware
             [content-type :refer [wrap-content-type]]
             [defaults :refer [wrap-defaults site-defaults]]
             [params :refer [wrap-params]]
             [multipart-params :refer [wrap-multipart-params]]
             [json :refer [wrap-json-body wrap-json-response]]]
            [psl-upload.core
             [response :refer [get-upload post-upload]]
             [middleware :refer [wrap-access-validation]]]))

(defroutes app-routes
  (->
   (GET "/upload" [] (get-upload))
   (wrap-content-type))                 ; do we need this?
  (POST "/upload" {params :params} (post-upload params))
  (GET "/link" [] (psl-upload.core.response/link))
  (GET "/test" [] (psl-upload.core.response/post-test))
  (route/not-found "Not Found"))

(def app
  (->
   #'app-routes
   (wrap-json-response)
   (wrap-params)
   (wrap-multipart-params)
   (wrap-defaults site-defaults)))

