(ns automata.print-image
  (:require [automata.core :as core]
            [automata.rules :as rules])
  (:import (java.awt.image BufferedImage)
           (javax.imageio ImageIO)
           (java.io File)
           (java.awt Color))
  (:require [automata.core :as core]
            [automata.rules :as rules]))

(def *black-rgb* (.getRGB Color/BLACK))
(def *white-rgb* (.getRGB Color/WHITE)) 


(defn rasterize
  "From [[0 1 0] [1 1 1] ...] to [[x y pixel] ...]"
  [steps width lines]
  (map (fn [[x y] pixel] [x y pixel])
       (for [y (range steps)
             x (range width)]
         [x y])
       (apply concat lines)))

(assert (= (rasterize 2 3 [[0 1 0] [1 1 1]])
           [[0 0 0] [1 0 1] [2 0 0]
            [0 1 1] [1 1 1] [2 1 1]]))


(defn create-image [steps width lines]
  (let [raster (rasterize steps width lines)
        img (BufferedImage. width steps BufferedImage/TYPE_INT_ARGB)]
    (doseq [[x y pixel] raster]
      (let [brush (if (= pixel 1)
                    *black-rgb*
                    *white-rgb*)]
        (.setRGB img x y brush)))
    img))

(defn pimg-automata [steps stepper the-state]
  (let [lines (core/run-steps steps stepper the-state)
        width (count the-state)
        img (create-image steps width lines)]
    (ImageIO/write img "png" 
      (File. (str steps "x" width "_" (gensym) ".png")))))

(comment
  (def initial-conditions (core/make-initial-conditions 300))
  (pimg-automata 300 rules/rule-30-stepper initial-conditions)
  )