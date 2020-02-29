(ns mini-program-cljs.core
  (:require [mini-program-cljs.request]))

(defn ^:export version [who]
  (str "0.1.1, " who "!"))
