(defsynth bizzle [out-bus 10 amp 0.5]
  (out out-bus
       (* amp
          (+ (* (decay2 (* (impulse 10 0)
                           (+ (* (lf-saw:kr 0.3 0) -0.3) 0.3))
                        0.001)
                0.3)
             (apply + (pulse [80 81]))))))

; Give it a try
(def biz (bizzle 0))
(kill biz)

;; Next, create a bus to connect the source synth with the fx synth:
(def b (audio-bus))

; All of these are based off the compander ugen.  Of course you can just use it
; directly in your synths, but it's nice to be able to stick on
(defsynth compressor-demo [in-bus 10]
  (let [source (in in-bus)]
    (out 0 (pan2 (compander source source (mouse-y:kr 0.0 1) 1 0.5 0.01 0.01)))))

(bizzle b)
(compressor-demo b)
(stop)