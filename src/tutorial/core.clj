(ns tutorial.core
  (:use [overtone.live])
  (:use [tutorial.simple-instrs])
  (:use [tutorial.touch-osc]))

(stop)

(def my-bus (audio-bus 2))

(defsynth asynth []
  (let [dur 2
	pchosc (+ 340 (* 34 (sin-osc 0.5)))
	osc (sin-osc pchosc)
	env (env-gen (envelope [1 0.000001] [2] :exponential))
	panned (pan2 osc)]

    (out:ar my-bus panned)))

(asynth)

(defsynth master-out [vol 1.0]
  (let [src (in:ar my-bus 2)]
    (out 0 (* vol src))))

(def master (master-out))

(osc-handle server "/3/fader3"
	    (fn [{[v] :args}]
	      (ctl master :vol v)))

(stop)

;;;;;;;;;;;;;;;;;;;;;;;;

(defsynth bizzle [out-bus 10 amp 0.5]
  (out out-bus
       (* amp
          (+ (*
	      (decay2
	       (* (impulse 10 0)
		  (+ (* (lf-saw:kr 0.3 0) -0.3) 0.3))
	       0.001)
	      0.3)
	   (apply + (pulse [80 81]))))))

					; Give it a try
(def biz (bizzle 0))
(kill biz)