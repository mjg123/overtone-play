(ns tutorial.designing.bar
  (:use [overtone.live]))

(defn exp-decay [dur]
  (envelope [1 0.00001] [dur] :exponential))

(defsynth exp-add-4 [out-bus 0 cps 440 amp 1 dur 5 o1 1 o2 1 o3 1]
 
  (let [fs [1 o1 o2 o3]

	env1 (env-gen (exp-decay dur) :action FREE)
	env2 (env-gen (exp-decay (/ dur (fs 1))))
	env3 (env-gen (exp-decay (/ dur (fs 2))))
	env4 (env-gen (exp-decay (/ dur (fs 3))))
	
	sig1 (sin-osc cps)
	sig2 (sin-osc (* cps (fs 1)))
	sig3 (sin-osc (* cps (fs 2)))
	sig4 (sin-osc (* cps (fs 3)))]

    (out out-bus
	 (+
	  (* env1 amp sig1)
	  (* env2 amp sig2)
	  (* env3 amp sig3)
	  (* env4 amp sig4)))))


(defsynth boe [cps 110]
  (out 0
       (klank [[1 6.267 17.55 34.39]
	       [0.5 0.25 0.125 0.06125]
	       [0.2 0.2 0.2 0.2]]
	      (impulse:ar 1)
	      cps
	      )))

(boe)
(stop)


(defn bar-one-end
  "Simulates a solid bar which is fixed at one end."
  [m]
  (exp-add-4 :position :head (m :out-bus) (m :cps) (m :amp) (m :dur) 6.267 17.55 34.39))

(defn bar-free
  "Simulates a solid bar which is free in space."
  [m]
  (exp-add-4 :position :head (m :out-bus) (m :cps) (m :amp) (m :dur) 2.756 5.141 8.933))

(do
  (bar-free {:out-bus ll :cps 272.73 :amp 1 :dur 10})
  (bar-free {:out-bus rr :cps 279.06 :amp 1 :dur 10}))

(stop)


(defsynth send-out [in-bus 100 pan 0]
  (out 0 (pan2 (in in-bus) pan)))

(def ll (audio-bus))

(send-out :position :tail ll -1)
(send-out :position :tail rr 1)

(node-tree)


