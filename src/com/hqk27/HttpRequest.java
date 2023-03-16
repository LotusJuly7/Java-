package com.hqk27;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    /**
     * ����·��
     */
    private String pathInfo;

    /**
     * ����ʽ
     */
    private String method;

    /**
     * ����ʽ
     */
    private Map<String, String> header;

    public HttpRequest(Reader inReader) throws NullPointerException {
    	//System.out.println("===����HttpRequest===");
        try {
            BufferedReader reader = new BufferedReader(inReader);
        	//System.out.println("===����BufferedReader===");
            // ��һ�У�������
            String firstLine = reader.readLine();
        	//System.out.println("===readLine===");
            String[] firstLineItems = firstLine.split(" ");
            // ����ʽ
            method = firstLineItems[0];
            // ����·��
            pathInfo = firstLineItems[1];
            // ��ȡ���������У�����ͷ
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
            throw new RuntimeException("����");
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
