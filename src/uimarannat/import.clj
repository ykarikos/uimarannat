(ns uimarannat.import
	(:require [uimarannat.db :as db]))

(use 'clojure.java.io)
(use '[clojure.string :only (split)])


(defn parse-line
	[line]
	(let [[date name coordinates description] (split line #"\t")
		  [lat lon] (map read-string (split coordinates #","))]
		(if (and (number? lat) (number? lon))
			{:date date
			 :name name
			 :location {:type "Point" :coordinates [lon lat]}
			 :description description})))

(defn process-data
	"Read a tsv file from rdr"
	[rdr]
  	(doseq [line (line-seq rdr)]
  		(if-let [data (parse-line line)]
        (let [coordinates (-> data :location :coordinates)]
    			(db/insert-location data)))))

(defn -main
  "Save tsv data in a GeoJSON database"
  [dbname filename & args]
  (db/connect! dbname)
  (with-open [rdr (reader filename)]
  	(process-data rdr))
  (println (str (db/count-locations) " locations in db")))

