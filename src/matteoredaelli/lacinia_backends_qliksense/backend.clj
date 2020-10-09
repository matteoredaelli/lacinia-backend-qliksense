(ns matteoredaelli.lacinia-backends-qliksense.backend
  (:require
   [com.walmartlabs.lacinia.resolve :as resolve]
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [matteoredaelli.qliksense.api :as qliksense-api]
   [matteoredaelli.qliksense.core :as qliksense-core]
   [clojure.string :as str])
  )

(defn qliksense-config
  []
  (->
   (System/getenv "LACINIA_BACKENDS_QLIKSENSE_CONFIG")
   slurp
   edn/read-string))

(defonce config (qliksense-config))

(defn get-objects-by-filter
  [config system path filter-row]
  (let [system_map (qliksense-core/get-system-map config system)
        filter (if (= filter-row "") "1 eq 1" filter-row)
        params {"filter" filter}]
    (def resp (qliksense-api/get-request system_map path params))
    (resp :body)))

(defn resolve-query-qliksense-streams
  [context arguments value]
  (let [{:keys [system filter]} arguments
        path "/qrs/stream/full"]

    (get-objects-by-filter config (keyword system) path filter)))
