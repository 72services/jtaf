package ch.jtaf.test.util;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

public class TestHttpServletRequest implements HttpServletRequest {
    
    @Override
    public String getAuthType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public Cookie[] getCookies() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public long getDateHeader(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public String getHeader(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public Enumeration<String> getHeaders(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public Enumeration<String> getHeaderNames() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public int getIntHeader(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public String getMethod() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public String getPathInfo() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public String getPathTranslated() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public String getContextPath() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public String getQueryString() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public String getRemoteUser() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public boolean isUserInRole(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public Principal getUserPrincipal() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public String getRequestedSessionId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public String getRequestURI() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public StringBuffer getRequestURL() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public String getServletPath() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public HttpSession getSession(boolean bln) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public HttpSession getSession() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String changeSessionId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public boolean isRequestedSessionIdFromCookie() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public boolean isRequestedSessionIdFromURL() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public boolean isRequestedSessionIdFromUrl() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public boolean authenticate(HttpServletResponse hsr) throws IOException, ServletException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public void login(String string, String string1) throws ServletException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public void logout() throws ServletException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public Collection<Part> getParts() throws IOException, ServletException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public Part getPart(String string) throws IOException, ServletException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T extends HttpUpgradeHandler> T upgrade(Class<T> aClass) throws IOException, ServletException {
        return null;
    }

    @Override
    public Object getAttribute(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public Enumeration<String> getAttributeNames() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public String getCharacterEncoding() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public void setCharacterEncoding(String string) throws UnsupportedEncodingException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public int getContentLength() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getContentLengthLong() {
        return 0;
    }

    @Override
    public String getContentType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public ServletInputStream getInputStream() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public String getParameter(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public Enumeration<String> getParameterNames() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public String[] getParameterValues(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public Map<String, String[]> getParameterMap() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public String getProtocol() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public String getScheme() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public String getServerName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public int getServerPort() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public BufferedReader getReader() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public String getRemoteAddr() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public String getRemoteHost() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public void setAttribute(String string, Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public void removeAttribute(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public Locale getLocale() {
        return new Locale("de", "CH");
    }
    
    @Override
    public Enumeration<Locale> getLocales() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public boolean isSecure() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public RequestDispatcher getRequestDispatcher(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public String getRealPath(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public int getRemotePort() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public String getLocalName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public String getLocalAddr() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public int getLocalPort() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public ServletContext getServletContext() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public AsyncContext startAsync(ServletRequest sr, ServletResponse sr1) throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public boolean isAsyncStarted() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public boolean isAsyncSupported() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public AsyncContext getAsyncContext() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public DispatcherType getDispatcherType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
