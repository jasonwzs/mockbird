package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

@Entity
public class Mock extends Model {
    @Id
    public Long id;
    public String uri;
    public String method;
    public String requestbody;
    public String responsebody;
    
    public static Finder<Long, Mock> find = new Finder<Long, Mock> (
        Long.class, Mock.class
    );
}