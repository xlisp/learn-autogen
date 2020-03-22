(ns wechat-clj.util
  (:require [clojure.xml]
            [buddy.core.hash :as hash]
            [buddy.core.codecs :refer [bytes->hex]]
            [clojure.string :as str])
  (:import [wechat_clj JavaHelper]))

(defn sha1 [stri]
  (JavaHelper/SHA1 stri))

(defn decrypt [encrypted-data session-key iv format]
  (JavaHelper/decrypt
    encrypted-data session-key iv "UTF-8"))

(defn parse-string-xml
  [st]
  (-> st .getBytes java.io.ByteArrayInputStream. clojure.xml/parse))

(defn get-hash-from-parsed-xml
  [parsed-xml]
  (->>
    parsed-xml :content
    (filter #(= (type %) clojure.lang.PersistentStructMap))
    (map #(vector (:tag %) (first (:content %))))
    (into {})))

(defn parse-wxpay-callback-xml
  "解析微信支付成功回调的xml为hash"
  [stri]
  (-> stri
    parse-string-xml get-hash-from-parsed-xml))

(defn out-trade-no-to-id
  [out-trade-no]
  "如果用uuid去掉-来作为订单号,那可以用这个函数转回uuid"
  (let [subs-list [[0 8] [8 12] [12 16] [16 20] [20 32]]]
    (->> subs-list
      (map #(subs out-trade-no (first %) (last %)))
      (clojure.string/join "-"))))

(defn gen-signature
  [payload secret-key]
  (let [url-str (->> (sort-by first payload)
                  (map (partial str/join "="))
                  (str/join "&"))
        url-str (str url-str "&key=" secret-key)
        sig (str/upper-case (bytes->hex (hash/md5 url-str)))]
    sig))
