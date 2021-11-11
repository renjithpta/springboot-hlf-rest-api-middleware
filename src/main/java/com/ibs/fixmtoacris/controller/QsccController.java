package com.ibs.fixmtoacris.controller;
import java.util.Base64;

import com.ibs.fixmtoacris.model.FabricBlock;
import com.ibs.fixmtoacris.service.QsccService;

import org.apache.tomcat.util.json.JSONParser;
import org.hyperledger.fabric.sdk.BlockInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping(value="/api/v1/qscc")
@Tag(name = "QsccController", description = "Controller for qscc functions operations")
public class QsccController {
    @Autowired
    private QsccService  qsccService;

    @GetMapping("/transaction/{txid}")
    @SecurityRequirement(name="JWTBearer")
    public ResponseEntity<?>  getTxDetails(@PathVariable(value = "txid") String txId) throws Exception {
       
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
       
        String txData = this.qsccService.getTxIdDetails(userName, txId);
       
        return ResponseEntity.ok(txData);

 
    }

    @GetMapping("/blockdetails/{blockid}")
    @SecurityRequirement(name="JWTBearer")
    public ResponseEntity<?>  getBlockDetails(@PathVariable(value = "blockid") String blockId) throws Exception {
       
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("======Blockid=========================="+blockId);
        //String txData = this.qsccService.getBlockNumberDetaiis(userName,blockId);
        BlockInfo  blockInfo = this.qsccService.getBlockDetailsFromChannel(userName, blockId);
        
 
        return ResponseEntity.ok(FabricBlock.create(blockInfo));

 
    }

    
}
