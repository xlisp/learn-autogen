(defproject wechat-clj "0.1.1-SNAPSHOT"
  :description "微信支付,网络授权,加密库: 包括了前端的wx签名等"
  :url "https://clojars.org/wechat-clj"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/clojurescript "1.10.597"]
                 [cheshire "5.10.0"]
                 [com.github.wxpay/wxpay-sdk "0.0.3"]
                 [clj-http "3.10.0"]]
  :java-source-paths ["src/java"]
  :repl-options {:init-ns wechat-clj.core})
