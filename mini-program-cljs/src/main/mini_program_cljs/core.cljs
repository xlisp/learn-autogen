(ns mini-program-cljs.core
  (:require-macros [mini-program-cljs.macro
                    :refer [call-promise-1 wx-fun-dev wx-fun]])
  (:require
   [mini-program-cljs.request :refer [request]]
   [mini-program-cljs.util :refer
    [alert switch-router set-storage-sync get-storage-sync
     tel-phone set-title]]
   [mini-program-cljs.login :refer [login get-user-info]]
   [mini-program-cljs.js-wx :refer [mini-program current-page]]))

(def ^:export Storage
  #js {:getSync get-storage-sync
       :setSync set-storage-sync})

(def ^:export MiniCljs
  #js {:request request
       :alert alert
       :login login
       :switchRouter switch-router
       :getUserInfo get-user-info
       :telPhone tel-phone
       :setTitle set-title})
