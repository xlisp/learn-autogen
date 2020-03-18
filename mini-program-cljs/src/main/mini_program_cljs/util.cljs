(ns mini-program-cljs.util
  (:require [mini-program-cljs.js-wx :refer [js-wx]]))

;; TODO: 需要写个宏来生成这样的函数,帮它转好参数为clojure的keys风格
(defn jsx->clj
  [x]
  (into {} (for [k (.keys js/Object x)] [(keyword k) (aget x k)])))

(defn alert [title]
  (js-wx "showToast"
    #js {:title title
         :icon "none"
         :mask false
         :duration 3000}))

(defn switch-router [url]
  (js-wx "navigateTo"
    #js {:url url}))

(defn set-storage-sync [key values]
  (js-wx "setStorageSync"
    #js {:key key
         :values (.stringify js/JSON values)}))

(defn get-storage-sync [key]
  (.parse js/JSON
    (js-wx "getStorageSync" key)))

(defn tel-phone [phone]
  (js-wx "makePhoneCall"
    #js {:phoneNumber phone}))

(defn set-title [title]
  (js-wx "setNavigationBarTitle"
    #js {:title title}))
