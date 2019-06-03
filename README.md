This is a personal site source based on Spring cloud. It contains multiple micro-service components, which provide functions such as permission verification, file reading and writing, video transcoding, video streaming, etc. The following briefly describes the role of each service.

#config
Configuring distribution services
Support for reading encrypted configuration files from github and decrypting them locally.

#discovery
Service discovery and governance
EUREKA-based service governance microservices.

#auth
User Token Authentication Service
Based on security-oauth2, the authorization code authentication method is provided. After the verification is passed, the JWT token encrypted by RSA can be returned.

#account
Account data management service
Support the addition, deletion and change of account data, and save the modified data in Mysql, and support Redis to do the second-level cache to speed up the query.

#resource
Resource management service
Manage all data submitted by users and use Mysql for management. At the same time, support Redis to do secondary cache to speed up query efficiency.

#message
Message management service
Support for posting multiple topics via Kafka.

#stream
Streaming service
Support for transcoding (HLS) video files and publishing transcoding results through message service.

#gateway
Gateway service, integrated UI (thymeleaf based)
Provide all user interfaces