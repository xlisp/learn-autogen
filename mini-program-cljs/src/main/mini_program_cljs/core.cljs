(ns mini-program-cljs.core
  (:require
   [mini-program-cljs.request :refer [get post]]
   [mini-program-cljs.util :refer [alert]]
   [mini-program-cljs.login :refer [login get-user-info]]))

(def ^:export Request #js {:get get :post post})

(def ^:export
  MiniCljs (js-obj
             "alert" alert
             "login" login
             "getUserInfo" get-user-info))

(defn ^:export version [who]
  (str "0.1.4, " who "!"))
