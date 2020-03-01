(ns mini-program-cljs.core
  (:require
   [mini-program-cljs.request :refer [request]]
   [mini-program-cljs.util :refer [alert switch-router set-storage-sync get-storage-sync]]
   [mini-program-cljs.login :refer [login get-user-info]]))

(def ^:export Storage #js {:getSync get-storage-sync :setSync set-storage-sync})

;; (def ^:export Request #js {:get get :post post})

(def ^:export
  MiniCljs
  #js {:request request
       :alert alert
       :login login
       :switchRouter switch-router
       :getUserInfo get-user-info
       :isEmpty empty?})

(defn ^:export version [who]
  (str "0.2.1, " who "!"))
