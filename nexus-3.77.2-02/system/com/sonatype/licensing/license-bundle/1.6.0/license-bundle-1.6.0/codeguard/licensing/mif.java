/*
 * Decompiled with CFR 0.152.
 */
package codeguard.licensing;

import de.schlichtherle.license.LicenseContent;
import java.beans.BeanDescriptor;
import java.beans.EventSetDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

public class mif
extends SimpleBeanInfo {
    private static BeanDescriptor qyf = null;
    private static final int qeu = 0;
    private static final int klp = 1;
    private static final int ysn = 2;
    private static final int zhj = 3;
    private static final int rnn = 4;
    private static final int bos = 5;
    private static final int zsv = 6;
    private static final int aec = 7;
    private static final int whz = 8;
    private static final int agq = 9;
    private static EventSetDescriptor[] haa = null;
    private static MethodDescriptor[] bjw = null;
    private static final int aua = -1;
    private static final int omz = -1;

    private static BeanDescriptor itm() {
        return qyf;
    }

    private static PropertyDescriptor[] omj() {
        PropertyDescriptor[] propertyDescriptorArray = new PropertyDescriptor[10];
        try {
            propertyDescriptorArray[0] = new PropertyDescriptor("consumerAmount", LicenseContent.class, "getConsumerAmount", "setConsumerAmount");
            propertyDescriptorArray[1] = new PropertyDescriptor("consumerType", LicenseContent.class, "getConsumerType", "setConsumerType");
            propertyDescriptorArray[2] = new PropertyDescriptor("extra", LicenseContent.class, "getExtra", "setExtra");
            propertyDescriptorArray[3] = new PropertyDescriptor("holder", LicenseContent.class, "getHolder", "setHolder");
            propertyDescriptorArray[4] = new PropertyDescriptor("info", LicenseContent.class, "getInfo", "setInfo");
            propertyDescriptorArray[5] = new PropertyDescriptor("issued", LicenseContent.class, "getIssued", "setIssued");
            propertyDescriptorArray[6] = new PropertyDescriptor("issuer", LicenseContent.class, "getIssuer", "setIssuer");
            propertyDescriptorArray[7] = new PropertyDescriptor("notAfter", LicenseContent.class, "getNotAfter", "setNotAfter");
            propertyDescriptorArray[8] = new PropertyDescriptor("notBefore", LicenseContent.class, "getNotBefore", "setNotBefore");
            propertyDescriptorArray[9] = new PropertyDescriptor("subject", LicenseContent.class, "getSubject", "setSubject");
        }
        catch (IntrospectionException introspectionException) {
            introspectionException.printStackTrace();
        }
        return propertyDescriptorArray;
    }

    private static EventSetDescriptor[] zxn() {
        return haa;
    }

    private static MethodDescriptor[] clk() {
        return bjw;
    }

    @Override
    public BeanDescriptor getBeanDescriptor() {
        return mif.itm();
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        return mif.omj();
    }

    @Override
    public EventSetDescriptor[] getEventSetDescriptors() {
        return mif.zxn();
    }

    @Override
    public MethodDescriptor[] getMethodDescriptors() {
        return mif.clk();
    }

    @Override
    public int getDefaultPropertyIndex() {
        return -1;
    }

    @Override
    public int getDefaultEventIndex() {
        return -1;
    }
}

