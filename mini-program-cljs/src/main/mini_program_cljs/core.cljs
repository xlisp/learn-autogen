(ns mini-program-cljs.core
  (:require-macros [mini-program-cljs.macro
                    :refer [call-promise-1 wx-fun-dev wx-fun]])
  (:require
   [mini-program-cljs.request :refer [request]]
   [mini-program-cljs.util :refer
    [alert switch-router set-storage-sync get-storage-sync
     tel-phone set-title]]
   [mini-program-cljs.login :refer [login get-user-info]]))

(def mini-program (atom ""))

(comment
  (require '[miniprogram-automator :as automator])
  (reset-mini-program automator)
  ;;
  (wx-fun-dev @mini-program checkSession) ;; => #'mini-program-cljs.core/wx-check-session

  (call-promise-1
    (fn [res] (prn "=====" res))        ;;=> "=====" #js {:errMsg "checkSession:ok"}
    (wx-check-session :success (fn [res] res)))
  )
(defn reset-mini-program [automator]
  (call-promise-1
    (fn [miniprogram]
      (reset! mini-program miniprogram))
    (.connect automator
      #js {:wsEndpoint "ws://localhost:9420"})))

(comment
  (http-get-test "https://www.test.com/testjson"))
(defn http-get-test [url]
  (mini-program-cljs.macro/async
    (let [response (mini-program-cljs.macro/await
                     (js/setTimeout (fn [] (alert "定时结束")) 2000))
          json (mini-program-cljs.macro/await #js {:data "定时器await"})]
      (alert json)
      (.log js/console json)
      json)))

(def ^:export Storage
  #js {:getSync get-storage-sync :setSync set-storage-sync})

;; (def ^:export Request #js {:get get :post post})

(def ^:export MiniCljs
  #js {:request request
       :alert alert
       :login login
       :switchRouter switch-router
       :getUserInfo get-user-info
       :isEmpty empty?
       :awaitTest http-get-test
       :telPhone tel-phone
       :setTitle set-title})
