(ns mini-program-cljs.request)

(defn request [url method data]
  (js/Promise.
    (fn [^js resolve ^js reject]
      (.request js/wx
        #js {:url url
             :method method
             :data data
             :header {"Content-Type" "application/json; charset=UTF-8"}
             :success (fn [^js request]
                        (js/console.log request)
                        (resolve request))
             :fail (fn [^js error])}))))
