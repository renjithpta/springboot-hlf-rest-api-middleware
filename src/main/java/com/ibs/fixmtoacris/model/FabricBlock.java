package com.ibs.fixmtoacris.model;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.protobuf.InvalidProtocolBufferException;
import com.ibs.fixmtoacris.utils.CommonUtil;

import org.hyperledger.fabric.protos.ledger.rwset.kvrwset.KvRwset;
import org.hyperledger.fabric.sdk.BlockInfo;
import org.hyperledger.fabric.sdk.TxReadWriteSetInfo;
import org.hyperledger.fabric.sdk.BlockInfo.EnvelopeType;

public class FabricBlock {
    private String channel;

	private int transactionCount;

	private String chaincode;

	/**
	 * Query context transaction id, @see {@link FabricHistory}
	 */
	private String currentTxId;

	/**
	 * Query context transaction timestamp, @see {@link FabricHistory}
	 */
	private String currentTxTimestamp;

	private String currentHash;
	private String previousHash;
    private long blockNumber;
    private String dataHash;


    List<BlockEnvelopeInfo> envelopeInfo =  new ArrayList<>();

    

	public String getChannel() {
        return channel;
    }





    public long getBlockNumber() {
        return blockNumber;
    }





    public void setBlockNumber(long blockNumber) {
        this.blockNumber = blockNumber;
    }





    public void setChannel(String channel) {
        this.channel = channel;
    }





    public int getTransactionCount() {
        return transactionCount;
    }





    public void setTransactionCount(int transactionCount) {
        this.transactionCount = transactionCount;
    }





    public String getChaincode() {
        return chaincode;
    }





    public void setChaincode(String chaincode) {
        this.chaincode = chaincode;
    }





    public String getCurrentTxId() {
        return currentTxId;
    }





    public void setCurrentTxId(String currentTxId) {
        this.currentTxId = currentTxId;
    }





    public String getCurrentTxTimestamp() {
        return currentTxTimestamp;
    }





    public void setCurrentTxTimestamp(String currentTxTimestamp) {
        this.currentTxTimestamp = currentTxTimestamp;
    }





    public List<BlockEnvelopeInfo> getEnvelopeInfo() {
        return envelopeInfo;
    }





    public void setEnvelopeInfo(List<BlockEnvelopeInfo> envelopeInfo) {
        this.envelopeInfo = envelopeInfo;
    }





    public String getCurrentHash() {
        return currentHash;
    }





    public void setCurrentHash(String currentHash) {
        this.currentHash = currentHash;
    }





    public String getPreviousHash() {
        return previousHash;
    }





    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }



    public String getDataHash() {
        return dataHash;
    }



    public void setDataHash(String dataHash) {
        this.dataHash = dataHash;
    }





    public static FabricBlock create(BlockInfo blockInfo) throws InvalidProtocolBufferException {
		if (blockInfo == null) {
			return null;
		}
		FabricBlock block = new FabricBlock();
		block.setBlockNumber(blockInfo.getBlockNumber());
		try {
			block.setChannel(blockInfo.getChannelId());
		} catch (InvalidProtocolBufferException e) {
//			e.printStackTrace();
		}

        block.setCurrentHash(new String(blockInfo.getBlock().getHeader().getDataHash().toByteArray(),StandardCharsets.UTF_8));
      
		block.setDataHash(CommonUtil.hashToString(blockInfo.getDataHash()));
		block.setTransactionCount(blockInfo.getTransactionCount());

		block.setPreviousHash(CommonUtil.hashToString(blockInfo.getPreviousHash()));
       
        for(BlockInfo.EnvelopeInfo envInfo:  blockInfo.getEnvelopeInfos()){
             int count = 0;                   
             Map<String, String> map = new HashMap<>();
             String txInfo = "";
            

         if(envInfo.getType() == EnvelopeType.TRANSACTION_ENVELOPE){
             
             String chainCode ="";
             
         
             for(BlockInfo.TransactionEnvelopeInfo.TransactionActionInfo actionInfo : ((BlockInfo.TransactionEnvelopeInfo)envInfo).getTransactionActionInfos()){
                            
                                chainCode = chainCode + "|"+actionInfo.getChaincodeIDName() + ":" + actionInfo.getChaincodeIDVersion() + "|" ;
                                count = actionInfo.getEndorsementsCount();
                               
                                for(int n=0; n < actionInfo.getEndorsementsCount(); n++){
                                    map.put(actionInfo.getEndorsementInfo(n).getMspid(),CommonUtil.hashToString(actionInfo.getEndorsementInfo(n).getSignature()));
                                    
                                }
                            
                                if(actionInfo.getTxReadWriteSet() != null){
                                    StringBuilder rwListStr = new StringBuilder();
                                   for(TxReadWriteSetInfo.NsRwsetInfo txRwInfo:actionInfo.getTxReadWriteSet().getNsRwsetInfos()){
                                   System.out.println("====inside=============================="+txRwInfo.getRwset().getWritesList().size());
                                    for(KvRwset.KVWrite list : txRwInfo.getRwset().getWritesList()){
                                        System.out.println("====inside22=============================="+new String(list.getValue().toByteArray(),StandardCharsets.UTF_8));
                                        txInfo =txInfo + (new String(list.getValue().toByteArray(),StandardCharsets.UTF_8));

                                    }
                                    //txInfo = rwListStr.toString();

                                   }
                                }

             }
            
             block.setChaincode(chainCode);
             block.getEnvelopeInfo().add(new BlockEnvelopeInfo(envInfo.getTransactionID(),envInfo.getCreator().getMspid(),
             envInfo.getTimestamp().toString(),envInfo.getCreator().getId(),count,map,txInfo));
         }
        }

		return block;
	}





  
}