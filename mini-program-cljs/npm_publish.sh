#!/bin/bash
## 发布线上的时候,去掉模拟器
ruby -p -i -e 'gsub("[miniprogram-automator :as automator]", "")' src/main/mini_program_cljs/js_wx.cljs
yarn release
git checkout src/main/mini_program_cljs/js_wx.cljs

npm publish
