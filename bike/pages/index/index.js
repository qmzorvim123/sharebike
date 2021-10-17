var myUtils = require("../../utils/myUtils")

// index.js
// 获取应用实例
Page({
  data: {
    longitude: 117.337747,
    latitude: 39.992895,
    relog:0,
    relat:0,
    controls : [],
    markers:[
      {
        iconPath:"/images/bike@white.png",
        width:35,
        height:40,
        longitude:116.337747,
        latitude:39.992895
      }
    ]
    // tempmarker:[]
    
  },
  
  onLoad :function() {
    var that = this;
    wx.getLocation({ 
      success : function(res) {
        var longitude = res.longitude
        var latitude = res.latitude
        that.setData({ 
          longitude : longitude,
          latitude : latitude
        })
        findBikes(longitude,latitude,that)
      }, 
    });
    
    // console.log(that.data.controls);
    wx.getSystemInfo({
      success: (result) => {
        var windowWidth = result.windowWidth
        var windowHeight = result.windowHeight
        that.setData({
          controls: [
            {
              id:1, 
              iconPath: "/images/lock.png",
              position: {
                width:100,
                height:40,
                left:windowWidth/2-50,
                top:windowHeight-60
              },
              clickable:true
            },
            {
              //定位按钮
              id:2,
              iconPath:"/images/locate.png",
              position: {
                width:40,
                height:40,
                left:10,
                top:windowHeight-60
              },
              clickable:true
            },
            {
              //大头针表示中心点位置
              id:3,
              iconPath:"/images/cur.png",
              position:{
                width:30,
                height:30,
                left:windowWidth/2-15,
                top:windowHeight/2-25
              },
              clickable :true
            },
            {
              //充值按钮
              id:4,
              iconPath:"/images/chongzhi.png",
              position: {
                width:40,
                height:40,
                left:windowWidth-45,
                top:windowHeight-100
              },
              clickable:true
            },
            {
              //添加车辆
              id:5,
              iconPath:"/images/add.png",
              position:{
                width:35,
                height:35,
              },
              clickable:true
            },
            {
              //报警
              id:6,
              iconPath:"/images/baojing.png",
              position: {
                width:35,
                height:35,
                left:windowWidth-42,
                top:windowHeight-60
              },
              clickable:true
            }
          ]
        })
      },
    })
  },
  onReady:function() {
    
    this.mapCtx = wx.createMapContext('myMap');
  },

  /**
   * 移动后 地图视野发生变化 
   */
  regionchange:function(e) {
    //获取移动之后的位置
    var that = this;
    var etype = e.type;
    if(etype == 'end') {
      console.log(e.type);
      this.mapCtx.getCenterLocation({
        success:function(res) {
          that.setData({
            relog:res.longitude,
            relat:res.latitude
          }) 
        }
      })
    }
  },

  //控件被点击方法 当你点击控件触发事情时 会把控件对应的事件当作参数传入
  controltap :function(e) { 
    var that = this;
    var cid = e.controlId; 
    switch(cid) {
      case 1: {
        var status = myUtils.get("status");
        //点击扫码 根据用户状态跳转到对应页面
        if(status==0) {
          //跳转注册页面
          wx.navigateTo({
            url: '../register/register',
          })
        }else if(status==1) {  
          wx.navigateTo({
            url: '../deposite/deposite',
          })
        } else if(status == 2) {
          wx.navigateTo({
            url: '../identify/identify',
          })
        }
        break;
      }
      //定位按钮
      case 2: {
        this.mapCtx.moveToLocation()
        break;
      }
      //添加车辆
      case 5: {
        var bikes = that.data.markers;
        wx.request({
          url: 'http://localhost:8080/bike/add',
          data: { 
            location:[that.data.relog,that.data.relat],
            status:0,
            bikeno:1000
          },
          method: 'POST',
          success:function(res) {
            //查找单车 显示到页面
            findBikes(that.data.relog,that.data.relat,that)
          }
        })

        break;
      }
    }
  }
  
  
})


function findBikes(longitude,latitude,that) {
  var bikes;
  wx.request({
    url: 'http://localhost:8080/bike/findNear',
    method:'GET',
    data : {
      latitude :latitude,
      longitude : longitude
      
    },
    success : function(res) {
        bikes = res.data.map((geoResult)=>{
        return {
          iconPath:"/images/bike@white.png", 
          width:35,
          height:40,
          longitude:geoResult.content.location[0],
          latitude:geoResult.content.location[1],
          id : geoResult.content.id
        }
      })
      that.setData({ 
        markers : bikes
      })

    }
    
  })
    
}