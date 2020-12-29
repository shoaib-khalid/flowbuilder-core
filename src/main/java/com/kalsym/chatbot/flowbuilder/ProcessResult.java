/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kalsym.chatbot.flowbuilder;

/**
 *
 * @author user
 */
public class ProcessResult {
    //private ErrorCode error;
    //private String errorDescription;
   // private String resultCode;
    private Object data;
    private String failReason;
    private int status;
    private String message;
    private String trxId;
    /*public String getErrorCode() {
		return error.getErrorCode();
	}
    
    public String getErrorDescription() {
               return error.getErrorDescription();
       }
    
    public void setError(ErrorCode error) {
		this.error = error;
                this.errorCode = error.getErrorCode();
                this.errorDescription = error.getErrorDescription();
	}*/
 
     public Object getData() {
               return data;
       }
    
    public void setData(Object data) {
		this.data = data;
	}
    
    public String getFailReason() {
		return failReason;
	}
    
    public void setFailReason(String failReason) {
               this.failReason=failReason;
       }
    
   
    public int getStatus() {
		return status;
	}
    
    public void setStatus(int status) {
               this.status=status;
       }
    
   
    public String getMessage() {
		return message;
	}
    
    public void setMessage(String message) {
               this.message=message;
       }
    
   
    public String getTrxId() {
		return trxId;
	}
    
    public void setTrxId(String trxId) {
               this.trxId=trxId;
       }
    
    
    @Override
    public String toString() {
        return "status:"+status+" trxId:"+trxId+" "
                + "message:"+message+" failReason:"+failReason+" data:"+data;
    }
}
