/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.sonatype.nexus.common.text.Strings2
 */
package org.sonatype.nexus.security.user;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.sonatype.nexus.common.text.Strings2;
import org.sonatype.nexus.security.role.RoleIdentifier;
import org.sonatype.nexus.security.user.UserStatus;

public class User
implements Comparable<User> {
    private String userId;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String source;
    private UserStatus status;
    private boolean readOnly;
    private int version;
    private Set<RoleIdentifier> roleIdentifiers = new HashSet<RoleIdentifier>();

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        Object name;
        Object object = name = this.getFirstName() != null ? this.getFirstName() : "";
        if (!Strings2.isBlank((String)this.getLastName())) {
            name = (String)name + " " + this.getLastName();
        }
        return name;
    }

    public void setName(String name) {
        if (!Strings2.isBlank((String)name)) {
            String[] nameParts = name.trim().split(" ", 2);
            this.setFirstName(nameParts[0]);
            if (nameParts.length > 1) {
                this.setLastName(nameParts[1]);
            }
        }
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Set<RoleIdentifier> getRoles() {
        return Collections.unmodifiableSet(this.roleIdentifiers);
    }

    public void addRole(RoleIdentifier roleIdentifier) {
        this.roleIdentifiers.add(roleIdentifier);
    }

    public boolean removeRole(RoleIdentifier roleIdentifier) {
        return this.roleIdentifiers.remove(roleIdentifier);
    }

    public void addAllRoles(Set<RoleIdentifier> roleIdentifiers) {
        this.roleIdentifiers.addAll(roleIdentifiers);
    }

    public void setRoles(Set<RoleIdentifier> roles) {
        this.roleIdentifiers = roles;
    }

    public UserStatus getStatus() {
        return this.status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public boolean isReadOnly() {
        return this.readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public int compareTo(User o) {
        int before = -1;
        boolean equal = false;
        boolean after = true;
        if (this == o) {
            return 0;
        }
        if (o == null) {
            return 1;
        }
        if (this.getUserId() == null && o.getUserId() != null) {
            return -1;
        }
        if (this.getUserId() != null && o.getUserId() == null) {
            return 1;
        }
        int result = this.getUserId().compareTo(o.getUserId());
        if (result != 0) {
            return result;
        }
        if (this.getSource() == null) {
            return -1;
        }
        return this.getSource().compareTo(o.getSource());
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        User user = (User)o;
        if (this.source != null ? !this.source.equals(user.source) : user.source != null) {
            return false;
        }
        return !(this.userId != null ? !this.userId.equals(user.userId) : user.userId != null);
    }

    public int hashCode() {
        int result = this.userId != null ? this.userId.hashCode() : 0;
        result = 31 * result + (this.source != null ? this.source.hashCode() : 0);
        return result;
    }

    public String toString() {
        return this.getClass().getSimpleName() + "{userId='" + this.userId + "', firstName='" + this.firstName + "', lastName='" + this.lastName + "', source='" + this.source + "'}";
    }
}

