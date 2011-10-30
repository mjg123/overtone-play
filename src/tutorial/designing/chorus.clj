(ns tutorial.designing.chorus
  (:use [overtone.live]))

(def baby-36 (load-sample "36.wav"))

(defsynth chorusbaby []
  (let [dry (play-buf 1 baby-36)

	d1osc (+ 0.05 (* 0.04 (sin-osc 0.25)))
	del1 (delay-l dry 0.5 d1osc)

	d2osc (+ 0.05 (* 0.04 (sin-osc 0.2)))
	del2 (delay-l dry 0.5 d2osc)
	
	wet (* 0.333 (+ dry del1 del2))]
  
    (out 0 (+ (pan2 dry 1)
	      (pan2 wet -1)))))

;; how to do the feedback?

(chorusbaby)
(stop)

