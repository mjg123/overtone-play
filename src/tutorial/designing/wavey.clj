(ns tutorial.designing.wavey
  (:use [overtone.core]))

;;;;;;;;;;;;;;;;;;;

(defn create-buffer
  "calls f with s values linearly interpolated with mn <= values < mx.
   returns a seq of s values"
  [mn mx f s]
  (let [point-range (- mx mn)
	rangemap #(+ mn (/ (* % point-range) s))]
    (map #(f (rangemap %)) (range 0 s))))

(defn data->wavetable
  "Convert a sequence of floats into wavetable format. Result will be twice the
  size of source data. Length of ource data must be a power of 2 for SC
  compatability."
  [data]
  (let [v   (vec data)
        cnt (count v)
        res (float-array (* 2 cnt))]
    (dorun
     (map (fn [idx]
	    (let [a (get v idx)
		  b (get v (inc idx))
		  r-idx (* 2 idx)]
	      (aset-float res r-idx (-  (* 2 a) b))
	      (aset-float res (inc r-idx) (- b a))))
	  (range (dec cnt))))
    (let [a (get v (dec cnt))
          b (get v 0)]
      (aset-float res (* 2 (dec cnt)) (- (* 2 a) b))
      (aset-float res (inc (* 2 (dec cnt))) (- b a)))
    (seq res)))

(def two-pi (* 2 Math/PI))

(def sin-buf (make-buffer two-pi #(Math/cos %)))
(def squ-buf (make-buffer 2 #(if (> 1 %) 1 -1)))
(def tri-buf (make-buffer 2 #(if (> 1 %)
			       (- % 0.5)
			       (- 1.5 %))))
(def imp-buf (make-buffer 1 #(if (> 0.01 %) 1 0)))

(def buf1 sin-buf)

(demo (osc-n buf1 440))

;;;;;;;;;;;;;;;;;;;;

(comment
  (defn show-buffer [buffy]
    (let [buffloats (into '[] (buffer-read buffy))]
      (incanter.core/view (incanter.charts/xy-plot (range (count buffloats)) buffloats))))
)


