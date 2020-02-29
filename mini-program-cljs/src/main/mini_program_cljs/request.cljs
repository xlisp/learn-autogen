(ns mini-program-cljs.request)

(defn promise
  ([] js/Promise.)
  ([callback] (js/Promise. callback)))

(defn request
  [{:keys [url method data token]}]
  (promise
    (fn [resolve reject]
      (.request js/wx
        #js {:url url
             :method (name method)
             :data (if (= method :GET)
                     data
                     (.stringify js/JSON data))
             :header (merge {:Content-Type "application/json; charset=UTF-8"}
                       (js->clj token))
             :success (fn [^js request]
                        (if (= 200 (.-code (.-data request)))
                          (resolve (.-data request))
                          (do
                            (reject (.-data request)))))
             :fail (fn [^js error]
                     (reject (.-data error)))}))))

(defn get [url params token]
  (request {:url url :method :GET :data params :token token}))

(defn post [url params token]
  (request {:url url :method :POST :data params :token token}))
