/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  javax.xml.bind.annotation.XmlAccessType
 *  javax.xml.bind.annotation.XmlAccessorType
 *  javax.xml.bind.annotation.XmlElement
 *  javax.xml.bind.annotation.XmlRootElement
 *  javax.xml.bind.annotation.XmlType
 */
package org.sonatype.nexus.coreui.internal.wonderland;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(value=XmlAccessType.FIELD)
@XmlType(name="authToken", propOrder={"u", "p"})
@XmlRootElement(name="authToken")
public class AuthTokenXO {
    @XmlElement(required=true)
    @JsonProperty(value="u")
    private String u;
    @XmlElement(required=true)
    @JsonProperty(value="p")
    private String p;

    public String getU() {
        return this.u;
    }

    public void setU(String value) {
        this.u = value;
    }

    public String getP() {
        return this.p;
    }

    public void setP(String value) {
        this.p = value;
    }

    public AuthTokenXO withU(String value) {
        this.setU(value);
        return this;
    }

    public AuthTokenXO withP(String value) {
        this.setP(value);
        return this;
    }
}

