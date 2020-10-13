(ns matteoredaelli.lacinia-backend-qliksense.system
  (:require
   [com.stuartsierra.component :as component]
   [matteoredaelli.lacinia-backend-qliksense.schema :as schema]
   [matteoredaelli.lacinia-backend-qliksense.server :as server]
   [matteoredaelli.lacinia-backend-qliksense.backend :as backend]
))

(defn new-system
  []
  (merge (component/system-map)
         (server/new-server)
         (schema/new-schema-provider)
         (backend/new-backend)))
