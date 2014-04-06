(ns uimarannat.import
	(:require [monger.collection :as mc]
			  [monger.core :as mg]))

(use 'clojure.java.io)
(use '[clojure.string :only (split)])

; query like this:
; db.runCommand( { geoNear: "rannat", 
;	near: {type: "Point", 
;		coordinates: [21.5228, 61.5643] }, 
;		spherical: true, 
;		maxDistance: 10 }).results.length

(defn parse-line
	[line]
	(let [[date name coordinates description] (split line #"\t")
		  [lat lon] (map read-string (split coordinates #","))]
		(if (and (number? lat) (number? lon))
			{:date date
			 :name name
			 :location {:type "Point" :coordinates [lon, lat]}
			 :description description})))

(defn -main
  "Save tsv data in a GeoJSON database"
  [dbname filename & args]
  (mg/connect!)
  (mg/set-db! (mg/get-db dbname))
  (with-open [rdr (reader filename)]
  	(doseq [line (line-seq rdr)]
  		(let [data (parse-line line)]
  			(if data
	  			(mc/insert "rannat" data))))))

