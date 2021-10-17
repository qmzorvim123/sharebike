// app.js
App({
  onLaunch() {

  },
  //所有页面共享的数据
  globalData: {
    userInfo: null,
    status:0,  //优先从磁盘中取值
    phoneNum:""
  }
})
