(ns Automata.core)

(def state (assoc (vec (take 20 (iterate identity 0))) 10 1))

(defn mkrule [v1 v2 v3 v4 v5 v6 v7 v8]
  { [1 1 1] v1
    [1 1 0] v2
    [1 0 1] v3
    [1 0 0] v4
    [0 1 1] v5
    [0 1 0] v6
    [0 0 1] v7
    [0 0 0] v8 })

(def rule30 (mkrule 0 0 0 1 1 1 1 0))

(defn wrap-sub [the-state the-index the-width]
  (let [left-index (- (int (/ the-width 2)))
        right-index (+ the-width left-index)
        size (count the-state)]
    (->> (vec (range left-index right-index))
         (map #(nth the-state
              (rem (+ the-index size %) size)))
         (vec))))

(defn stepper-3 [the-rule the-state]
  (->> (vec (range (count the-state)))
    (map #(the-rule (wrap-sub the-state % 3)))
    (vec)))

(defn run-steps [the-steps the-stepper the-rule the-state]
  (take the-steps (iterate #(the-stepper the-rule %) the-state)))


(defn chunk-stepper-3 [the-rule the-state]
  (->> (drop 1 (drop-last (vec (range (count the-state)))))
    (map #(the-rule (wrap-sub the-state % 3)))
    (vec)))

(defn chunkify [the-state chunk-size]
  (let [window (+ 2 chunk-size)
        first-index (- (int (/ window 2)) 1)
        the-range (vec (range first-index
                    (* chunk-size (int (/ (count the-state) chunk-size)))
                    chunk-size))
        size (count the-state)
        rem-size (rem size chunk-size)
        tail-sub (if (not= 0 rem-size)
                   (let [range-tail (vec (range (- size rem-size) size))
                         tail-size (count range-tail)
                         tail-index (nth range-tail (int (/ tail-size 2)))]
                     (wrap-sub the-state tail-index (+ tail-size 2))))]
    (if tail-sub
      (conj (vec (pmap #(wrap-sub the-state % window) the-range)) 
        tail-sub)
      (vec (pmap #(wrap-sub the-state % window) the-range)))))

;; Chunks and un-chunks using the stepper
(defn chunkerator [the-stepper the-rule the-state chunk-size]
  (vec (flatten (pmap #(the-stepper the-rule %) (chunkify the-state chunk-size)))))

;; iterates the chunkerator
(defn run-chunks [the-steps the-stepper the-rule the-state chunk-size]
  (take the-steps (iterate #(chunkerator the-stepper the-rule % chunk-size) the-state)))

(def state (assoc (vec (take 1000000 (iterate identity 0))) 50000 1))

(time (take 20 (drop 49990 (last (run-steps 10 stepper-3 rule30 state)))))
(time (take 20 (drop 49990 (last (run-chunks 10 chunk-stepper-3 rule30 state 1000)))))
