(ns tutorial.pieces.p01
  (:use [overtone.live])
  (:use [overtone.viz.scope])
  )

;; ----------------- patchbay -----------------

(comment (def cloud-bus (audio-bus 2)))

;; ------------------ instrs ------------------

(defsynth cloudy [out-bus 0 amp 1 cps 440 bwr 0.1]
  (let [nnn 256

	sweeps [(+ 2200 (* 1000 (sin-osc (/ (i-rand) nnn))))
		(+ 2200 (* 1000 (sin-osc (/ (i-rand) nnn))))
		(+ 2200 (* 1000 (sin-osc (/ (i-rand) nnn))))
		(+ 2200 (* 1000 (sin-osc (/ (i-rand) nnn))))]
	
	rezzy [(apply + (resonz (white-noise) sweeps bwr))
	       (apply + (resonz (white-noise) sweeps bwr))]

	spread (map #(* cps %) [1 2])
	
	filtrd [(apply + (resonz (rezzy 0) spread 0.001))
		(apply + (resonz (rezzy 1) spread 0.001))]]
	
    (out out-bus (* amp filtrd))))
	  
(def nuage (cloudy 0 4 880 0.1))
(kill nuage)

;;

(defn gen-spread [type]
  (cond
   (= type :squ) [1 0 1/3  0 1/5  0 1/7]
   (= type :tri) [1 0 -1/9 0 1/25 0 -1/49]
   (= type :sin) [1 0]
    
   :else [1 0]))

(defsynth happy-harpy [out-bus 0 amp 1 m 60 dur 2]
  (let [env (env-gen (envelope [1 0.000001] [dur] :exponential) :action FREE)
	dclck (env-gen (envelope [0 1 1 0] [0.01 (- dur 0.02) 0.01]))

	pch (midicps m)
	pch (+ pch (* 0.008 pch (sin-osc 8)))
	
	osc (apply +
		   (map-indexed
		    (fn [idx itm] (* itm (sin-osc (* (inc idx) pch))))
		    [1 0 -1/9 0 1/25 0 0 1/8]))
	
	snd (* amp osc env dclck)]
    
    (out out-bus [snd (* -1 snd)])))

(def n (happy-harpy 0 1 75 5))
(kill n)

(scope-on)


(stop)

(def h-bus (audio-bus 2))

(defsynth master-out [amp 1 pan 0]
  (let [src (in h-bus 2)]
    (out 0 (pan2 src)))

(def master (master-out))
(kill master)

(defsynth compressor-demo [in-bus 10]
  (let [source (in in-bus)]
    (out 0 (pan2 (compander source source (mouse-y:kr 0.0 1) 1 0.5 0.01 0.01)))))

(stop)

;; -------------------- score -------------------------

(def metro (metronome 256))

(def hh-out h-bus)

(defn ply-rp [b r]
  (at (metro (+ 0 b)) (happy-harpy 0 1 r 16))
  (at (metro (+ 2 b)) (happy-harpy 0 1 (+ 3 r) 4))
  (at (metro (+ 4 b)) (happy-harpy 0 1 (+ 7 r) 4))
  (at (metro (+ 6 b)) (happy-harpy 0 1 (+ 10 r) 4)))

(defn arp-seq [b]
  (ply-rp b 72)
  (ply-rp (+ 8 b) 72)
  (ply-rp (+ 16 b) 75)
  (ply-rp (+ 24 b) 77))

(defn trk [b]
; (cloudy 0 2 (* 8 880) 1)
  (arp-seq (+ b 0))
  (arp-seq (+ b 32)))

(trk (metro))
(stop)

(ply-rp (metro) 79)

(osc-handle server "/2/push1"
	    (fn [{[v] :args}]
	      (when (< 0 v)
		(trk (metro)))))