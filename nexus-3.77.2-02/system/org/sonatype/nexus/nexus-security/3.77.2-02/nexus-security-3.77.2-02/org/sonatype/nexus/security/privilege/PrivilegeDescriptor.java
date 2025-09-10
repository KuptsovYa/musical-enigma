/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.shiro.authz.Permission
 *  org.sonatype.nexus.formfields.FormField
 */
package org.sonatype.nexus.security.privilege;

import java.util.List;
import org.apache.shiro.authz.Permission;
import org.sonatype.nexus.formfields.FormField;
import org.sonatype.nexus.security.config.CPrivilege;
import org.sonatype.nexus.security.privilege.Privilege;
import org.sonatype.nexus.security.privilege.rest.ApiPrivilege;
import org.sonatype.nexus.security.privilege.rest.ApiPrivilegeRequest;

public interface PrivilegeDescriptor<T extends ApiPrivilege, Y extends ApiPrivilegeRequest> {
    public String getType();

    public String getName();

    public Permission createPermission(CPrivilege var1);

    public List<FormField> getFormFields();

    public T createApiPrivilegeImpl(Privilege var1);

    public void validate(Y var1);
}

