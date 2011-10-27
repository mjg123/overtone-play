(ns p02
  (:use [overtone.live]))

(definst chowning [amp 1 c 440 m 440 idx 3]
  (let [dev (* idx m)]
    (sin-osc
     (+ c (* dev (sin-osc m)))
     amp)))

(chowning 1 700 440 3)
(stop)

