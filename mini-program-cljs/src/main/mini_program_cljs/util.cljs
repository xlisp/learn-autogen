(ns mini-program-cljs.util
  (:require-macros [mini-program-cljs.macro
                    :refer [call-promise-1]])
  (:require [mini-program-cljs.js-wx :refer [js-wx]]))

(defn jsx->clj
  [x]
  (into {} (for [k (.keys js/Object x)] [(keyword k) (aget x k)])))

(comment
  (alert "test"))
(defn alert [title]
  (js-wx "showToast"
    #js {:title title
         :icon "none"
         :mask false
         :duration 3000}))
(comment
  (switch-router "/pages/login/login"))
(defn switch-router [url]
  (js-wx "navigateTo"
    #js {:url url}))

(comment
  ;; 不知为何无效
  (set-storage-sync "atestkey" #js {:url 1111 :test2 "1132321" :datas #js {:aaa 111 :bbb 222}}))
(defn set-storage-sync [tkey values]
  (call-promise-1
    (fn [obj] (prn "设置storage ok: " obj))
    (js-wx "setStorageSync"
      #js {:key tkey
           :values (.stringify js/JSON values)})))

(comment
  ;; 1. 设置值: wx.setStorageSync("testkey", JSON.stringify({dsadsa: 111}))
  (get-storage-sync "testkey" #(prn "----:" %)) ;;=> `"----:" #js {:dsadsa 111}`
  )
(defn get-storage-sync [tkey op-fn]
  (call-promise-1
    (fn [value]
      (op-fn (.parse js/JSON value)))
    (js-wx "getStorageSync" tkey)))

(comment
  ;; ok test
  (tel-phone "10086"))
(defn tel-phone [phone]
  (js-wx "makePhoneCall"
    #js {:phoneNumber phone}))

(comment
  ;; js-wx方法测试很不错!
  (set-title "设置导航标题"))
(defn set-title [title]
  (js-wx "setNavigationBarTitle"
    #js {:title title}))
