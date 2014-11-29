(ns psl-upload.core.access)

(def ^{:doc "POST access keys"
       :dynamic true}
  *valid-access-tokens*
  ["847a5af43d99bd5a35f2d70ae8276440"])

(def token-not-provided 4032)
(def token-not-valid 4033)
