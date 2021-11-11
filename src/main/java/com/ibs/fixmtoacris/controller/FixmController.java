package com.ibs.fixmtoacris.controller;

import com.ibs.fixmtoacris.model.FabricResponse;
import com.ibs.fixmtoacris.model.FixmData;
import com.ibs.fixmtoacris.service.BlockchainOperations;

import org.apache.tomcat.util.json.JSONParser;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping(value="/api/v1/fixm")
@Tag(name = "FixmController", description = "Controller for fixm data operations")
public class FixmController {
    
    
    private static final Logger log = LoggerFactory.getLogger(FixmController.class);

    
    private @Autowired BlockchainOperations blockchainOperations  ;

    @PostMapping("/create")
    @SecurityRequirement(name="JWTBearer")
    public ResponseEntity<?> create(@RequestBody FixmData fixmData) throws Exception {
        log.info("FixmController::create controller.....start");
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("FixmController::create controller.....username -> %s",userName);
        String result = this.blockchainOperations.submitTransaction("createAcrisDataModel", userName, true, null,fixmData.getFixmXml());
        String acrisData = this.blockchainOperations.queryTransaction("readAcrisDataModel",userName ,result);
        log.info("FixmController::create controller.....end \n result -> %s", result); 
        return ResponseEntity.ok(new FabricResponse(201, "Data added to the ledger successfully!",  new JSONParser(acrisData).parse()));

    }

    @PostMapping("/update/{key}")
    @SecurityRequirement(name="JWTBearer")
    public ResponseEntity<?> update(@PathVariable(value = "key") String acrisKey, @RequestBody FixmData fixmData) throws Exception {
        log.info("FixmController::update controller.....start");
        String userName  = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("FixmController::update controller.....username -> %s",userName);
        this.blockchainOperations.submitTransaction("updateAcrisData", userName, false, null,acrisKey,fixmData.getFixmXml());
        String acrisData = this.blockchainOperations.queryTransaction("readAcrisDataModel",userName ,acrisKey); 
        log.info("FixmController::update controller.....end "); 
        return ResponseEntity.ok(new FabricResponse(201, "Data added to the ledger successfully!",  new JSONParser(acrisData).parse()));

    }

    @GetMapping("/{key}")
    @SecurityRequirement(name="JWTBearer")
    public ResponseEntity<?>  getAcrisKey(@PathVariable(value = "key") String acrisKey) throws Exception {
       
        log.info("FixmController::getAcrisKey controller.....start");
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("FixmController::update controller.....username -> %s",userName);
        String acrisData = this.blockchainOperations.queryTransaction("readAcrisDataModel",userName, acrisKey);
       
        return ResponseEntity.ok(new JSONParser(acrisData).parse());

 
    }


    @GetMapping("/history/{key}")
    @SecurityRequirement(name="JWTBearer")
    public ResponseEntity<?>  getHistory(@PathVariable(value = "key") String acrisKey)throws Exception {
        
        log.info("FixmController::getHistory controller.....start");
       
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        String acrisData = this.blockchainOperations.queryTransaction("getFlightHistory",userName,acrisKey);
        return ResponseEntity.ok(new JSONParser(acrisData).parse());

    }

    



}
