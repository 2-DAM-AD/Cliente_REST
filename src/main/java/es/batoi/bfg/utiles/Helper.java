package es.batoi.bfg.utiles;

import java.net.http.HttpResponse;

public class Helper {
    private HttpResponse<String> httpResponse;
    private boolean isList;

    public Helper(HttpResponse<String> httpResponse, boolean isList) {
        this.httpResponse = httpResponse;
        this.isList = isList;
    }

    public HttpResponse<String> getHttpResponse() {
        return httpResponse;
    }

    public void setHttpResponse(HttpResponse<String> httpResponse) {
        this.httpResponse = httpResponse;
    }

    public boolean isList() {
        return isList;
    }

    public void setList(boolean list) {
        isList = list;
    }
}
