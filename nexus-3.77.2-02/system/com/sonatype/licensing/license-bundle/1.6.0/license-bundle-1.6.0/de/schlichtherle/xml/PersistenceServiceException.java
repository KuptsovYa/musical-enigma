/*
 * Decompiled with CFR 0.152.
 */
package de.schlichtherle.xml;

public class PersistenceServiceException
extends Exception {
    public PersistenceServiceException(Throwable throwable) {
        super(throwable.getLocalizedMessage(), throwable);
    }
}

