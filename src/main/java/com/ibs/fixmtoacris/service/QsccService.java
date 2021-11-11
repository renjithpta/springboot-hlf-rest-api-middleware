package com.ibs.fixmtoacris.service;

import org.hyperledger.fabric.sdk.BlockInfo;

public interface QsccService {
    String getTxIdDetails(String userName,String txId)  throws Exception;
    String getBlockNumberDetaiis(String userName,String blockNumber) throws Exception;
    BlockInfo getBlockDetailsFromChannel(String userName,String blockNumber) throws Exception;


}
