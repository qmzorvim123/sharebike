package cn.edu.bike.bikedemo.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.redis.core.index.GeoIndexed;
import org.springframework.data.redis.core.index.Indexed;

//Bike类和mongodb的sharedbike表关联上
@Document(collection = "bikes")
public class Bike {

    //主键 唯一+索引 id->_id
    @Id
    private String id;
//    private double longitude;
//    private double latitude;
    private int status;
    //使用geo hash查找附近的单车

    @Field(value = "location")
    @GeoSpatialIndexed(type= GeoSpatialIndexType.GEO_2DSPHERE)
    private double[] location;//location是1个表示经纬度的数组

    public double[] getLocation() {
        return location;
    }

    public void setLocation(double[] location) {
        this.location = location;
    }

    //建立索引
//    @Indexed
    private Long bikeno;

    public String getId() {
        return id;
    }

    public Long getBikeno() {
        return bikeno;
    }

    public void setBikeno(Long bikeno) {
        this.bikeno = bikeno;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }



}
