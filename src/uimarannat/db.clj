(ns uimarannat.db
  (:require [monger.collection :as mc]
            [monger.core :as mg]
            [monger.command :as mcmd]))

(require '[monger.conversion :refer [from-db-object]])

(def collection "rannat")

(defn connect!
  "Set up db connection"
  [dbname]
  (mg/connect!)
  (mg/set-db! (mg/get-db dbname)))


(defn find-locations
  "Find locations in the given coordinate pair [lon lat] with maxDistance"
  [coordinates maxDistance]
  (let [raw-result (mg/command
                    (sorted-map :geoNear collection
                                :near {:type "Point" :coordinates coordinates}
                                :spherical true
                                :maxDistance maxDistance))
        result (from-db-object raw-result true)]
    (result :results)))

(defn count-locations
  "Count locations stored in db"
  []
  (mc/count collection))

(defn insert-location
  "Insert a location to db"
  [data]
  (mc/insert collection data))

(defn merge-locations
  "Merge new location comments with existing one"
  [old-data new-data]
  (let [old-id (old-data :_id)
        old-comments (old-data :comments)
        new-comments (new-data :comments)]
    (mc/update-by-id collection old-id
                     {"$set" {:comments (into old-comments new-comments)}})))
