package ch.jtaf.test.util;

import javax.ejb.*;
import javax.transaction.UserTransaction;
import javax.xml.rpc.handler.MessageContext;
import java.security.Identity;
import java.security.Principal;
import java.util.Map;
import java.util.Properties;

public class UnallowedTestSessionContext implements SessionContext {

    private static final String NAME = "boeser@bube.ch";

    @Override
    public EJBLocalObject getEJBLocalObject() throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public EJBObject getEJBObject() throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MessageContext getMessageContext() throws IllegalStateException {
        return null;
    }

    @Override
    public <T> T getBusinessObject(Class<T> type) throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Class getInvokedBusinessInterface() throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean wasCancelCalled() throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public EJBHome getEJBHome() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public EJBLocalHome getEJBLocalHome() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Properties getEnvironment() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Identity getCallerIdentity() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Principal getCallerPrincipal() {
        return () -> NAME;
    }

    @Override
    public Map<String, Object> getContextData() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isCallerInRole(Identity idnt) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isCallerInRole(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public UserTransaction getUserTransaction() throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setRollbackOnly() throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean getRollbackOnly() throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TimerService getTimerService() throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object lookup(String string) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
