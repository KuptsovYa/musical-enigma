/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.sonatype.nexus.formfields.FormField
 *  org.sonatype.nexus.security.privilege.PrivilegeDescriptor
 */
package org.sonatype.nexus.coreui.internal.privileges;

import java.util.List;
import java.util.Map;
import org.sonatype.nexus.formfields.FormField;
import org.sonatype.nexus.security.privilege.PrivilegeDescriptor;

public class PrivilegesTypesUIResponse {
    private final String id;
    private final String name;
    private final List<FormField> formFields;

    public PrivilegesTypesUIResponse(Map.Entry<String, PrivilegeDescriptor> entry) {
        PrivilegeDescriptor privilegeDescriptor = entry.getValue();
        this.id = privilegeDescriptor.getType();
        this.name = privilegeDescriptor.getName();
        this.formFields = privilegeDescriptor.getFormFields();
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public List<FormField> getFormFields() {
        return this.formFields;
    }
}

