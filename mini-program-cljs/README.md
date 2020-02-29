## [微信小程序的cljs库mini-program-cljs](https://www.npmjs.com/package/mini-program-cljs)

- [微信小程序的cljs库mini-program-cljs](#%E5%BE%AE%E4%BF%A1%E5%B0%8F%E7%A8%8B%E5%BA%8F%E7%9A%84cljs%E5%BA%93mini-program-cljs)
  - [node上引用测试](#node%E4%B8%8A%E5%BC%95%E7%94%A8%E6%B5%8B%E8%AF%95)

```sh

npm i mini-program-cljs

```


### node上引用测试

``` sh


➜  test-path npm i  mini-program-cljs
+ mini-program-cljs@0.1.1
added 1 package in 1.157s

### 主命名空间(package.json => main)

➜  test-path node
> var x = require("mini-program-cljs");
undefined
> x.
x.__defineGetter__      x.__defineSetter__      x.__lookupGetter__
x.__lookupSetter__      x.__proto__             x.constructor
x.hasOwnProperty        x.isPrototypeOf         x.propertyIsEnumerable
x.toLocaleString        x.toString              x.valueOf

> x.version(1)
'0.1.1, 1!'

### 其他的命名空间
>
> var y = require("mini-program-cljs/target/release/mini_program_cljs.request");
undefined
> y.request
[Function: request]
> y.request({})
ReferenceError: wx is not defined
    at Object.request (/Users/clojure/WeChatProjects/test-path222/node_modules/mini-prog
ram-cljs/target/release/mini_program_cljs.request.js:2:37)
>

```
