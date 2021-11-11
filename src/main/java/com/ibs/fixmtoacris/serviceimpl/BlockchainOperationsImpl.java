package com.ibs.fixmtoacris.serviceimpl;

import java.util.Map;

import com.ibs.fixmtoacris.fabric.FabricConstants;
import com.ibs.fixmtoacris.fabric.models.FabricOperationModel;
import com.ibs.fixmtoacris.service.BlockchainOperations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service("BlockchainOperations")
public class BlockchainOperationsImpl implements BlockchainOperations {

    private static final Logger log = LoggerFactory.getLogger(BlockchainOperationsImpl.class);
    static {		
        System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");
	}

  
    @Value("${APP_CONNECTION_PROFILE_PATH:/home/ubuntu/userregistration_69/connection/connection-org1.json}")
    private String connectionFilePath;

    @Value("${CHANNEL_NAME:mychannel}")
    private String channelName;

    @Value("${CHAINCODE_NAME:fixmtoacris}")



    private String chaincodeName;



    
    public BlockchainOperationsImpl() {
        log.info("BlockchainOperationImpl::constructor...start");
        log.info("this.connectionFilePath -> {%s}::this.channelName -> %s..this.chaincodeName ->%s",this.connectionFilePath,this.channelName,this.chaincodeName);
       
    }

    @Retry(name = "re-instanceA")
    @RateLimiter(name = "rl-instanceA")
    @Override
    public String submitTransaction(String funcName, String userName, boolean hasReturns,Map<String, byte[]>  transientData,String... args) throws Exception{
   		
        log.info("BlockchainOperationImpl::submitTransaction....start");
        log.info("BlockchainOperationImpl::submitTransaction....function name -> { %s }, hasReturns ->{%b}, argslength ->{%s}",funcName,hasReturns,args);
          return new FabricOperationModel(FabricConstants.CONTEXT_SUBMIT).withConnectionProfile(this.connectionFilePath)
           .withChainCode(this.chaincodeName)
           .withChannelName(this.channelName)
           .withWalletPath("wallet")
           .withUserName(userName)
           .withFunctionName(funcName)
           .withHasReturn(hasReturns)
           .withTransietData(transientData)
           .executeTransaction(args);

    }

    @Override
    public String queryTransaction(String funcName, String userName, String... args) throws Exception{
        log.info("BlockchainOperationImpl::queryTransaction...start");
        log.info("BlockchainOperationImpl::queryTransaction....function name -> { %s }, argslength ->{%s}",funcName,args);

       return new FabricOperationModel(FabricConstants.CONTEXT_SUBMIT)
        .withConnectionProfile(this.connectionFilePath)
        .withChainCode(this.chaincodeName)
        .withChannelName(this.channelName)
        .withWalletPath("wallet")
        .withUserName(userName)
        .withFunctionName(funcName)
        .withHasReturn(true)
        .withTransietData(null)
        .executeTransaction(args);
       
    }
    
}
