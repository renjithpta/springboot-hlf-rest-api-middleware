package com.ibs.fixmtoacris.fabric;

import java.util.Map;
import com.ibs.fixmtoacris.fabric.FabricConstants;
import com.google.common.collect.ImmutableMap;

public class FabricOperationContext {

   private  static Map<String,FabircExecuteOperation> fabricStrategy = ImmutableMap.<String,FabircExecuteOperation>builder()
                                                                   .put(FabricConstants.CONTEXT_SUBMIT+FabricConstants.WITH_PARAMS+FabricConstants.WITH_RETURNS ,new FabricExecuteSubmitWithParamAndReturns())
                                                                   .put(FabricConstants.CONTEXT_SUBMIT+FabricConstants.WITH_PARAMS, new FabricExecuteWithParams())
                                                                   .put(FabricConstants.CONTEXT_SUBMIT+FabricConstants.WITH_RETURNS, new FabricExecuteAndReturns())
                                                                   .put(FabricConstants.CONTEXT_SUBMIT, new FabricExecute())
                                                                   .put(FabricConstants.CONTEXT_QUERY+FabricConstants.WITH_RETURNS, new FabicQueryAndReturns())
                                                                   .put(FabricConstants.CONTEXT_QUERY+FabricConstants.WITH_PARAMS+FabricConstants.WITH_RETURNS, new FabricQueryWithParamsAndReturns()).build();
    

    
    
    public static FabircExecuteOperation getFabricStrategy(String key) {
        return fabricStrategy.get(key);
    }
    
    
    
}
