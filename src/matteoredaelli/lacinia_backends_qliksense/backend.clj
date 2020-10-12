(ns matteoredaelli.lacinia-backends-qliksense.backend
  (:require
   [com.walmartlabs.lacinia.resolve :as resolve]
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [matteoredaelli.qliksense.api :as qliksense-api]
   [matteoredaelli.qliksense.core :as qliksense-core]
   [clojure.string :as str])
  )

(defn get-qliksense-config
  []
  (->
   (System/getenv "LACINIA_BACKENDS_QLIKSENSE_CONFIG")
   slurp
   edn/read-string))

(defonce qliksense-config (get-qliksense-config))

(defn get-objects-by-filter
  [qliksense-config system path filter-row]
  (let [system_map (qliksense-core/get-system-map qliksense-config system)
        filter (if (= filter-row "") "1 eq 1" filter-row)
        params {"filter" filter}]
    (def resp (qliksense-api/get-request system_map path params))
    (resp :body)))

(defn resolve-query-qliksense-get
  [context arguments value path]
  (let [{:keys [system filter]} arguments]
    (get-objects-by-filter qliksense-config (keyword system) path filter)))

(defn resolve-query-qliksense-custom-property-definitions
  [context arguments value]
  (resolve-query-qliksense-get context arguments value "/qrs/custompropertydefinition/full"))

(defn resolve-query-qliksense-apps
  [context arguments value]
  (resolve-query-qliksense-get context arguments value "/qrs/app/full"))

(defn resolve-query-qliksense-streams
  [context arguments value]
  (resolve-query-qliksense-get context arguments value "/qrs/stream/full"))

(defn resolve-query-qliksense-users
  [context arguments value]
  (resolve-query-qliksense-get context arguments value "/qrs/user/full"))
