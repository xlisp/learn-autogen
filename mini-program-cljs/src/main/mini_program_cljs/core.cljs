(ns mini-program-cljs.core
  (:require-macros [mini-program-cljs.macro
                    :refer [call-promise-1 wx-fun-dev wx-fun]])
  (:require
   ;; 发布的时候需要删除下面这一行, 用gsub去掉,发布结束后再checkout回来
   [miniprogram-automator :as automator]
   [mini-program-cljs.request :refer [request]]
   [mini-program-cljs.util :refer
    [alert switch-router set-storage-sync get-storage-sync
     tel-phone set-title]]
   [mini-program-cljs.login :refer [login get-user-info]]))

(def mini-program (atom ""))

(def current-page (atom ""))

(comment
  (reset-mini-program automator)
  (reset-current-page "personal")
  (set-page-data #js {:title "测试测试32132"}))
(defn reset-mini-program [automator]
  (call-promise-1
    (fn [miniprogram]
      (reset! mini-program miniprogram))
    (.connect automator
      #js {:wsEndpoint "ws://localhost:9420"})))

(defn reset-current-page [page]
  (do
    (.callWxMethod @mini-program "navigateTo"
      #js {:url (str "/pages/" page "/" page)})
    (call-promise-1
      (fn [page-obj]
        (reset! current-page page-obj))
      (.reLaunch @mini-program page))))

(defn set-page-data [js-hash]
  (call-promise-1
    (fn [res]
      (prn "设置页面的AppData: " res))
    (.setData @current-page js-hash)))

;; (def ^:export Request #js {:get get :post post})

(def ^:export Storage
  #js {:getSync get-storage-sync
       :setSync set-storage-sync})

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
