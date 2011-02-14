(ns automata.pfile
  (:require [automata.core :as core]
            [automata.rules :as rules]))

(defn pp-automata [output]
  (let [to-pretty-char {0 " " 1 "#"}]
    (doseq [line output]
      (println (apply str (map to-pretty-char line))))))

(defn pfile-automata [output filename]
  (->> (with-out-str (pp-automata output))
       (spit filename)))
;(comment
(def initial-conditions (core/make-initial-conditions 300))
(pfile-automata (core/run-steps 300 rules/rule-90-stepper initial-conditions) 
  "rule90-300x300.txt")
(pfile-automata (core/run-steps 300 rules/rule-30-stepper initial-conditions) 
  "rule30-300x300.txt")
;)