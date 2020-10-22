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

(defn qliksense-stream-ldap-groups
  [backend]
  (fn [context arguments value]
    (let [customProperties (:customProperties value)]
      ;,(filter #(= (get-in % [:definition :name]) "GroupAccess"))
      (map :value
           (filter #(= (get-in % [:definition :name]) "GroupAccess")
                   customProperties)))))

(defn qliksense-user-ldap-qliksense-groups
  [backend]
  (fn [context arguments value]
    (let [attributes (:attributes value)]
      (filter #(clojure.string/starts-with? (clojure.string/upper-case %)
                                            "QLIKSENSE_")
              (map :attributeValue attributes)))))

(defn query-qliksense-apps
  [backend]
  (fn [context arguments value]
    (qliksense-get-objects backend context arguments value "/qrs/app/full")))

(defn query-qliksense-custom-property-definitions
  [backend]
  (fn [context arguments value]
    (qliksense-get-objects backend context arguments value "/qrs/custompropertydefinition/full")))

(defn query-qliksense-reload-tasks
  [backend]
  (fn [context arguments value]
    (qliksense-get-objects backend context arguments value "/qrs/reloadtask/full")))

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
  (let [backend (:qliksense-backend component)]
    {
     :QliksenseStream/ldap-groups (qliksense-stream-ldap-groups backend)
     :QliksenseUser/ldap-qliksense-groups (qliksense-user-ldap-qliksense-groups backend)
     :query/qliksense-apps (query-qliksense-apps backend)
     :query/qliksense-custom-property-definitions (query-qliksense-custom-property-definitions backend)
     :query/qliksense-reload-tasks (query-qliksense-reload-tasks backend)
     :query/qliksense-streams (query-qliksense-streams backend)
     :query/qliksense-users (query-qliksense-users backend)
     }
    ))

(defn get-schema
  [component]
  (-> (io/resource "qliksense-schema.edn")
      slurp
      edn/read-string))

(defn load-schema
  [component]

  (-> (get-schema component)
      (util/attach-resolvers (resolver-map component))
      schema/compile))


(defrecord SchemaProvider [schema]

  component/Lifecycle

  (start [this]
    (assoc this :qliksense-schema (get-schema this)))

  (stop [this]
    (assoc this :qliksense-schema nil)))

(defn new-schema-provider
  []
  {:schema-provider (-> {}
                        map->SchemaProvider
                        (component/using [:qliksense-backend]))})
