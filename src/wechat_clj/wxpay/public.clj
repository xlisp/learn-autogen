(ns wechat-clj.wxpay.public
  (:import [wechat_clj JavaHelper]
           [java.util UUID]))

;; TODO: 这里写一个cljc宏,生成所有的前端的函数,重新命名为下划线的名字,加上success & erro的fn作为参数
(def default-jsapi-list
  ["onMenuShareQZone"
   "updateAppMessageShareData"
   "updateTimelineShareData"
   "onMenuShareTimeline"
   "onMenuShareAppMessage"
   "onMenuShareQQ"
   "onMenuShareWeibo"
   "hideMenuItems"
   "showMenuItems"
   "hideAllNonBaseMenuItem"
   "showAllNonBaseMenuItem"
   "translateVoice"
   "startRecord"
   "stopRecord"
   "onRecordEnd"
   "playVoice"
   "pauseVoice"
   "stopVoice"
   "uploadVoice"
   "downloadVoice"
   "chooseImage"
   "previewImage"
   "uploadImage"
   "downloadImage"
   "getNetworkType"
   "openLocation"
   "getLocation"
   "hideOptionMenu"
   "showOptionMenu"
   "closeWindow"
   "scanQRCode"
   "chooseWXPay"
   "openProductSpecificView"
   "addCard"
   "chooseCard"
   "openCard"])

(defn hash-params->url [hash]
  (if (empty? hash)
    ""
    (str "?"
      (clojure.string/join
        "&"
        (map
          (fn [item]
            (str (name (first item)) "=" (last item)))
          hash)))))

(defn url-signature
  [{:keys [official-account-jsapi-ticket url debug appid]
    :or {debug true
         jsapi-list default-jsapi-list}}]
  (let [noncestr (str (UUID/randomUUID))
        timestamp (Long/toString (/ (System/currentTimeMillis) 1000))
        signature (-> (str "jsapi_ticket="
                        official-account-jsapi-ticket
                        "&noncestr=" noncestr
                        "&timestamp=" timestamp
                        "&url=" url)
                    JavaHelper/SHA1)]
    {:debug debug
     :appId appid
     :timestamp timestamp
     :nonceStr noncestr
     :signature signature
     :jsApiList default-jsapi-list}))
