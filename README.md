# Welcome
This is a personal site source based on Spring cloud. It contains multiple micro-service components, which provide functions such as permission verification, file reading and writing, video transcoding, video streaming, etc. The following briefly describes the role of each service.

## configserver ![travis_ci](https://www.travis-ci.org/ray0728/configserver.svg?branch=master)
Configuring distribution services Support for reading encrypted configuration files from github and decrypting them locally.  
[more](https://github.com/ray0728/configserver)

## discoveryserver ![travis_ci](https://www.travis-ci.org/ray0728/discoveryserver.svg?branch=master)
Service discovery and governance EUREKA-based service governance microservices.  
[more](https://github.com/ray0728/discoveryserver)

## authserver ![travis_ci](https://www.travis-ci.org/ray0728/authserver.svg?branch=master)
User Token Authentication Service Based on security-oauth2, the authorization code authentication method is provided. After the verification is passed, the JWT token encrypted by RSA can be returned.  
[more](https://github.com/ray0728/authserver)

## accountserver ![travis_ci](https://www.travis-ci.org/ray0728/accountserver.svg?branch=master)
Account data management service Support the addition, deletion and change of account data, and save the modified data in Mysql, and support Redis to do the second-level cache to speed up the query.    
[more](https://github.com/ray0728/accountserver)

## resourceserver ![travis_ci](https://www.travis-ci.org/ray0728/resourceserver.svg?branch=master)
Resource management service Manage all data submitted by users and use Mysql for management. At the same time, support Redis to do secondary cache to speed up query efficiency.    
[more](https://github.com/ray0728/resourceserver)

## messageserver ![travis_ci](https://www.travis-ci.org/ray0728/messageserver.svg?branch=master)
Message management service Support for posting multiple topics via Kafka.    
[more](https://github.com/ray0728/messageserver)

## streamserver ![travis_ci](https://www.travis-ci.org/ray0728/streamserver.svg?branch=master)
Streaming service Support for transcoding (HLS) video files and publishing transcoding results through message service.    
[more](https://github.com/ray0728/streamserver)

#gatewayserver ![travis_ci](https://www.travis-ci.org/ray0728/gatewayserver.svg?branch=master)
Gateway service, integrated UI (thymeleaf based) Provide all user interfaces    
[more](https://github.com/ray0728/gatewayserver)
