package cn.edu.bike.bikedemo.service;

import cn.edu.bike.bikedemo.pojo.Bike;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BikeServiceImpl implements BikeService{

    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public void save(Bike bike) {
        mongoTemplate.insert(bike);//在bike类中添加了注解 注解保存了映射关系
    }

    //查找附近单车
    @Override
    public List<GeoResult<Bike>> findNear(double longitude, double latitude) {
        NearQuery nearQuery = NearQuery.near(longitude,latitude);
        //查找范围和距离单位
        nearQuery.maxDistance(0.5, Metrics.KILOMETERS);
        GeoResults<Bike> bikes = mongoTemplate.geoNear(
                nearQuery.query(new Query(Criteria.where("status").is(0)).limit(20)),
                Bike.class);

        return bikes.getContent();
    }
}
