package com.ibs.fixmtoacris.service;

import java.util.Map;

public interface BlockchainOperations {

String submitTransaction(String funcName, String userName, boolean hasReturns,Map<String, byte[]>  transientData,String... args) throws Exception;

String queryTransaction(String funcName, String userName, String... args) throws Exception;

    
}
