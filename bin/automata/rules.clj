(ns automata.rules
  (:require [automata.core :as core]))

(def rule30 
  (with-meta
    (core/make-rule 0 0 0 1 1 1 1 0)
    {:name 'rule30}))

(def rule-30-stepper
  (core/make-stepper rule30))

(def rule-30-stepper
  (with-meta rule-30-stepper {:rule-name 'rule30}))

(def rule90 (core/make-rule 0 1 0 1 1 0 1 0))

(def rule-90-stepper
  (core/make-stepper rule90))

(def rule250 (core/make-rule 1 1 1 1 1 0 1 0))

(def rule-250-stepper
  (core/make-stepper rule250))


(comment
  (def initial-conditions (core/make-initial-conditions 1000000))
  (time (take 20 
          (drop 49990 
            (last 
              (core/run-steps 10 
                rule-30-stepper initial-conditions)))))
)
