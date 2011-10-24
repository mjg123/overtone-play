;; audio bus
(def my-bus (audio-bus))

;; produce a signal & put it into the bus
(defsynth sig-gen [out-bus 0 cps 440 dur 1]
  (let [env (env-gen (envelope [1 0.00001] [dur] :exponential))
	sig (* env (saw cps))]
    
  (out out-bus sig)))

;; something to read the bus & make it audible
(defsynth send-out [in-bus 3]
  (let [src (in in-bus)]
    (out 0 src)))

;; switch it on
(send-out my-bus)

;; looper will call sig-gen repeatedly
(defn looper [b]
  (sig-gen my-bus)
  (apply-at (m (inc b)) #'looper (inc b) []))

(def m (metronome 128))
(looper (m))

;; but I only get a small snippet of sound out when I call
;;   (send-out my-bus)
;; why?


(stop)