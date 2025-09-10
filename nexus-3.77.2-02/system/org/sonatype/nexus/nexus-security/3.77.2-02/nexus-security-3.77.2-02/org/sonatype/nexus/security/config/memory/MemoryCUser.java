/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.sonatype.nexus.common.text.Strings2
 */
package org.sonatype.nexus.security.config.memory;

import org.sonatype.nexus.common.text.Strings2;
import org.sonatype.nexus.security.config.CUser;

public class MemoryCUser
implements CUser {
    private String email;
    private String firstName;
    private String id;
    private String lastName;
    private String password;
    private String status;
    private int version;

    @Override
    public String getEmail() {
        return this.email;
    }

    @Override
    public String getFirstName() {
        return this.firstName;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getLastName() {
        return this.lastName;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getStatus() {
        return this.status;
    }

    @Override
    public int getVersion() {
        return this.version;
    }

    @Override
    public boolean isActive() {
        return "active".equals(this.status) || "changepassword".equals(this.status);
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public void setVersion(int version) {
        this.version = version;
    }

    public MemoryCUser withEmail(String email) {
        this.setEmail(email);
        return this;
    }

    public MemoryCUser withFirstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public MemoryCUser withId(String id) {
        this.setId(id);
        return this;
    }

    public MemoryCUser withLastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public MemoryCUser withPassword(String password) {
        this.setPassword(password);
        return this;
    }

    public MemoryCUser withStatus(String status) {
        this.setStatus(status);
        return this;
    }

    public MemoryCUser withVersion(int version) {
        this.setVersion(version);
        return this;
    }

    @Override
    public MemoryCUser clone() {
        try {
            return (MemoryCUser)super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public String toString() {
        return this.getClass().getSimpleName() + "{id='" + this.id + "', firstName='" + this.firstName + "', lastName='" + this.lastName + "', password='" + Strings2.mask((String)this.password) + "', status='" + this.status + "', email='" + this.email + "', version='" + this.version + "'}";
    }
}

