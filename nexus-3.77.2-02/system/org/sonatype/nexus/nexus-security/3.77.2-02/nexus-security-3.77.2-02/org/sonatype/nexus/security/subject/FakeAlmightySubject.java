/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  org.apache.shiro.authc.AuthenticationToken
 *  org.apache.shiro.authz.Permission
 *  org.apache.shiro.session.Session
 *  org.apache.shiro.subject.ExecutionException
 *  org.apache.shiro.subject.PrincipalCollection
 *  org.apache.shiro.subject.SimplePrincipalCollection
 *  org.apache.shiro.subject.Subject
 *  org.apache.shiro.subject.support.SubjectCallable
 *  org.apache.shiro.subject.support.SubjectRunnable
 */
package org.sonatype.nexus.security.subject;

import com.google.common.base.Preconditions;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.ExecutionException;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.SubjectCallable;
import org.apache.shiro.subject.support.SubjectRunnable;

public class FakeAlmightySubject
implements Subject {
    public static final String TASK_USERID = "*TASK";
    public static final Subject TASK_SUBJECT = FakeAlmightySubject.forUserId("*TASK");
    private final String fakeUserId;
    private final PrincipalCollection principalCollection;

    public static Subject forUserId(String fakeUserId) {
        return new FakeAlmightySubject(fakeUserId, FakeAlmightySubject.class.getName());
    }

    public static Subject forUserId(String fakeUserId, String realmName) {
        return new FakeAlmightySubject(fakeUserId, realmName);
    }

    private FakeAlmightySubject(String fakeUserId, String realmName) {
        this.fakeUserId = (String)Preconditions.checkNotNull((Object)fakeUserId);
        this.principalCollection = new SimplePrincipalCollection((Object)fakeUserId, realmName);
    }

    public Object getPrincipal() {
        return this.fakeUserId;
    }

    public PrincipalCollection getPrincipals() {
        return this.principalCollection;
    }

    public boolean isPermitted(String permission) {
        return true;
    }

    public boolean isPermitted(Permission permission) {
        return true;
    }

    public boolean[] isPermitted(String ... permissions) {
        return this.repeat(true, permissions.length);
    }

    public boolean[] isPermitted(List<Permission> permissions) {
        return this.repeat(true, permissions.size());
    }

    public boolean isPermittedAll(String ... permissions) {
        return true;
    }

    public boolean isPermittedAll(Collection<Permission> permissions) {
        return true;
    }

    public void checkPermission(String permission) {
    }

    public void checkPermission(Permission permission) {
    }

    public void checkPermissions(String ... permissions) {
    }

    public void checkPermissions(Collection<Permission> permissions) {
    }

    public boolean hasRole(String roleIdentifier) {
        return true;
    }

    public boolean[] hasRoles(List<String> roleIdentifiers) {
        return this.repeat(true, roleIdentifiers.size());
    }

    public boolean hasAllRoles(Collection<String> roleIdentifiers) {
        return true;
    }

    public void checkRole(String roleIdentifier) {
    }

    public void checkRoles(Collection<String> roleIdentifiers) {
    }

    public void checkRoles(String ... roleIdentifiers) {
    }

    public void login(AuthenticationToken token) {
    }

    public void logout() {
    }

    public boolean isAuthenticated() {
        return true;
    }

    public boolean isRemembered() {
        return false;
    }

    public Session getSession() {
        return null;
    }

    public Session getSession(boolean create) {
        return this.getSession();
    }

    public <V> V execute(Callable<V> callable) throws ExecutionException {
        try {
            return this.associateWith(callable).call();
        }
        catch (Throwable t) {
            throw new ExecutionException(t);
        }
    }

    public void execute(Runnable runnable) {
        this.associateWith(runnable).run();
    }

    public <V> Callable<V> associateWith(Callable<V> callable) {
        return new SubjectCallable((Subject)this, callable);
    }

    public Runnable associateWith(Runnable runnable) {
        return new SubjectRunnable((Subject)this, runnable);
    }

    public void runAs(PrincipalCollection principals) {
        throw new IllegalStateException("The " + this.getClass().getName() + " subject does not support runAs");
    }

    public boolean isRunAs() {
        return false;
    }

    public PrincipalCollection getPreviousPrincipals() {
        return null;
    }

    public PrincipalCollection releaseRunAs() {
        return null;
    }

    private boolean[] repeat(boolean val, int count) {
        Preconditions.checkArgument((count > -1 ? 1 : 0) != 0);
        boolean[] result = new boolean[count];
        Arrays.fill(result, val);
        return result;
    }
}

