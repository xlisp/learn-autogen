(ns mini-program-cljs.request)

(defn request [args]
  (.request js/wx args))
