(ns mini-program-cljs.js-wx
  "处理js/wx和微信开发者模拟器对象相关的方法: 其他地方需要引用这里的mini-program和current-page对象,在repl开发测试的时候"
  (:require-macros
   [mini-program-cljs.macro
    :refer [call-promise-1 wx-fun-dev wx-fun]])
  (:require
   ;; 发布的时候需要删除下面这一行, 用gsub去掉,发布结束后再checkout回来
   [miniprogram-automator :as automator]
   [goog.object :as g]))

;; 这个函数没办法再写到另外一个文件里面
(defn log [& js-objs]
  (try
    (js-wx "showToast"
      #js {:title
           (->> js-objs
             (map #(.stringify js/JSON %))
             (clojure.string/join ""))
           :icon "none"
           :mask false
           :duration 5000})
    (catch :default e
      (js-wx "showToast"
        #js {:title (str js-objs)
             :icon "none"
             :mask false
             :duration 5000}))))

(def mini-program (atom ""))

(def current-page (atom ""))

(def export-js #js {:version "0.3.8"})

(comment
  (reset-mini-program automator)
  (reset-current-page "recharge")
  (set-page-data #js {:title (str "测试测试3213" (js/Date.now))})
  (get-page-data (fn [res] (log "Page data: " res))))
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


(defn get-page-data [op-fn]
  (call-promise-1
    op-fn
    (.data @current-page)))

(comment
  (print-class-name-wxml "recharge-button"))
(defn print-class-name-wxml [class-name]
  (call-promise-1
    (fn [res]
      (call-promise-1
        (fn [res] (log "wxml: " res))
        (.wxml res)))
    (.$ @current-page (str  "." class-name))))

(comment
  (js-wx "showToast"
    #js {:title "Hello, mini-program-cljs!"
         :icon "none"
         :mask false
         :duration 10000}))
(defn js-wx
  "支持生产环境调用js/wx,开发环境调用模拟器的callWxMethod"
  [wx-method js-args]
  (try
    ((g/get js/wx wx-method) js-args)
    (catch :default e
      (if (= (.-message  e)
            "wx is not defined")
        (.callWxMethod @mini-program wx-method js-args)
        (prn "js/wx调用错误: " e)))))
