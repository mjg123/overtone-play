(ns waveshaping)

(use 'overtone.live)
(use 'tutorial.designing.wavey)

(defn minimax
  "returns x, clipped by mn and mx"
  [mn x mx]
  (max mn (min x mx)))

(def half-clip (create-buffer -1 1 #(minimax -0.5 % 0.5) 512))
(def cheby0 (create-buffer -1 1 (fn [] 1) 512))
(def cheby1 (create-buffer -1 1 identity 512))
(def cheby2 (create-buffer -1 1 #(- (* 2 % %) 1) 512))
(def cheby3 (create-buffer -1 1 #(- (* 4 % % %) (* 3 %)) 512))
(def cheby4 (create-buffer -1 1 #(- (* 8 % % % %) (* 8 % %) -1) 512))



(def shapey (buffer 1024))
(buffer-write! shapey (data->wavetable cheby4))

(defsynth shapy [cps 440]
  (let [sig (sin-osc cps)]
    (out 0 [sig (shaper shapey sig)])))

(shapy 440)
(stop)

(scope :bus 0)
(scope :bus 1)

