(ns wavetable)

(use 'overtone.live)

;;;;;;;;;;;;;;;;;;;

(defn make-buffer [f]
 (let [s 1024
       b (buffer s)]
   (buffer-write! b (map #(f (/ (* % 2 Math/PI) s)) (range 0 s)))
   b))

(def sin-buf (make-buffer #(Math/cos %)))
(def squ-buf (make-buffer #(if (> Math/PI %) 1 -1)))
(def tri-buf (make-buffer #(if (> Math/PI %)
			     (- (/ % Math/PI) 0.5)
			     (- 1.5 (/ % Math/PI)))))
(def imp-buf (make-buffer #(if (> 0.1 %) 1 0)))

(def buf1 squ-buf)

(demo (sin-osc 440))
(demo (osc-n buf1 440))
;;;;;;;;;;;;;;;;;;;;

(comment)
  "incanter stuff"
  (def buffloats (into '[] (buffer-read buf1)))
  (ns foo)
  (in-ns 'foo)
  (use '(incanter core charts datasets))
  (incanter.core/view (incanter.charts/xy-plot (range 1024) user/buffloats))



