/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.codehaus.plexus.util.xml.pull.MXSerializer
 *  org.codehaus.plexus.util.xml.pull.XmlSerializer
 */
package codeguard.licensing;

import codeguard.licensing.agq;
import codeguard.licensing.aua;
import codeguard.licensing.bjw;
import codeguard.licensing.haa;
import codeguard.licensing.whz;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import org.codehaus.plexus.util.xml.pull.MXSerializer;
import org.codehaus.plexus.util.xml.pull.XmlSerializer;

public class omz {
    private static final String uca = null;

    public void itm(Writer writer, agq agq2) throws IOException {
        MXSerializer mXSerializer = new MXSerializer();
        mXSerializer.setProperty("http://xmlpull.org/v1/doc/properties.html#serializer-indentation", (Object)"  ");
        mXSerializer.setProperty("http://xmlpull.org/v1/doc/properties.html#serializer-line-separator", (Object)"\n");
        mXSerializer.setOutput(writer);
        mXSerializer.startDocument(agq2.rnn(), null);
        this.itm(agq2, "nexusLicenseContent", (XmlSerializer)mXSerializer);
        mXSerializer.endDocument();
    }

    public void itm(OutputStream outputStream, agq agq2) throws IOException {
        MXSerializer mXSerializer = new MXSerializer();
        mXSerializer.setProperty("http://xmlpull.org/v1/doc/properties.html#serializer-indentation", (Object)"  ");
        mXSerializer.setProperty("http://xmlpull.org/v1/doc/properties.html#serializer-line-separator", (Object)"\n");
        mXSerializer.setOutput(outputStream, agq2.rnn());
        mXSerializer.startDocument(agq2.rnn(), null);
        this.itm(agq2, "nexusLicenseContent", (XmlSerializer)mXSerializer);
        mXSerializer.endDocument();
    }

    private void itm(whz whz2, String string, XmlSerializer xmlSerializer) throws IOException {
        xmlSerializer.startTag(uca, string);
        if (whz2.getId() != null) {
            xmlSerializer.startTag(uca, "id").text(whz2.getId()).endTag(uca, "id");
        }
        xmlSerializer.endTag(uca, string);
    }

    private void itm(agq agq2, String string, XmlSerializer xmlSerializer) throws IOException {
        xmlSerializer.startTag(uca, string);
        if (agq2.aec() != null) {
            xmlSerializer.startTag(uca, "version").text(agq2.aec()).endTag(uca, "version");
        }
        if (agq2.isFreeLicense()) {
            xmlSerializer.startTag(uca, "freeLicense").text(String.valueOf(agq2.isFreeLicense())).endTag(uca, "freeLicense");
        }
        if (agq2.getLicensedUsers() != -1) {
            xmlSerializer.startTag(uca, "licensedUsers").text(String.valueOf(agq2.getLicensedUsers())).endTag(uca, "licensedUsers");
        }
        if (agq2.isEvaluation()) {
            xmlSerializer.startTag(uca, "evaluation").text(String.valueOf(agq2.isEvaluation())).endTag(uca, "evaluation");
        }
        if (agq2.bos() != null && agq2.bos().size() > 0) {
            xmlSerializer.startTag(uca, "products");
            for (bjw serializable : agq2.bos()) {
                this.itm(serializable, "product", xmlSerializer);
            }
            xmlSerializer.endTag(uca, "products");
        }
        if (agq2.zhj() != null && agq2.zhj().size() > 0) {
            xmlSerializer.startTag(uca, "features");
            for (whz whz2 : agq2.zhj()) {
                this.itm(whz2, "feature", xmlSerializer);
            }
            xmlSerializer.endTag(uca, "features");
        }
        if (agq2.getKeys() != null && agq2.getKeys().size() > 0) {
            xmlSerializer.startTag(uca, "keys");
            for (haa haa2 : agq2.getKeys()) {
                this.itm(haa2, "key", xmlSerializer);
            }
            xmlSerializer.endTag(uca, "keys");
        }
        if (agq2.zsv() != null && agq2.zsv().size() > 0) {
            xmlSerializer.startTag(uca, "properties");
            for (aua aua2 : agq2.zsv()) {
                this.itm(aua2, "property", xmlSerializer);
            }
            xmlSerializer.endTag(uca, "properties");
        }
        xmlSerializer.endTag(uca, string);
    }

    private void itm(haa haa2, String string, XmlSerializer xmlSerializer) throws IOException {
        xmlSerializer.startTag(uca, string);
        if (haa2.getPassword() != null) {
            xmlSerializer.startTag(uca, "password").text(haa2.getPassword()).endTag(uca, "password");
        }
        if (haa2.getEntry() != null) {
            xmlSerializer.startTag(uca, "entry").text(haa2.getEntry()).endTag(uca, "entry");
        }
        if (haa2.whz() != null) {
            xmlSerializer.startTag(uca, "type").text(haa2.whz()).endTag(uca, "type");
        }
        xmlSerializer.endTag(uca, string);
    }

    private void itm(bjw bjw2, String string, XmlSerializer xmlSerializer) throws IOException {
        xmlSerializer.startTag(uca, string);
        if (bjw2.getId() != null) {
            xmlSerializer.startTag(uca, "id").text(bjw2.getId()).endTag(uca, "id");
        }
        xmlSerializer.endTag(uca, string);
    }

    private void itm(aua aua2, String string, XmlSerializer xmlSerializer) throws IOException {
        xmlSerializer.startTag(uca, string);
        if (aua2.agq() != null) {
            xmlSerializer.startTag(uca, "key").text(aua2.agq()).endTag(uca, "key");
        }
        if (aua2.haa() != null) {
            xmlSerializer.startTag(uca, "value").text(aua2.haa()).endTag(uca, "value");
        }
        xmlSerializer.endTag(uca, string);
    }
}

