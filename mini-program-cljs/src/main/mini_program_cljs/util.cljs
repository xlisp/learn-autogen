(ns mini-program-cljs.util
  (:require-macros [mini-program-cljs.macro
                    :refer [call-promise-1 defn-js c-log]])
  (:require
   [mini-program-cljs.js-wx :refer [js-wx] :as jswx]
   [clojure.string :as str]))

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
  (log #js {:aaa 321321 :bbb 3321 :ccc #js {:ttt 2321 :a 1}}
    #js {:uuiiuuiiuuii 321321 :bbb 3321 :ccc #js {:ttt 2321 :uuii 1}}))
(defn log [& js-objs]
  (let [stri (->> js-objs
               (map #(.stringify js/JSON %))
               (str/join ""))]
    (try
      (js-wx "showToast"
        #js {:title stri

             :icon "none"
             :mask false
             :duration 5000})
      (catch :default e
        (js-wx "showToast"
          #js {:title (str js-objs)
               :icon "none"
               :mask false
               :duration 5000})))))

(declare get-current-route)

(comment
  (switch-router "/pages/login/login"))
(defn switch-router [url]
  (if (= (get-current-route) url)
    (js/console.log "在当前页面,不需要跳转")
    (js-wx "navigateTo"
      #js {:url url})))

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

(comment
  ;; 调用CallWxMethod的时候,会返回一个promise,但是直接调用js/wx却不会
  (call-promise-1
    (fn [res] (alert (str (.-result res)))) ;;=> 能打印出来扫出来的码
    (scan-code (fn [res] (alert (str res))))))
(defn scan-code [success]
  (js-wx "scanCode"
    #js {:success success}))

;; Page内部或者util里面才能执行成功的方法, 或者再控制台也能执行成功
(defn get-current-page []
  (let [pages (js/getCurrentPages)
        len (.-length pages)
        current-page (aget pages (dec len))]
    current-page))

(defn get-current-route [current-page]
  (.-route current-page))

(defn get-current-options [current-page]
  (.-options current-page))
