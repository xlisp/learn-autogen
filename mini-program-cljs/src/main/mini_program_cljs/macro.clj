;; the file form: https://github.com/roman01la/cljs-async-await
(ns mini-program-cljs.macro
  (:refer-clojure :exclude [await])
  (:require [cljs.analyzer :as ana]
            [cljs.compiler :as compiler]
            [clojure.string :as str]))

(def ^:dynamic *in-async* false)

(alter-var-root #'ana/specials #(conj % 'async* 'await*))

(defmethod ana/parse 'await*
  [op env [_ expr :as form] _ _]
  (when-not *in-async*
    (throw (ana/error env "Can't await outside of async block")))
  (when (not= 2 (count form))
    (throw (ana/error env "Wrong number of args to await")))
  {:env env
   :op :await
   :children [:expr]
   :expr (ana/analyze env expr)
   :form form})

(defmethod ana/parse 'async*
  [op env [_ & exprs :as form] _ _]
  (binding [*in-async* true]
    (let [statements (ana/disallowing-recur
                       (->> (butlast exprs)
                         (mapv #(ana/analyze (assoc env :context :statement) %))))
          ret (ana/disallowing-recur
                (ana/analyze (assoc env :context :return) (last exprs)))
          children [:statements :ret]]
      {:op :async
       :env env
       :form form
       :statements statements
       :ret ret
       :ret-tag 'js/Promise
       :children children})))

(defmethod compiler/emit* :await
  [{:keys [env expr]}]
  (when (= :return (:context env))
    (compiler/emits "return "))
  (compiler/emits "(await ")
  (compiler/emits (assoc-in expr [:env :context] :expr))
  (compiler/emits ")"))

(defmethod compiler/emit* :async
  [{:keys [statements ret env]}]
  (when (= :return (:context env))
    (compiler/emits "return "))
  (compiler/emitln "(async function (){")
  (doseq [s statements]
    (compiler/emitln s))
  (compiler/emit ret)
  (compiler/emitln "})()"))

;; ====== Public API ======

(defmacro async
  "Wraps body into self-invoking JavaScript's async function, returns promise"
  [& body]
  `(~'async* ~@body))

(defmacro await
  "Suspends execution of current async block and returns asynchronously resolved value"
  [expr]
  `(~'await* ~expr))

(defmacro await-all
  "Same as (seq (.all js/Promise coll)), but for easier usage within async blocks"
  [coll]
  `(seq (~'await* (.all js/Promise ~coll))))

(defmacro await-first
  "Same as (.race js/Promise coll), but for easier usage within async blocks"
  [coll]
  `(~'await* (.race js/Promise ~coll)))

;; -------------------
(defmacro call-promise
  [{:keys [then-fn catch-fn]} & body]
  `(-> ~@body
     (.then
       (fn [obj#]
         (js/console.log "Get Promise Object: " obj#)
         (~then-fn obj#)))
     (.catch
       (fn [e#]
         (js/console.error "Promise Error: " e#)
         (~catch-fn e#)))))

(comment
  (demo.core/call-promise
    {:then-fn (fn [miniprogram]
                (reset! mini-program miniprogram))
     :catch-fn (fn [x] x)}
    (.connect  automator
      {:wsEndpoint "ws://localhost:9420"})))

(defmacro call-promise-1
  "只是取出来promise的值,不关心错误"
  [then-fn & body]
  `(-> ~@body
     (.then
       (fn [obj#]
         (js/console.log "Get Promise Object: " obj#)
         (~then-fn obj#)))
     (.catch
       (fn [e#]
         (js/console.error "Promise Error: " e#)))))

(comment
  (jsname->clj "offPageNotFound")
  ;; => "off-page-not-found"
  (clj->jsname "off-page-not-found")
  ;; => "offPageNotFound"
  )
(defn jsname->clj [stri]
  (->>
    (seq (str/replace stri "_" "-"))
    (map (fn [st]
           (let [s (str st)]
             (if (re-find #"[A-Z]" s)
               (str "-" (str/lower-case s)) s))))
    (str/join "")))

(defn clj->jsname [stri]
  (str/replace stri #"-([a-z])"
    #(str (str/upper-case (last %1)))))

(comment
  ;; => 1. 生成函数:
  (clojure.pprint/pprint (macroexpand-1 '(wx-fun-dev mini-pro checkSession)))
  ;; => 2. 变成调用: (wx-login :success (fn [res] res) :fail (fn [res] 111))
  ;; ---- 生产release
  (clojure.pprint/pprint (macroexpand-1 '(wx-fun checkSession)))


  (wx-fun-dev @mini-program checkSession) ;; => #'mini-program-cljs.core/wx-check-session

  (call-promise-1
    (fn [res] (prn "----" res))
    (wx-check-session :success (fn [res] res)))
  ;;  Get Promise Object:  { errMsg: 'checkSession:ok' }
  ;;  "----" #js {:errMsg "checkSession:ok"}
  )
(defmacro wx-fun-dev [mini-pro fname]
  `(defn ~(symbol (str "wx-" (jsname->clj (str fname))))
     [& args#]
     (.callWxMethod ~mini-pro
       ~(str fname)
       (apply hash-map args#))))

(defmacro wx-fun [fname]
  `(defn ~(symbol (str "wx-" (jsname->clj (str fname))))
     [& args#]
     (~(symbol (str "." fname)) js/wx
      (apply hash-map args#))))

(comment
  (clojure.pprint/pprint
    (macroexpand-1 '(defn-js test-fun [:url :method :data :header]
                      (do 1 2) (let [x 1] x) )))
  ;; test in cljs:
  ;; (test-fun #js {:url 111 :method 222})
  ;; => [111 222]
  (defn-js test-fun [:url :method :data :header]
    [url method]))
(defmacro defn-js
  "生成js导出需要的cljs函数: 用宏来包装副作用和领域特殊和不干净的东西,其他都可以按照lisp的风格来设计"
  [fun-name fun-args & body]
  `(do (defn ~fun-name [^js options#]
         (let [{:keys [~@(map symbol fun-args)]}
               (into {}
                 (for [k# (.keys js/Object options#)]
                   [(keyword k#) (aget options# k#)]))]
           ~@body))
       (set! (~(symbol (str ".-" (clj->jsname (str fun-name))))
              mini-program-cljs.core/MPCljs) ~fun-name)))
