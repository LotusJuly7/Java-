package com.hqk27;

public class HttpResponse {

    public HttpResponse() {
        
    }
    
    private int code;
    public void setStatusCode(int code) {
    	this.code = code;
    }
    
    public void setHeader(String key, String value) {
    	
    }

    private String data;
    public void setData(String data) {
        this.data = data;
    }

    /**
     * ��������
     * @param
     */
    public byte[] getBytes() {
        // ���շ��ص����ݣ� ��Ӧ��+��Ӧͷ+����+��Ӧ����
        return ("HTTP/1.1 200\r\n" +
                "Content-Type: text/plain\r\n" +
                "\r\n" + data).getBytes();
    }
}
