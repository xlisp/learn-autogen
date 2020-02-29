(ns mini-program-cljs.core
  (:require [mini-program-cljs.request :refer [request]]))

(def ^:export
  Request (js-obj :request request
            :methodtest #(+ %)))

(defn ^:export version [who]
  (str "0.1.1, " who "!"))
