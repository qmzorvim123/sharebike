package cn.edu.bike.bikedemo.service;

import cn.edu.bike.bikedemo.pojo.Bike;
import org.springframework.data.geo.GeoResult;

import java.util.List;

public interface BikeService {
    void save(Bike bike);

    List<GeoResult<Bike>> findNear(double longitude, double latitude);
}
