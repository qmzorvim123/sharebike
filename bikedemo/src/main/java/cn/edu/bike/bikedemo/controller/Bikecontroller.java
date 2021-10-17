package cn.edu.bike.bikedemo.controller;

import cn.edu.bike.bikedemo.pojo.Bike;
import cn.edu.bike.bikedemo.service.BikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
public class Bikecontroller {

    @Autowired
    private BikeService bikeService;

    @RequestMapping("/bike/add")
    @ResponseBody
    public String add(@RequestBody Bike bike) {
        //调用service 保存到mongodb数据库
        bikeService.save(bike);
        return "success ";
    }

    @RequestMapping("/bike/findNear")
    @ResponseBody
    public List<GeoResult<Bike>> findNear(double longitude,double latitude) {
        //调用service 保存到mongodb数据库
        List<GeoResult<Bike>> bikes = bikeService.findNear(longitude,latitude);
        return bikes;
    }
}
