package com.ibs.fixmtoacris.service;

import com.ibs.fixmtoacris.model.UserRegistration;

public interface UserRegistrationService {
   
    void enrollAdmin() throws Exception;
    void enrollUser(UserRegistration userRegistration) throws Exception;
    
}
