(ns Automata.cljrule30)

(defn mkrule [v1 v2 v3 v4 v5 v6 v7 v8]
  {[1 1 1] v1
   [1 1 0] v2
   [1 0 1] v3
   [1 0 0] v4
   [0 1 1] v5
   [0 1 0] v6
   [0 0 1] v7
   [0 0 0] v8})

(def rule30 (mkrule 0 0 0 1 1 1 1 0))

(defn start-state [width]
  (-> (take width (repeat 0))
      vec
      (assoc (int (/ width 2)) 1)))

(def state (start-state 20))

(defn run-steps [the-steps the-stepper the-rule the-state]
  (take the-steps
        (iterate (partial the-stepper the-rule) the-state)))

(defn cycle-partition [n step coll]
  (let [size (count coll)]
    (->> (cycle coll)
         (drop (- size step))
         (partition n step)
         (take size))))

(assert (= (cycle-partition 3 1 [0 1 2 3 4 5 6 7 8 9])
           [[9 0 1] [0 1 2] [1 2 3] [2 3 4] [3 4 5] [4 5 6] [5 6 7] [6 7 8] [7 8 9] [8 9 0]]  ))

(defn stepper-3 [the-rule the-state]
  (->> the-state
       (cycle-partition 3 1)
       (map the-rule)
       vec))

(defn run-automata [n-steps width]
  (run-steps n-steps  stepper-3 rule30 (start-state width)))

(defn pp-automata [output]
  (let [to-pretty-char {0 " " 1 "#"}]
    (doseq [line output]
      (println (apply str (map to-pretty-char line))))))

(defn pfile-automata [output filename]
  (->> (with-out-str (pp-automata output))
       (spit filename)))

(def state (assoc (vec (take 1000000 (iterate identity 0))) 50000 1))
(comment (pfile-automata (run-automata 150 300) "150x300.txt"))
