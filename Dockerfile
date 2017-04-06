FROM tomcat:8.5.13-jre8

COPY ./build/libs/acme-invoice-0.1.0.war /usr/local/tomcat/webapps/acme-invoice-0.1.0.war