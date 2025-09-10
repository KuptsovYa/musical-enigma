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
@XmlType(name="authTicket", propOrder={"t"})
@XmlRootElement(name="authTicket")
public class AuthTicketXO {
    @XmlElement(required=true)
    @JsonProperty(value="t")
    private String t;

    public String getT() {
        return this.t;
    }

    public void setT(String value) {
        this.t = value;
    }

    public AuthTicketXO withT(String value) {
        this.setT(value);
        return this;
    }
}

