(ns uimarannat.import-test
  (:use clojure.test
        uimarannat.import))

(use '[clojure.string :only (join)])


(deftest test-parse-line
  (testing "Testing valid input for parse-line"
    (let [date "2014-04-04T12:00:00"
          name "Hietsu"
          lat 60.173871
          lon 24.905013
          comment "Paras biitsi ikinä missään"
          line (join "\t" (list date name (join "," (list lat lon)) comment))
          data (parse-line line)]
      (is (= date (data :date)))
      (is (= name (data :name)))
      (is (= [lon lat] (-> data :location :coordinates)))
      (is (= 1 (count (data :comments))))
      (is (= comment (nth (data :comments) 0)))))

  (testing "Testing invalid input for parse-line"
    (let [date "2014-04-04T12:00:00"
          name "Hietsu"
          lat "foo"
          lon "bar"
          comment "Paras biitsi ikinä missään"
          line (join "\t" (list date name (join "," (list lat lon)) comment))
          data (parse-line line)]
      (is (nil? data)))))

