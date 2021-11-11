package com.ibs.fixmtoacris.serviceimpl;

import java.nio.file.Paths;
import java.security.PrivateKey;
import java.util.Properties;
import java.util.Set;

import com.ibs.fixmtoacris.exception.BadRequestException;
import com.ibs.fixmtoacris.model.UserRegistration;
import com.ibs.fixmtoacris.repository.UserRepository;
import com.ibs.fixmtoacris.service.UserRegistrationService;


import org.hyperledger.fabric.gateway.Identities;
import org.hyperledger.fabric.gateway.Identity;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;
import org.hyperledger.fabric.gateway.X509Identity;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric.sdk.security.CryptoSuiteFactory;
import org.hyperledger.fabric_ca.sdk.EnrollmentRequest;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {

    
    private static final Logger log = LoggerFactory.getLogger(UserRegistrationServiceImpl.class);

    @Autowired
    private UserRepository userRepository;


    @Value("${APP_ADMIN_USERNAME:admin}")
    private String app_admin;

    @Value("${APP_ADMIN_PASSWORD:adminpw}")
    private String app_password;


    @Autowired
    PasswordEncoder passwordEncoder;
    

    @Value("${CA_CERT_PATH:/home/ubuntu/userregistration_69/connection/peerOrganizations/org1.example.com/ca/ca.org1.example.com-cert.pem}")
    private String caCertificatePath;

    @Value("${CA_URL:https://localhost:7054}")
    private String ca_url;

    @Value("${HOST_NAME:localhost}")
    private String host_name;

    @Value("${CA_USER_NAME:admin}")
    private String ca_user_name;

    @Value("${CA_USER_PASSWORD:adminpw}")
    private String ca_user_password;

    @Value("${ORG_MSP:Org1MSP}")
    private String orgMSP;


    

    public UserRegistrationServiceImpl() {

       log.info("===== UserRegistrationServiceImpl::constructor...... start ");
       log.info("app_admin->%s ->\n apppassword->%s",app_admin ,app_password);
       log.info("Certificatepath->%s ->\n ca_url->%s",caCertificatePath ,ca_url);
       log.info("MSP->%s ->\n host_name->%s",orgMSP ,host_name);
       log.info("CAUSER Name->%s ->\n ca_user_password->%s",ca_user_name,ca_user_password);
       log.info("===== UserRegistrationServiceImpl::constructor...... end ");

    }

    public void enrollAdmin() throws Exception {

        log.info("UserRegistartionServiceImpl::enrollAdmin() ... start");
        Properties props = new Properties();
        props.put("pemFile", caCertificatePath);
        props.put("allowAllHostNames", "true");
        HFCAClient caClient = HFCAClient.createNewInstance(ca_url, props);
        CryptoSuite cryptoSuite = CryptoSuiteFactory.getDefault().getCryptoSuite();
        caClient.setCryptoSuite(cryptoSuite);

        // Create a wallet for managing identities
        Wallet wallet = Wallets.newFileSystemWallet(Paths.get("wallet"));


        // Check to see if we've already enrolled the admin user.
        if (wallet.get(this.app_admin) != null) {
            log.error("An identity for the admin user %s already exists in the wallet -> ", this.app_admin);
            return;
        }

        // Enroll the admin user, and import the new identity into the wallet.
        final EnrollmentRequest enrollmentRequestTLS = new EnrollmentRequest();
        enrollmentRequestTLS.addHost(this.host_name);
        enrollmentRequestTLS.setProfile("tls");
        Enrollment enrollment = caClient.enroll(this.app_admin, this.app_password, enrollmentRequestTLS);
        Identity user = Identities.newX509Identity(this.orgMSP, enrollment);
        wallet.put(this.app_admin, user);

        log.info("Successfully enrolled user \"admin\" and imported it into the wallet");

    }

    @Override
    public void enrollUser(UserRegistration userRegistration) throws Exception {
         
        log.info("UserRegistartionServiceImpl::enrollUser ... start \n args  username: %s", userRegistration.getUserName());

        String orgMsp = this.orgMSP;
        String appUser = userRegistration.getUserName();
        
        if(!this.checkUserNameAvailability(appUser)) throw new BadRequestException("User already exit!.");
        if(!this.checkEmailAvailability(userRegistration.getEmail())) throw new BadRequestException("User with same email already exts!.");
        Properties props = new Properties();
       
        props.put("pemFile", caCertificatePath);
        props.put("allowAllHostNames", "true");
       
        HFCAClient caClient = HFCAClient.createNewInstance(ca_url, props);
        CryptoSuite cryptoSuite = CryptoSuiteFactory.getDefault().getCryptoSuite();
        caClient.setCryptoSuite(cryptoSuite);

        // Create a wallet for managing identities
        Wallet wallet = Wallets.newFileSystemWallet(Paths.get("wallet"));

        // Check to see if we've already enrolled the user.
        if (wallet.get(appUser) != null) {
            log.error("An identity for the user " + appUser + " already exists in the wallet");
            throw new Exception("User already exits!");
        }

        X509Identity adminIdentity = (X509Identity) wallet.get(app_admin);
        if (adminIdentity == null) {
            log.error("\"admin\" needs to be enrolled and added to the wallet first");
            throw new Exception("Enroll admin first!");
        }
        User admin = new User() {

            @Override
            public String getName() {
                return app_admin;
            }

            @Override
            public Set<String> getRoles() {
                return null;
            }

            @Override
            public String getAccount() {
                return null;
            }

            @Override
            public String getAffiliation() {
                return "org1.department1";
            }

            @Override
            public Enrollment getEnrollment() {
                return new Enrollment() {

                    @Override
                    public PrivateKey getKey() {
                        return adminIdentity.getPrivateKey();
                    }

                    @Override
                    public String getCert() {
                        return Identities.toPemString(adminIdentity.getCertificate());
                    }
                };
            }

            @Override
            public String getMspId() {
                return orgMsp;
            }

        };

        // Register the user, enroll the user, and import the new identity into the
        // wallet.
        log.info("===New Enrollement Process start=====");
        RegistrationRequest registrationRequest = new RegistrationRequest(appUser);
        registrationRequest.setAffiliation("org1.department1");
        registrationRequest.setEnrollmentID(appUser);
        String enrollmentSecret = caClient.register(registrationRequest, admin);
        Enrollment enrollment = caClient.enroll(appUser, enrollmentSecret);
        Identity user = Identities.newX509Identity(orgMsp, enrollment);
        wallet.put(appUser, user);
        System.out.println("===User Enrollement process  end=====");
        com.ibs.fixmtoacris.bo.User userDetails = new com.ibs.fixmtoacris.bo.User();
        userDetails.setName(userRegistration.getName());
        userDetails.setUsername(userRegistration.getUserName());
        userDetails.setPassword(passwordEncoder.encode(userRegistration.getPassword()));
        
        userDetails.setEmail(userRegistration.getEmail());
        this.userRepository.save(userDetails);
        log.info("===database enrolled====");
        log.info("Successfully enrolled user {%} and imported it into the wallet",appUser);

    }


    public Boolean checkEmailAvailability(String email) {
        Boolean isAvailable = !userRepository.existsByEmail(email);
        return isAvailable;
    }


    public Boolean checkUserNameAvailability(String userName) {
        Boolean isAvailable = !userRepository.existsByEmail(userName);
        return isAvailable;
    }

}
