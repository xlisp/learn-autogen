(ns mini-program-cljs.core
  (:require
   [mini-program-cljs.request :refer [request]]
   [mini-program-cljs.util :refer [alert switch-router]]
   [mini-program-cljs.login :refer [login get-user-info]]))

;; (def ^:export Request #js {:get get :post post})

(def ^:export
  MiniCljs
  #js {:request request
       :alert alert
       :login login
       :switchRouter switch-router
       :getUserInfo get-user-info})

(defn ^:export version [who]
  (str "0.2.1, " who "!"))
