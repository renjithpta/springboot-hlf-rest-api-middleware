package com.ibs.fixmtoacris.fabric.models;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import com.ibs.fixmtoacris.fabric.FabricConstants;
import com.ibs.fixmtoacris.fabric.FabricOperationContext;

import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FabricOperationModel {

    private static final Logger log = LoggerFactory.getLogger("chain-logs");

    private String connectionFilePath;

    private String channelName;

    private String chaincodeName;

    private String walletPath;

    private String userName;

    private String funcName;

    private Map<String, byte[]> transientData;

    private boolean hasReturns;

    private String context;

    private boolean hasParams;

    public FabricOperationModel(String context) {

        this.context = context;

    }

    public FabricOperationModel withConnectionProfile(String profile) {

        this.connectionFilePath = profile;
        return this;
    }

    public FabricOperationModel withChannelName(String channel) {

        this.channelName = channel;
        return this;
    }

    public FabricOperationModel withChainCode(String chaincode) {

        this.chaincodeName = chaincode;
        return this;
    }

    public FabricOperationModel withWalletPath(String walletPath) {

        this.walletPath = walletPath;
        return this;
    }

    public FabricOperationModel withUserName(String userName) {

        this.userName = userName;
        return this;
    }

    public FabricOperationModel withFunctionName(String argFuncName) {
        this.funcName = argFuncName;
        return this;
    }

    public FabricOperationModel withHasReturn(boolean hasReturns) {

        this.hasReturns = hasReturns;
        return this;
    }

    public FabricOperationModel withTransietData(Map<String, byte[]> transientData) {

        this.transientData = transientData;
        return this;
    }

    public Gateway getGateway() throws Exception {

        final Path walletLoc = Paths.get(this.walletPath);
        final Wallet wallet = Wallets.newFileSystemWallet(walletLoc);
        // load a CCP
        final Path networkConfigPath = Paths.get(this.connectionFilePath);
        final Gateway.Builder builder = Gateway.createBuilder();
        builder.identity(wallet, userName).networkConfig(networkConfigPath).discovery(true);
        return builder.connect();
    }

    public String getConnectionFilePath() {
        return connectionFilePath;
    }

    public void setConnectionFilePath(String connectionFilePath) {
        this.connectionFilePath = connectionFilePath;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChaincodeName() {
        return chaincodeName;
    }

    public void setChaincodeName(String chaincodeName) {
        this.chaincodeName = chaincodeName;
    }

    public String getWalletPath() {
        return walletPath;
    }

    public void setWalletPath(String walletPath) {
        this.walletPath = walletPath;
    }

    public String getUserName() {
        return userName;
    }

    public String getFuncName() {
        return funcName;
    }

    public Map<String, byte[]> getTransientData() {
        return transientData;
    }

    public boolean isHasReturns() {
        return hasReturns;
    }

    public String getContext() {
        return context;
    }

    public boolean isHasParams() {
        return hasParams;
    }

    public void setHasParams(boolean hasParams) {
        this.hasParams = hasParams;
    }

    public String formKey() {
        log.debug("Blockchaincontext key builder ....start");
        StringBuilder key = new StringBuilder(this.context);
        if (this.hasParams)
            key.append(FabricConstants.WITH_PARAMS);
        if (this.hasReturns)
            key.append(FabricConstants.WITH_RETURNS);
        log.debug("Blockchaincontext key builder ....end with response -> {%s}",key);
        return key.toString();

    }

    public String executeTransaction(String... args) throws Exception {
        log.debug("FabricOperationModel::executeTransaction .......... start");
        if (args != null && args.length > 0)
            this.hasParams = true;
        
        return FabricOperationContext.getFabricStrategy(this.formKey()).execute(this, args);

    }

}