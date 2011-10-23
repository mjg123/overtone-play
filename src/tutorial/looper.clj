(ns tutorial.looper
  (:use [overtone.live])
  (:use [tutorial.simple-instrs]))

(stop)

(bop 0.1)
(schnare 0.5)

(def metro (metronome 40))

(defn t8 [tick]
  (/ (dec tick) 8))

(defn player [beat]
  (at (metro beat) (bop))
  (at (metro (+ (t8 3) beat)) (schnare))
  (at (metro (+ (t8 4) beat)) (bop))
  (at (metro (+ (t8 7) beat)) (schnare 1))
  (apply-at (metro (inc beat)) #'player (inc beat) []))

(player (metro))

       