/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  javax.inject.Named
 *  org.codehaus.plexus.util.StringUtils
 *  org.codehaus.plexus.util.xml.pull.MXParser
 *  org.codehaus.plexus.util.xml.pull.XmlPullParser
 *  org.codehaus.plexus.util.xml.pull.XmlPullParserException
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package org.sonatype.licensing.product.internal;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Named;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.xml.pull.MXParser;
import org.codehaus.plexus.util.xml.pull.XmlPullParser;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.licensing.AbstractLicenseKey;
import org.sonatype.licensing.LicenseKey;
import org.sonatype.licensing.feature.Feature;
import org.sonatype.licensing.feature.Features;
import org.sonatype.licensing.product.ProductLicenseKey;
import org.sonatype.licensing.product.SslKeyContainer;
import org.sonatype.licensing.product.internal.GenericSslKeyContainer;

@Named(value="licensing.default")
public class DefaultLicenseKey
extends AbstractLicenseKey
implements LicenseKey,
ProductLicenseKey {
    private final Logger fjz = LoggerFactory.getLogger(DefaultLicenseKey.class);
    private boolean ymv;
    private int bzn = -1;
    private final List<SslKeyContainer> fvf = new LinkedList<SslKeyContainer>();
    private final Set<String> zpu = new HashSet<String>();
    private Properties kxn = new Properties();

    @Inject
    public DefaultLicenseKey(Features features) {
        super(features);
    }

    @Override
    public boolean isFreeLicense() {
        return this.ymv;
    }

    private void setFreeLicense(boolean bl) {
        this.ymv = bl;
    }

    @Override
    public int getLicensedUsers() {
        return this.bzn;
    }

    private void setLicensedUsers(int n) {
        this.bzn = n;
    }

    @Override
    public void parseExtraContent(String string) {
        MXParser mXParser = new MXParser();
        try {
            mXParser.setInput((Reader)new StringReader(string));
            int n = mXParser.getEventType();
            boolean bl = false;
            boolean bl2 = false;
            boolean bl3 = false;
            boolean bl4 = false;
            boolean bl5 = false;
            boolean bl6 = false;
            boolean bl7 = false;
            boolean bl8 = false;
            boolean bl9 = false;
            String string2 = null;
            String string3 = null;
            SslKeyContainer.Type type = null;
            String string4 = null;
            String string5 = null;
            while (n != 1) {
                if (n == 2) {
                    String string6;
                    if (mXParser.getName().equals("nexusLicenseContent") || mXParser.getName().equals("licenseContent")) {
                        bl = true;
                    } else if (bl && mXParser.getName().equals("freeLicense")) {
                        this.setFreeLicense(Boolean.valueOf(StringUtils.trim((String)mXParser.nextText())));
                    } else if (bl && mXParser.getName().equals("licensedUsers")) {
                        this.setLicensedUsers(Integer.valueOf(StringUtils.trim((String)mXParser.nextText())));
                    } else if (bl && mXParser.getName().equals("evaluation")) {
                        this.setEvaluation(Boolean.valueOf(StringUtils.trim((String)mXParser.nextText())));
                    } else if (bl && mXParser.getName().equals("features")) {
                        bl2 = true;
                    } else if (bl2 && mXParser.getName().equals("feature")) {
                        bl3 = true;
                    } else if (bl3 && mXParser.getName().equals("id")) {
                        string6 = StringUtils.trim((String)mXParser.nextText());
                        Feature feature = this.getAvailableFeatures().get(string6);
                        this.getRawFeatures().add(string6);
                        if (feature != null) {
                            this.getFeatureSet().addFeature(feature);
                        } else {
                            this.fjz.debug("Invalid feature: {} found in license key, ignoring.", (Object)string6);
                        }
                    } else if (bl && mXParser.getName().equals("keys")) {
                        bl4 = true;
                    } else if (bl4 && mXParser.getName().equals("key")) {
                        bl5 = true;
                    } else if (bl5 && mXParser.getName().equals("entry")) {
                        string2 = mXParser.nextText();
                    } else if (bl5 && mXParser.getName().equals("password")) {
                        string3 = mXParser.nextText();
                    } else if (bl5 && mXParser.getName().equals("type")) {
                        string6 = mXParser.nextText();
                        try {
                            type = SslKeyContainer.Type.valueOf(string6);
                        }
                        catch (IllegalArgumentException illegalArgumentException) {
                            throw new XmlPullParserException("Unknown key type: " + string6, (XmlPullParser)mXParser, (Throwable)illegalArgumentException);
                        }
                    } else if (mXParser.getName().equals("products")) {
                        bl6 = true;
                    } else if (bl6 && mXParser.getName().equals("product")) {
                        bl7 = true;
                    } else if (bl7 && mXParser.getName().equals("id")) {
                        this.zpu.add(mXParser.nextText().toLowerCase(Locale.US));
                    } else if (mXParser.getName().equals("properties")) {
                        bl8 = true;
                    } else if (mXParser.getName().equals("property")) {
                        bl9 = true;
                    } else if (bl9 && mXParser.getName().equals("key")) {
                        string4 = StringUtils.trim((String)mXParser.nextText());
                    } else if (bl9 && mXParser.getName().equals("value")) {
                        string5 = StringUtils.trim((String)mXParser.nextText());
                    } else if (!bl) {
                        throw new XmlPullParserException("Illegal tag: '" + mXParser.getName() + "'", (XmlPullParser)mXParser, null);
                    }
                    if (string2 != null && string3 != null && type != null) {
                        this.itm(new GenericSslKeyContainer(string2, string3, type));
                        string2 = null;
                        string3 = null;
                        type = null;
                    } else if (string4 != null && string5 != null) {
                        this.getProperties().put(string4, string5);
                        string4 = null;
                        string5 = null;
                    }
                } else if (n == 3) {
                    if (mXParser.getName().equals("nexusLicenseContent") || mXParser.getName().equals("licenseContent")) {
                        bl = false;
                    } else if (bl && mXParser.getName().equals("features")) {
                        bl2 = false;
                    } else if (bl2 && mXParser.getName().equals("feature")) {
                        bl3 = false;
                    } else if (bl5 && mXParser.getName().equals("key")) {
                        bl5 = false;
                    } else if (bl4 && mXParser.getName().equals("keys")) {
                        bl4 = false;
                    } else if (bl6 && mXParser.getName().equals("products")) {
                        bl6 = false;
                    } else if (bl7 && mXParser.getName().equals("product")) {
                        bl7 = false;
                    } else if (bl8 && mXParser.getName().equals("properties")) {
                        bl8 = false;
                    } else if (bl9 && mXParser.getName().equals("property")) {
                        bl9 = false;
                    }
                }
                n = mXParser.next();
            }
        }
        catch (XmlPullParserException xmlPullParserException) {
            this.fjz.error("Unable to properly read license content", (Throwable)xmlPullParserException);
        }
        catch (IOException iOException) {
            this.fjz.error("Unable to properly read license content", (Throwable)iOException);
        }
    }

    private void itm(SslKeyContainer sslKeyContainer) {
        this.fvf.add(sslKeyContainer);
    }

    @Override
    public List<SslKeyContainer> getSslKeys() {
        return this.fvf;
    }

    public boolean isProductLicensed(String string) {
        return this.zpu.contains(string);
    }

    @Override
    public void setProperties(Properties properties) {
        this.kxn = properties;
    }

    @Override
    public Properties getProperties() {
        return this.kxn;
    }
}

