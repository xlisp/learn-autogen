(ns mini-program-cljs.core
  (:require
   [mini-program-cljs.request :refer [request]]
   [mini-program-cljs.util :refer [alert]]
   [mini-program-cljs.login :refer [login get-user-info]]))

(def ^:export
  MiniCljs (js-obj
             "request"  request
             "alert" alert
             "login" login
             "getUserInfo" get-user-info
             "methodtest" #(+ % 100)))

(defn ^:export version [who]
  (str "0.1.4, " who "!"))
