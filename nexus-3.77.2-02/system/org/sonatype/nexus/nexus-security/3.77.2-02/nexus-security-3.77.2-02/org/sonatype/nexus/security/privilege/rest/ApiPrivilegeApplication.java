/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModelProperty
 *  javax.validation.constraints.NotBlank
 */
package org.sonatype.nexus.security.privilege.rest;

import io.swagger.annotations.ApiModelProperty;
import java.util.Collection;
import javax.validation.constraints.NotBlank;
import org.sonatype.nexus.security.privilege.Privilege;
import org.sonatype.nexus.security.privilege.rest.ApiPrivilegeWithActions;
import org.sonatype.nexus.security.privilege.rest.PrivilegeAction;

public class ApiPrivilegeApplication
extends ApiPrivilegeWithActions {
    public static final String DOMAIN_KEY = "domain";
    @NotBlank
    @ApiModelProperty(value="The domain (i.e. 'blobstores', 'capabilities' or even '*' for all) that this privilege is granting access to.  Note that creating new privileges with a domain is only necessary when using plugins that define their own domain(s).")
    private String domain;

    private ApiPrivilegeApplication() {
        super("application");
    }

    public ApiPrivilegeApplication(String name, String description, boolean readOnly, String domain, Collection<PrivilegeAction> actions) {
        super("application", name, description, readOnly, actions);
        this.domain = domain;
    }

    public ApiPrivilegeApplication(Privilege privilege) {
        super(privilege);
        this.domain = privilege.getPrivilegeProperty(DOMAIN_KEY);
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDomain() {
        return this.domain;
    }

    @Override
    protected Privilege doAsPrivilege(Privilege privilege) {
        super.doAsPrivilege(privilege);
        privilege.addProperty(DOMAIN_KEY, this.domain);
        return privilege;
    }

    @Override
    protected String doAsActionString() {
        return this.toCrudActionString();
    }
}

