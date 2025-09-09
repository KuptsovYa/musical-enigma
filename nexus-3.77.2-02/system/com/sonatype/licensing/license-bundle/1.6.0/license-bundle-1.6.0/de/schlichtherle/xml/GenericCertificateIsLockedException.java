/*
 * Decompiled with CFR 0.152.
 */
package de.schlichtherle.xml;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;

public class GenericCertificateIsLockedException
extends PropertyVetoException {
    public GenericCertificateIsLockedException(PropertyChangeEvent propertyChangeEvent) {
        super(propertyChangeEvent.getPropertyName(), propertyChangeEvent);
    }
}

