package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import models.Mock;
import play.db.ebean.Transactional;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http.RawBuffer;
import play.mvc.Result;
import views.html.index;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render(request().path()));
    }
    
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public static Result upload() {
        if (request().body().isMaxSizeExceeded()) {
            return badRequest("Too much data");
        }
        JsonNode json = request().body().asJson();
        if (json == null) {
            return badRequest("Expecting Json data");
        }
        List<Map<String, String>> objs = Json.fromJson(json, List.class);
        if (objs == null) {
            return badRequest("Expecting Json array");
        }
        List<Mock> newMocks = new ArrayList<Mock>();
        for (Map<String, String> obj : objs) {
            Mock mock = new Mock();
            mock.uri = obj.get("uri");
            mock.method = obj.get("method");
            mock.requestbody = obj.get("requestbody");
            mock.responsebody = obj.get("responsebody");
            newMocks.add(mock);
        }
        List<Mock> oldMocks = Mock.find.all();
        
        Ebean.delete(oldMocks);
        Ebean.save(newMocks);
        
        int count = newMocks.size();
        int oldCount = oldMocks.size();
        ObjectNode result = Json.newObject();
        result.put("status", "ok");
        result.put("count", count);
        result.put("oldCount", oldCount);
        return ok(result);
    }
    
    public static Result download() {
        response().setContentType("application/x-download");  
        //response().setHeader("Content-disposition","attachment; filename=mockbird.json"); 
        List<Mock> mocks = Mock.find.all();
        JsonNode result = Json.toJson(mocks);
        return ok(result);
    }

    public static Mock findMock(String id) {
        return Mock.find.byId(Long.parseLong(id));
    }

    public static Mock findMock(String path, String method) {
        String uri = request().uri();
        int index = uri.indexOf("?");
        if (index == -1) {
            uri = path;
        } else {
            uri = path + uri.substring(index);
        }
        RawBuffer rbuffer = request().body().asRaw();
        String requestbody = "";
        if (rbuffer != null) {
            requestbody = new String(rbuffer.asBytes());
        }
        return Mock.find.where().eq("uri", uri).eq("method", method)
                .eq("requestbody", requestbody).findUnique();
    }

    @BodyParser.Of(BodyParser.Raw.class)
    public static Result callMock(String path, String method) {
        Mock mock = findMock(path, method);
        if (mock == null) {
            return notFound();
        }
        String result = mock.responsebody;
        return ok(result);
    }

    public static Result getMocks() {
        List<Mock> mocks = Mock.find.all();
        // truncate response text
        int truncateLength = 100;
        for (Mock mock : mocks) {
            if (mock.responsebody.length() > truncateLength) {
                mock.responsebody = mock.responsebody.substring(0, truncateLength);
            }
        }
        JsonNode result = Json.toJson(mocks);
        return ok(result);
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result addMock() {
        if (request().body().isMaxSizeExceeded()) {
            return badRequest("Too much data");
        }
        JsonNode json = request().body().asJson();
        if (json == null) {
            return badRequest("Expecting Json data");
        } else {
            String uri = json.findPath("uri").asText();
            String method = json.findPath("method").asText();
            String requestbody = json.findPath("requestbody").asText();
            String responsebody = json.findPath("responsebody").asText();

            Mock newMock = new Mock();
            newMock.uri = uri;
            newMock.method = method;
            newMock.requestbody = requestbody;
            newMock.responsebody = responsebody;
            Ebean.save(newMock);

            ObjectNode result = Json.newObject();
            result.put("id", newMock.id);
            return ok(result);
        }
    }

    public static Result getMock(String id) {
        Mock mock = findMock(id);
        if (mock == null) {
            return notFound();
        }
        JsonNode result = Json.toJson(mock);
        return ok(result);
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result updateMock(String id) {
        if (request().body().isMaxSizeExceeded()) {
            return badRequest("Too much data");
        }
        JsonNode json = request().body().asJson();
        if (json == null) {
            return badRequest("Expecting Json data");
        } else {
            Mock mock = findMock(id);
            if (mock == null) {
                return notFound();
            }
            String uri = json.findPath("uri").asText();
            String method = json.findPath("method").asText();
            String requestbody = json.findPath("requestbody").asText();
            String responsebody = json.findPath("responsebody").asText();
            mock.uri = uri;
            mock.method = method;
            mock.requestbody = requestbody;
            mock.responsebody = responsebody;
            Ebean.update(mock);
            return ok();
        }
    }

    public static Result deleteMock(String id) {
        Mock mock = findMock(id);
        if (mock == null) {
            return notFound();
        }
        Ebean.delete(mock);
        return ok();
    }

}
