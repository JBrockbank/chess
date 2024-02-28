package server.responses;

import com.google.gson.Gson;

public class BaseResponse {

    public String toJSon() {
        var serializer = new Gson();
        return serializer.toJson(this);
    }
}
