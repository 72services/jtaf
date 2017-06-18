FROM jboss/wildfly

USER root

ADD modules/system/layers/base/com/mysql /opt/jboss/wildfly/modules/system/layers/base/com/mysql
RUN chmod -R 744 /opt/jboss/wildfly/modules/system/layers/base/com/mysql
RUN chown -R jboss:root /opt/jboss/wildfly/modules/system/layers/base/com/mysql

ADD configuration/standalone.xml /opt/jboss/wildfly/standalone/configuration/standalone.xml
RUN chown jboss:root /opt/jboss/wildfly/standalone/configuration/standalone.xml

USER jboss

ADD jtaf.war /opt/jboss/wildfly/standalone/deployments/
