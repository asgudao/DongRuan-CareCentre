package com.neuedu.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HttpNeueduRequest extends HttpServletRequestWrapper {
    Map<String, String[]> parameterMap;
    public HttpNeueduRequest(HttpServletRequest request) {
        super(request);
        parameterMap = new HashMap<>();
        Map<String, String[]> requestMap = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : requestMap.entrySet()) {
            parameterMap.put(entry.getKey(), entry.getValue());
        }
    }
    public void addParams(String key, String value) {
        if (!parameterMap.containsKey(key)) {
            parameterMap.put(key, new String[]{value});
        } else {
            String[] values = parameterMap.get(key);
            values = Arrays.copyOf(values, values.length + 1);
            values[values.length - 1] = value;
        }
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return parameterMap;
    }

    @Override
    public String getParameter(String name) {
        if (parameterMap.containsKey(name)) {
            return parameterMap.get(name)[0];
        }
        return null;
    }

    @Override
    public String[] getParameterValues(String name) {
        if (parameterMap.containsKey(name)) {
            return parameterMap.get(name);
        }
        return null;
    }
}
