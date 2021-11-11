package com.ibs.fixmtoacris.model;

import java.util.Map;

import org.hyperledger.fabric.sdk.TxReadWriteSetInfo;

public class BlockEnvelopeInfo {
    private String txId;
    private String mspId;
    private String currentTxTimestamp;
    private String id;
    private int endorsementCount;
    Map<String,String> endorsementDetails;
    private String  txReadWrite;

    public BlockEnvelopeInfo() {
       
    }
    
    public BlockEnvelopeInfo(String txId, String mspId, String currentTxTimestamp,String id,int endorsementCount,Map<String,String> endorsementDetails,String txReadWrite) {
        this.txId = txId;
        this.mspId = mspId;
        this.currentTxTimestamp = currentTxTimestamp;
        this.id = id;
        this.endorsementCount = endorsementCount ;
        this.endorsementDetails = endorsementDetails;
        this.txReadWrite = txReadWrite;
    }


    
    
    public String getTxReadWrite() {
        return txReadWrite;
    }

    public void setTxReadWrite(String txReadWrite) {
        this.txReadWrite = txReadWrite;
    }

    public Map<String, String> getEndorsementDetails() {
        return endorsementDetails;
    }

    public void setEndorsementDetails(Map<String, String> endorsementDetails) {
        this.endorsementDetails = endorsementDetails;
    }

    public int getEndorsementCount() {
        return endorsementCount;
    }

    public void setEndorsementCount(int endorsementCount) {
        this.endorsementCount = endorsementCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTxId() {
        return txId;
    }
    public void setTxId(String txId) {
        this.txId = txId;
    }
    public String getMspId() {
        return mspId;
    }
    public void setMspId(String mspId) {
        this.mspId = mspId;
    }
    public String getCurrentTxTimestamp() {
        return currentTxTimestamp;
    }
    public void setCurrentTxTimestamp(String currentTxTimestamp) {
        this.currentTxTimestamp = currentTxTimestamp;
    }

   


    
}
