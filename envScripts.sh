# Application environment variables setting
APP_SERVER_ADDRESS=localhost
APP_SERVER_PORT=8080
APP_BUILD_NO=1.2.4
APP_VERSION=1.2.4
# Database environment variables setting
export APP_DB_PORT=5400
export APP_DB_HOSTNAME=localhost
export DB_HOST=localhost
export POSTGRES_USER=hlf_user
export DB_NAME=hlf_databases
export POSTGRES_PASSWORD=hlf_password
export APP_DB_USERNAME=hlf_user
export APP_DB_PASSWORD=hlf_password
export APP_DB_NAME=hlf_databases
export APP_DB_POSTGRESIMAGEVERSION=9.6-alpine
# Spring boot environment variables setting
export SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3307/polling_app?useSSL=false&serverTimezone=UTC&useLegacyDatetimeCode=false
export SPRING_JPA_HIBERNATE_DDL_AUTO=validate
export SPRING_PROFILES_ACTIVE=dev
export SPRING_APPLICATION_NAME=hlf_rest_api
export APP_CONNECTION_PROFILE_PATH=/home/ubuntu/userregisteration_69/connection-org1.json
export CHANNEL_NAME=mychannel
export CHAINCODE_NAME=fixmtoacris
export CA_USER_PASSWORD=adminpw
export CA_USER_NAME=admin
export ORG_MSP=Org1MSP
export CA_URL=https://localhost:7054
export HOST_NAME=org1
export A_CERT_PATH=/home/ubuntu/userregisteration_69/peerOrganizations/org1.example.com/ca/ca.org1.example.com-cert.pem