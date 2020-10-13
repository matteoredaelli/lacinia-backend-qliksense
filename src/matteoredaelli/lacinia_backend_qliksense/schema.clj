(ns matteoredaelli.lacinia-backend-qliksense.schema
  (:require
    [clojure.edn :as edn]
    [clojure.java.io :as io]
    [com.walmartlabs.lacinia.schema :as schema]
    [com.walmartlabs.lacinia.util :as util]
    [com.stuartsierra.component :as component]
    [matteoredaelli.lacinia-backend-qliksense.backend :as backend]))


(defn qliksense-get-objects
  [backend context arguments value path]
  (let [{:keys [system filter]} arguments]
    (backend/get-objects-by-filter backend (keyword system) path filter)))

(defn query-qliksense-apps
  [backend]
  (fn [context arguments value]
    (print backend)
    (print arguments)
    (qliksense-get-objects backend context arguments value "/qrs/app/full")))

(defn query-qliksense-custom-property-definitions
  [backend]
  (fn [context arguments value]
    (qliksense-get-objects backend context arguments value "/qrs/custompropertydefinition/full")))

(defn query-qliksense-streams
  [backend]
  (fn [context arguments value]
    (qliksense-get-objects backend context arguments value "/qrs/stream/full")))

(defn query-qliksense-users
  [backend]
  (fn [context arguments value]
    (qliksense-get-objects backend context arguments value "/qrs/user/full")))


(defn resolver-map
  [component]
  (let [backend (:backend component)]
    {
     :query/qliksense-apps (query-qliksense-apps backend)
     :query/qliksense-custom-property-definitions (query-qliksense-custom-property-definitions backend)
     :query/qliksense-streams (query-qliksense-streams backend)
     :query/qliksense-users (query-qliksense-users backend)
     }
    ))

(defn load-schema
  [component]
  (-> (io/resource "schema.edn")
      slurp
      edn/read-string
      (util/attach-resolvers (resolver-map component))
      schema/compile))


(defrecord SchemaProvider [schema]

  component/Lifecycle

  (start [this]
    (assoc this :schema (load-schema this)))

  (stop [this]
    (assoc this :schema nil)))

(defn new-schema-provider
  []
  {:schema-provider (-> {}
                        map->SchemaProvider
                        (component/using [:backend]))})
