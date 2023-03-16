package com.hqk27;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    /**
     * 请求路径
     */
    private String pathInfo;

    /**
     * 请求方式
     */
    private String method;

    /**
     * 请求方式
     */
    private Map<String, String> header;

    public HttpRequest(Reader inReader) throws NullPointerException {
    	//System.out.println("===构造HttpRequest===");
        try {
            BufferedReader reader = new BufferedReader(inReader);
        	//System.out.println("===构造BufferedReader===");
            // 第一行：请求行
            String firstLine = reader.readLine();
        	//System.out.println("===readLine===");
            String[] firstLineItems = firstLine.split(" ");
            // 请求方式
            method = firstLineItems[0];
            // 请求路径
            pathInfo = firstLineItems[1];
            // 读取接下来的行：请求头
            String headerLine;
            header = new HashMap<>();
            while ((headerLine = reader.readLine()) != null) {
                if (headerLine.length() == 0) {
                    break;
                }
                String[] headerLineItems = headerLine.split(": ");
                header.put(headerLineItems[0], headerLineItems[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("错误");
        }
    }

    public String getPathInfo() {
        return pathInfo;
    }

    public String getMethod() {
        return method;
    }

    public String getHeader(String name) {
        return header.get(name);
    }
}
