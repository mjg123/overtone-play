(ns tutorial.designing.chorus
  (:use [overtone.live]))

(def sample-buf (load-sample "36.wav"))

(defsynth reverb-on-left []
  (let [dry (play-buf 1 sample-buf)
	wet (free-verb dry 1)]
    (out 0 [wet dry])))

(reverb-on-left)
(stop)

