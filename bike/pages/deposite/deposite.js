// pages/deposite/deposite.js
var myUtils = require("../../utils/myUtils.js")

Page({

  /**
   * 页面的初始数据
   */
  data: {
  
  },

  deposite:function(){
    var that = this;
    //获取用户的手机号
    var phoneNum = myUtils.get("phoneNum")
    wx.showModal({
      title:'提示',
      content:'是否进行充值？',
      success:function (res) {
        if(res.confirm) { 
          wx.showLoading({
            title: '充值中...',
            mask: true
          })
          //先调用微信小程序支付接口 企业号才可以用
          //如果成功 向后台发送请求 更新押金
          wx.request({
            url: 'http://localhost:8080/user/deposite',
            method:'POST',
            data : {
              phoneNum:phoneNum,
              deposite:299,
              status:2      
            },
            success:function(res) {
              //关闭对话框
              wx.hideLoading()
              wx.navigateTo({
                url: '../identify/identify',
              })
              //更新用户状态 为2 交过押金
              getApp().globalData.status = 2;
              wx.setStorageSync('status', 2)
            }
          })

        } else {

        }
        
      }
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
  
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
  
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
  
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {
  
  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {
  
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {
  
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
  
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {
  
  }
})