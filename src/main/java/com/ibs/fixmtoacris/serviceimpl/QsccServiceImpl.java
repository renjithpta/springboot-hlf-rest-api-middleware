package com.ibs.fixmtoacris.serviceimpl;

import com.ibs.fixmtoacris.fabric.FabricConstants;
import com.ibs.fixmtoacris.fabric.models.FabricOperationModel;
import com.ibs.fixmtoacris.service.QsccService;

import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.sdk.BlockInfo;
import org.hyperledger.fabric.sdk.Channel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service("QsccService")
public class QsccServiceImpl implements QsccService{

    @Value("${APP_CONNECTION_PROFILE_PATH:/home/ubuntu/userregistration_69/connection/connection-org1.json}")
    private String connectionFilePath;

    @Value("${CHANNEL_NAME:mychannel}")
    private String channelName;

    @Value("${CHAINCODE_NAME:fixmtoacris}")
    private String chaincodeName;



    @Override
    public String getTxIdDetails(String userName,String txId) throws Exception {

        return new FabricOperationModel(FabricConstants.CONTEXT_QUERY).withConnectionProfile(this.connectionFilePath)
        .withChainCode("qscc")
        .withChannelName(this.channelName)
        .withWalletPath("wallet")
        .withUserName(userName)
        .withFunctionName("GetTransactionByID")
        .withHasReturn(true)
        .withTransietData(null)
        .executeTransaction(channelName,txId);
       
       
    }

    @Override
    public String getBlockNumberDetaiis(String userName,String blockNumber) throws Exception {
        
       try{
        return new FabricOperationModel(FabricConstants.CONTEXT_QUERY).withConnectionProfile(this.connectionFilePath)
        .withChainCode("qscc")
        .withChannelName(this.channelName)
        .withWalletPath("wallet")
        .withUserName(userName)
        .withFunctionName("GetBlockByNumber")
        .withHasReturn(true)
        .withTransietData(null)
        .executeTransaction(channelName,blockNumber);
       }catch(Exception ex){
           ex.printStackTrace();
          
           throw new Exception(ex.getMessage());
       }
    }


    @Override
    public BlockInfo getBlockDetailsFromChannel(String userName,String blockNumber) throws Exception {

        FabricOperationModel fabricOpModel =  new FabricOperationModel(FabricConstants.CONTEXT_QUERY).withConnectionProfile(this.connectionFilePath)
        .withChainCode("qscc")
        .withChannelName(this.channelName)
        .withWalletPath("wallet")
        .withUserName(userName)
        .withFunctionName("GetBlockByNumber")
        .withHasReturn(true)
        .withTransietData(null);


        final Gateway gateway = fabricOpModel.getGateway();
        final Channel channel = gateway.getNetwork(fabricOpModel.getChannelName()).getChannel();
        BlockInfo blockInfo = channel.queryBlockByNumber(Long.parseLong(blockNumber));
        return blockInfo;
        

    }
    
}
