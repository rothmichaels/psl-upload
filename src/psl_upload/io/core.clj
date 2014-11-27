(ns psl-upload.io.core
  "IO API."
  (:require [clojure.java [io :as io]])
  (:import [java.io.File]))

(def ^{:doc "Data file upload directory."
       :dynamic true}
  *data-dir* "data-uploads")

(defn path-in-data-dir
  "Appends the `path` to the data directory."
  [path]
  (str *data-dir* "/" path))

(defn create-get-dir
  "Get a File for the specified directory creating it if necessary."
  [path]
  (let [file (io/as-file (path-in-data-dir path))]
    (when (not (.exists file))
      (.mkdirs file))
    file))

(defn save-temp-file
  "Saves a temporary file at path `tmp` to a file named `file-name` in a
  subdirectory named `directory` within the files folder." ; should make this resources
  [tmp file-name directory]
  (let [path (str (create-get-dir directory) "/" file-name)]
    (println ">>>" tmp "*" file-name "*" directory)
    (io/copy tmp (io/as-file path))))

