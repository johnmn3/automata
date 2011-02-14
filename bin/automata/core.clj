(ns automata.core)

(defn make-initial-conditions [width]
  (-> (take width (repeat 0))
      vec
      (assoc (int (/ width 2)) 1)))

(defn make-rule [v1 v2 v3 v4 v5 v6 v7 v8]
  { [1 1 1] v1
    [1 1 0] v2
    [1 0 1] v3
    [1 0 0] v4
    [0 1 1] v5
    [0 1 0] v6
    [0 0 1] v7
    [0 0 0] v8 })

(defn wrap-sub [the-state the-index the-width]
  (let [left-index (- (int (/ the-width 2)))
        right-index (+ the-width left-index)
        size (count the-state)]
    (->> (vec (range left-index right-index))
         (map #(nth the-state
              (rem (+ the-index size %) size)))
         (vec))))

(defn make-stepper [the-rule]
  (fn [the-state] 
    (->> (vec (range (count the-state)))
         (map #(the-rule (wrap-sub the-state % 3)))
         (vec))))

(defn run-steps [the-steps the-stepper the-state]
  (take the-steps (iterate the-stepper the-state)))
