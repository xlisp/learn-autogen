## [微信小程序的cljs库mini-program-cljs](https://www.npmjs.com/package/mini-program-cljs)

- [微信小程序的cljs库mini-program-cljs](#%E5%BE%AE%E4%BF%A1%E5%B0%8F%E7%A8%8B%E5%BA%8F%E7%9A%84cljs%E5%BA%93mini-program-cljs)
  - [安装`mini-program-cljs`](#%E5%AE%89%E8%A3%85mini-program-cljs)
  - [小程序的登陆](#%E5%B0%8F%E7%A8%8B%E5%BA%8F%E7%9A%84%E7%99%BB%E9%99%86)
  - [异步的`wx.request`使用](#%E5%BC%82%E6%AD%A5%E7%9A%84wxrequest%E4%BD%BF%E7%94%A8)

### 安装`mini-program-cljs`

```sh

npm i mini-program-cljs

## Mac的微信开发者工具再编译:
/Applications/wechatwebdevtools.app/Contents/MacOS/cli --build-npm ~/YourWechatProject

```

``` javascript
import { MiniCljs } from 'mini-program-cljs';

MiniCljs.alert("Hello, mini-program-cljs!");
```

### 小程序的登陆

``` html
<button open-type="getUserInfo" bindgetuserinfo="getUserInfo"> 获取用户信息登陆</button>
```

```js
Page({
    getUserInfo: function(e) {
      MiniCljs.login({successFn: function(res) {console.log(res)},
                      iv: e.detail.iv,
                      encryptedData: e.detail.encryptedData})
    }
})

//=> res: 将res的内容传递给后端的接口即可获取用户的信息(openid等)
{code: "043Ndldz0WVpcc1Cqpcz03Xgdz0...."
 encryptedData: "NxmCRAyhhMT2jzdcu012VJznC6HH0H....."
 iv: "jbjTusiIz2tfzt1ddU..=="}

```

### 异步的`wx.request`使用

``` javascript

MiniCljs.request(
  {url: 'https://www.test.com/login/jscode2session',
   method: "GET",
   data: {encrypted_data: res.encryptedData,
   iv: res.iv,
   jscode: res.code}}).then(res => {
   console.log("登陆返回数据");
   console.log(res);
   if(res.statusCode == 200){
     // ...
   } else {
     // ...
   }
   }).catch(err => {
   MiniCljs.alert(err.message)
   })
```
