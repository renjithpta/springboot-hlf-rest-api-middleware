package com.ibs.fixmtoacris.model;


public class ExceptionResponse {

    public String exceptionMessage;
    public String requestedURI;


    public ExceptionResponse() {
      
    }
    
    public ExceptionResponse(String exceptionMessage, String requestedURI) {
        this.exceptionMessage = exceptionMessage;
        this.requestedURI = requestedURI;
    }
    public String getExceptionMessage() {
        return exceptionMessage;
    }
    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }
    public String getRequestedURI() {
        return requestedURI;
    }
    public void callerURL(String requestedURI) {
        this.requestedURI = requestedURI;
    }

    
    


}



