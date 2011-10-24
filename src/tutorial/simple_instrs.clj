(ns tutorial.simple-instrs
  (:use [overtone.live]))

(definst bing [m 60]
  (let [dur 4
	sig (sin-osc (midicps m))
	kdclck (env-gen (envelope [0 1 1 0] [0.01 (- dur 0.02) 0.01]))
	env (env-gen:kr (envelope [1 0.00001] [dur] :exponential))]
    (* sig env kdclck)))


(definst siney [cps 440 vol 0.5]
  (* vol
     (sin-osc cps)))

(definst bop [dur 0.1]
  (let [weight (i-rand 155 220)
	pch (line:kr weight 22 dur FREE)
	dclck (env-gen:kr (envelope [0 1 1 0] [0.01 (- dur 0.02) 0.01]))
	osc (sin-osc pch)]
    (* dclck osc)))

(definst schnare [dur 0.5]
  (let [sig (white-noise)
	env (env-gen:kr
	     (envelope
	      [1 0.000001]
	      [dur]
	      :exponential))]
    (* sig env)))

