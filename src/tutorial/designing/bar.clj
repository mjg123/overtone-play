(ns tutorial.designing.bar
  (:use [overtone.live]))

(defn exp-decay [dur]
  (envelope [1 0.00001] [dur] :exponential))

(defsynth exp-add-4 [out-bus 0 cps 440 amp 1 dur 5 pan 0 o1 1 o2 1 o3 1]
 
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
	 (pan2 
	  (+
	   (* env1 amp sig1)
	   (* env2 amp sig2)
	   (* env3 amp sig3)
	   (* env4 amp sig4))
	  pan))))

(comment
  "also can be done with resonators acting on impulse"
  (defsynth boe [cps 110]
    (out 0
	 (klank [[1 6.267 17.55 34.39]
		 [0.5 0.25 0.125 0.06125]
		 [0.2 0.2 0.2 0.2]]
		(impulse:ar 1)
		cps
		))))

(defn bar-one-end
  "Simulates a solid bar which is fixed at one end."
  [m]
  (exp-add-4 :position :head
	     (m :out-bus) (m :cps) (m :amp) (m :dur) (m :pan) 6.267 17.55 34.39))

(defn bar-free
  "Simulates a solid bar which is free in space."
  [m]
  (exp-add-4 :position :head
	     (m :out-bus) (m :cps) (m :amp) (m :dur) (m :pan) 2.756 5.141 8.933))

(do
  (bar-one-end {:out-bus my-bus :cps 272.73 :amp 1 :dur 10 :pan -1})
  (bar-one-end {:out-bus my-bus :cps 279.06 :amp 1 :dur 10 :pan 1}))


(stop)


(defsynth send-out [in-bus 100]
  (out 0 (in in-bus 2)))

(def my-bus (audio-bus 2))

(send-out :position :tail my-bus)

(do
  (scope :bus my-bus))



