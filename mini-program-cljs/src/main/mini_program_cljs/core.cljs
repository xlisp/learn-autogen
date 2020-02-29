(ns mini-program-cljs.core
  (:require [mini-program-cljs.request :refer [request]]))

(def ^:export
  MiniCljs (js-obj
             "request"  request
             "methodtest" #(+ %)))

(defn ^:export version [who]
  (str "0.1.1, " who "!"))
