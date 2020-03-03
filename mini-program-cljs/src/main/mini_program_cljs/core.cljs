(ns mini-program-cljs.core
  (:require-macros [mini-program-cljs.macro])
  (:require
   [mini-program-cljs.request :refer [request]]
   [mini-program-cljs.util :refer [alert switch-router set-storage-sync get-storage-sync]]
   [mini-program-cljs.login :refer [login get-user-info]]))

(defn http-get-test [url]
  (mini-program-cljs.macro/async
    (let [response (mini-program-cljs.macro/await
                     (js/setTimeout (fn [] (alert "定时结束")) 2000))
          json (mini-program-cljs.macro/await #js {:data "定时器await"})]
      (.log js/console json))))

(def ^:export Storage #js {:getSync get-storage-sync :setSync set-storage-sync})

;; (def ^:export Request #js {:get get :post post})

(def ^:export
  MiniCljs
  #js {:request request
       :alert alert
       :login login
       :switchRouter switch-router
       :getUserInfo get-user-info
       :isEmpty empty?
       :awaitTest http-get-test})

(defn ^:export version [who]
  (str "0.2.1, " who "!"))
