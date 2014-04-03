(ns uimarannat.import
	(:require [monger.collection :as mc]
			  [monger.core :as mg]))

(use 'clojure.java.io)
(use '[clojure.string :only (split)])

(defn process-line
	[line]
	(let [[date name coordinates description] (split line #"\t")
		  [lat lon] (map read-string (split coordinates #","))]
		(if (and (number? lat) (number? lon))
			{:date date
			 :name name
			 :lat lat
			 :lon lon
			 :description description})))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (mg/connect!)
  (mg/set-db! (mg/get-db "uimarannat"))
  (with-open [rdr (reader "uimapaikkakysely.tsv")]
  	(doseq [line (line-seq rdr)]
  		(let [data (process-line line)]
  			(if data
	  			(mc/insert "rannat" (process-line line)))))))

