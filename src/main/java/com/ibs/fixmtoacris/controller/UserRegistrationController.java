package com.ibs.fixmtoacris.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.ibs.fixmtoacris.model.FabricResponse;
import com.ibs.fixmtoacris.model.JwtAuthenticationResponse;
import com.ibs.fixmtoacris.model.LoginRequest;
import com.ibs.fixmtoacris.model.UserRegistration;
import com.ibs.fixmtoacris.security.JwtTokenProvider;
import com.ibs.fixmtoacris.service.UserRegistrationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value="/api/v1/userRegistration")
@Tag(name = "User Registartion and Authentication Services",
        description ="Enroll new user and issue authentication token")
public class UserRegistrationController {
    
    @Autowired
    private UserRegistrationService userRegistrationService;


    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Autowired
    AuthenticationManager authenticationManager;


    @Autowired
    JwtTokenProvider tokenProvider;

    @GetMapping("/endpoints")
    public ResponseEntity<List<String>> getEndpoints() {
        return new ResponseEntity<>(
                requestMappingHandlerMapping
                        .getHandlerMethods()
                        .keySet()
                        .stream()
                        .map(RequestMappingInfo::toString)
                        .collect(Collectors.toList()),
                HttpStatus.OK
        );
    }



    @Operation(summary = "Enroll a new client admin and create the wallet",description ="A new client admin is registred and enrolled .")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description  = "Successfully enrolled user"),
            @ApiResponse(responseCode = "401", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error during the enrollement."),
            @ApiResponse(responseCode = "404", description = "Error in user enrollment")
    }
    )

    @GetMapping("/enrollAdmin")
    @SecurityRequirement(name="basicauth")
    public ResponseEntity<?> enrollAdmin() throws Exception {

        try{

        this.userRegistrationService.enrollAdmin();
         return ResponseEntity.ok(new FabricResponse(201, "Admin enrolled successfully!",""));
        }catch(Exception e){

            throw new Exception("Error in admin enrollement!.Cause:"+e.getMessage());
        }

    }

    @Operation(summary = "Enroll a new user and create the wallet",description ="A new user is registred and enrolled .")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description  = "Successfully enrolled user"),
            @ApiResponse(responseCode = "401", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "500", description = "Error in user enrollement")
    }
    )

    @PostMapping("/enrollUser")
    @SecurityRequirement(name="basicauth")
    public ResponseEntity<?>  enrollNewUser(@RequestBody UserRegistration userRegistration) throws Exception {

     this.userRegistrationService.enrollUser(userRegistration);
     return ResponseEntity.ok(new FabricResponse(201, "User enrolled successfully!",""));
    }

    @PostMapping("/login")
    @Operation(summary = "Loging and get the JWT token",description ="Get the JWT token .")
    public ResponseEntity<?> login(@RequestBody LoginRequest  loginRequest) throws Exception {

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    loginRequest.getUserName().trim(),
                    loginRequest.getPassWord().trim()
            )
    );

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = tokenProvider.generateToken(authentication);
    return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    
    }

    
    



}
