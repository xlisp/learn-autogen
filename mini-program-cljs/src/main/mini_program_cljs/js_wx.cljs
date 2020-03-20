(ns mini-program-cljs.js-wx
  "处理js/wx和微信开发者模拟器对象相关的方法: 其他地方需要引用这里的mini-program和current-page对象,在repl开发测试的时候"
  (:require-macros
   [mini-program-cljs.macro
    :refer [call-promise-1 wx-fun-dev wx-fun evaluate-args c-log]])
  (:require
   ;; 发布的时候需要删除下面这一行, 用gsub去掉,发布结束后再checkout回来
   [miniprogram-automator :as automator]
   [goog.object :as g]
   [clojure.string :as str]))

(declare js-wx)

(comment
  (log #js {:aa 11} #js {:bb 22 :cc 33}))
;; 这个函数没办法再写到另外一个文件里面
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

(def mini-program (atom ""))

(def current-page (atom ""))

(def export-js #js {:version "0.3.8"})

(comment
  (reset-mini-program automator)
  (reset-current-page)

  (reset-page "recharge")
  (set-page-data #js {:title (str "测试测试3213" (js/Date.now))})
  (get-page-data (fn [res] (log "Page data: " res))))
(defn reset-mini-program [automator]
  (call-promise-1
    (fn [miniprogram]
      (reset! mini-program miniprogram))
    (.connect automator
      #js {:wsEndpoint "ws://localhost:9420"})))

(defn reset-current-page []
  (call-promise-1
    (fn [page-obj]
      (prn "当前页面为: " (.-path page-obj))
      (reset! current-page page-obj))
    (.currentPage @mini-program)))

(defn reset-page [page]
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
  (call-page-method
    "wxPay"
    #js {}
    (fn [res] (log "----" res))))
(defn call-page-method
  "调用小程序Page({})内部的方法"
  [method js-args op-fn]
  (call-promise-1
    op-fn
    (if (nil? js-args)
      (.callMethod @current-page method)
      (.callMethod @current-page method js-args))))

;; 大全的模拟器方法: https://developers.weixin.qq.com/miniprogram/dev/devtools/auto/miniprogram.html
(comment
  (evaluate (fn [] (js/getApp))) ;; alert 出来一堆的用户登陆后的信息
  (evaluate (fn [] (-> (js/getApp) .-globalData .-userInfo))) ;;=> 微信信息打印出来了
  (evaluate (fn [] (.setStorageSync js/wx "test" "dsadasdsa"))) ;; 无效,还是要CallWxMethod才行
  (evaluate (fn [] js/wx))
  ;; ===>>>
  ;;{ env: { USER_DATA_PATH: 'http://usr' },
  ;;  error:
  ;;   { OK: 0,
  ;;     Global_APINoPermission: 10012,
  ;;     Global_APINoAuthorization: 10022,
  ;;     Global_FileStorageNotEnough: 10031,
  ;;     Render_CanvasIllegalInvocation: 13013,
  ;;     Render_FontFileInvalid: 13023,
  ;;     Render_ImageLoadFailed: 13033,
  ;;     Network_RequestTimeout: 14012,
  )
(defn evaluate
  "往 AppService 注入代码片段并返回执行结果"
  [code-fn]
  (call-promise-1
    (fn [res] (c-log @mini-program "eval code: " res))
    (.evaluate  @mini-program code-fn)))

(comment
  (evaluate-1 (fn [arg1]  (js/console.log arg1) ) "aaaa") ;;=> 终于控制台打印出来了
  (c-log @mini-program "aaaa" "bbb" "cccc"
    #js {:aaa 111 :bb "222dsadsa" :cc #js {:ooo 11 :bb "33"}})
  )
(defn evaluate-1
  [code-fn arg1]
  (call-promise-1
    (fn [res] (c-log @mini-program "eval code 1: " res))
    (.evaluate  @mini-program code-fn arg1)))

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
