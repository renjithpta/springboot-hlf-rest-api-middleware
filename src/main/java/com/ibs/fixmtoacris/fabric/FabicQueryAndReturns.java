package com.ibs.fixmtoacris.fabric;

import com.ibs.fixmtoacris.fabric.models.FabricOperationModel;

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;

public class FabicQueryAndReturns implements FabircExecuteOperation {

    @Override
    public String execute(FabricOperationModel fabricOpModel, String... args) throws Exception {

        final Gateway gateway = fabricOpModel.getGateway();
        final Network network = gateway.getNetwork(fabricOpModel.getChannelName());
        final Contract contract = network.getContract(fabricOpModel.getChaincodeName());

        byte[] result = contract.evaluateTransaction(fabricOpModel.getFuncName());

        gateway.close();
        return new String(result);
    }

}
