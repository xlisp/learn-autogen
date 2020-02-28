(ns wechat-clj.jsapi
  (:require [wechat-clj.util :refer [is-weixin]]))

;; 分享类型,music、video或link，不填默认为link
;; 如果type是music或video，则要提供数据链接，默认为空
(defn ^:export wx-share-ready
  [{:keys [title success-fn cancel-fn
           desc type data-url img-url]
    :or {type "link"
         data-url ""}}]
  (let [link (first (array-seq (.split (-> js/location .-href) "#")))]
    (.ready js/wx
      (fn []
        (.onMenuShareTimeline
          js/wx
          #js {:title title
               :link link
               :imgUrl img-url
               :success success-fn
               :cancel cancel-fn})
        (.onMenuShareAppMessage
          js/wx
          #js {:title title
               :link link
               :imgUrl img-url
               :desc desc
               :type link
               :dataUrl data-url
               :success success-fn
               :cancel cancel-fn})))))

(defn vibrate-short []
  (if (is-weixin)
    (.vibrateShort
      js/wx
      #js {:success
           (fn [res]
             (js/console.log (str "开始振动..." res)))
           :fail (fn [res]
                   (js/console.log (str "振动失败..." res)))
           :completel (fn []
                        (js/console.log "振动完成"))})
    (js/console.log "浏览器不支持振动")))
