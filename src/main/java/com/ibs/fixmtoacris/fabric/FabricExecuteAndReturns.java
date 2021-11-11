package com.ibs.fixmtoacris.fabric;


import com.ibs.fixmtoacris.fabric.models.FabricOperationModel;

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Transaction;

public class FabricExecuteAndReturns implements FabircExecuteOperation{

    @Override
    public String execute(FabricOperationModel fabricOpModel, String... args) throws Exception {
        final Gateway gateway =  fabricOpModel.getGateway();
        final Network network = gateway.getNetwork(fabricOpModel.getChannelName());
		final Contract contract = network.getContract(fabricOpModel.getChaincodeName());
        final Transaction transction = contract.createTransaction(fabricOpModel.getFuncName());
       
        if(fabricOpModel.getTransientData()!= null && !fabricOpModel.getTransientData().isEmpty())   {

            transction.setTransient(fabricOpModel.getTransientData());	

        }   
        byte[] result = transction.submit() ;    
        gateway.close();
        return new String(result);
    }
    
}
