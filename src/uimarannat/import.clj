(ns uimarannat.import)

(use 'clojure.java.io)
(use '[clojure.string :only (split)])

(defn process-line
	[line]
	(let [[date name coordinates description] (split line #"\t")
		  [lat lon] (map read-string (split coordinates #","))]
		(if (and (number? lat) (number? lon))
			(str lat ":" lon)
			"")))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (with-open [rdr (reader "uimapaikkakysely.tsv")]
  	(doseq [line (line-seq rdr)]
  		(println (process-line line)))))

