(ns wechat-clj.util
  (:import [wechat_clj JavaHelper]))

(defn sha1 [stri]
  (JavaHelper/SHA1 stri))

(defn decrypt [encrypted-data session-key iv format]
  (JavaHelper/decrypt
    encrypted-data session-key iv "UTF-8"))
