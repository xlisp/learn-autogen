(ns mini-program-cljs.request)

;; TODO: 需要做成异步的request请求
(defn ^:export request [args]
  (.request js/wx args))
