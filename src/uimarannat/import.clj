(ns uimarannat.import
	(:require [uimarannat.db :as db]))

(use 'clojure.java.io)
(use '[clojure.string :only (split)])


(defn parse-line
  "Parse a line to a map structure. Returns nil if no coordinates were found."
  [line]
  (let [[date name coordinates comment] (split line #"\t")
        [lat lon] (map read-string (split coordinates #","))]
		(when (and (number? lat) (number? lon))
			{:date date
			 :name name
			 :location {:type "Point" :coordinates [lon lat]}
			 :comments [comment]})))

; Locations within nearDistance metres are treated the same
(def nearDistance 3500)

(defn process-data
	"Read a tsv file from rdr"
	[rdr]
  	(doseq [line (line-seq rdr)]
  		(when-let [data (parse-line line)]
        (let [coordinates (-> data :location :coordinates)
              old-locations (db/find-locations coordinates nearDistance)]
          (if-not (empty? old-locations)
            (db/merge-locations ((first old-locations) :obj) data)
      			(db/insert-location data))))))

(defn -main
  "Save tsv data in a GeoJSON database"
  [dbname filename & args]
  (db/connect! dbname)
  (with-open [rdr (reader filename)]
  	(process-data rdr))
  (println (str (db/count-locations) " locations in db")))

