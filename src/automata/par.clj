(ns automata.par
  (:require [automata.core :as core]
            [automata.rules :as rules]))

(defn make-chunk-stepper [the-rule]
  (fn [the-state]
    (->> (drop 1 (drop-last (vec (range (count the-state)))))
         (pmap #(the-rule (core/wrap-sub the-state % 3)))
         (vec))))

(def rule30-chunk-stepper
  (make-chunk-stepper rules/rule30))

(defn chunk-cycle-partition [step coll]
  (let [size (count coll)
        take-size (int (/ size step))
        append-remaining
          #(if (not= 0 (rem size step))
             (->> (nth coll 0)
                  (conj (vec (drop (- (* step take-size)  1) coll)))
                  (conj %))
             %)]
    (->> (cycle coll)
         (drop (- size 1))
         (partition (+ step 2) step)
         (take take-size)
         (map vec)
         (vec)
         (append-remaining))))

;; Chunks and un-chunks using the stepper
(defn chunkerator [the-stepper the-state chunk-size]
  (vec (flatten (pmap #(the-stepper %) 
                  (chunk-cycle-partition chunk-size the-state)))))

;; iterates the chunkerator
(defn run-chunks [the-steps the-stepper the-state chunk-size]
  (take the-steps 
    (iterate #(chunkerator the-stepper % chunk-size) the-state)))

(comment
(def initial-conditions (core/make-initial-conditions 1000000))
(time (take 20 
        (drop 49990 
          (last 
            (run-chunks 10 
              rule30-chunk-stepper initial-conditions 1000)))))
)
