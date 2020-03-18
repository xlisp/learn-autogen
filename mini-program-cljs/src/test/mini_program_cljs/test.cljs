(ns mini-program-cljs.test
  "只是放一些测试的函数在里面,但是函数以repl开发测试为主,而不是测试和主体代码分开的方式"
  (:require-macros [mini-program-cljs.macro
                    :refer [call-promise-1 wx-fun-dev wx-fun]])
  (:require
   [miniprogram-automator :as automator]
   [mini-program-cljs.request :refer [request]]
   [mini-program-cljs.util :refer
    [alert switch-router set-storage-sync get-storage-sync
     tel-phone set-title]]
   [mini-program-cljs.login :refer [login get-user-info]]))

(comment
  (http-get-test "https://www.test.com/testjson"))
(defn http-get-test [url]
  (mini-program-cljs.macro/async
    (let [response (mini-program-cljs.macro/await
                     (js/setTimeout (fn [] (alert "定时结束")) 2000))
          json (mini-program-cljs.macro/await #js {:data "定时器await"})]
      (alert json)
      (.log js/console json)
      json)))
