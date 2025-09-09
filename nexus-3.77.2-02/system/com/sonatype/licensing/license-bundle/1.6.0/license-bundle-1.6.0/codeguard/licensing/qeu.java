/*
 * Decompiled with CFR 0.152.
 */
package codeguard.licensing;

import codeguard.licensing.klp;
import codeguard.licensing.ysn;
import codeguard.licensing.zhj;
import de.schlichtherle.xml.PersistenceServiceException;
import java.beans.Encoder;
import java.beans.ExceptionListener;
import java.beans.PersistenceDelegate;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.HashMap;
import java.util.Map;

public class qeu
implements zhj {
    private static final HashMap sxg = new HashMap();
    public static int quo = 10240;

    private static final ExceptionListener ysn() {
        return new klp();
    }

    public static final synchronized void itm(Class clazz, PersistenceDelegate persistenceDelegate) {
        sxg.put(clazz, persistenceDelegate);
    }

    protected static synchronized void itm(Encoder encoder) {
        for (Map.Entry entry : sxg.entrySet()) {
            encoder.setPersistenceDelegate((Class)entry.getKey(), (PersistenceDelegate)entry.getValue());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void itm(Object object, OutputStream outputStream) throws NullPointerException, PersistenceServiceException {
        block19: {
            if (outputStream == null) {
                throw new NullPointerException();
            }
            try {
                BufferedOutputStream bufferedOutputStream = null;
                XMLEncoder xMLEncoder = null;
                try {
                    bufferedOutputStream = new BufferedOutputStream(outputStream, quo);
                    xMLEncoder = new XMLEncoder(bufferedOutputStream);
                    qeu.itm(xMLEncoder);
                    xMLEncoder.setExceptionListener(qeu.ysn());
                    if (object != null) {
                        Object object2 = object;
                        synchronized (object2) {
                            xMLEncoder.writeObject(object);
                            break block19;
                        }
                    }
                    xMLEncoder.writeObject(object);
                }
                finally {
                    if (xMLEncoder != null) {
                        try {
                            xMLEncoder.close();
                        }
                        catch (Throwable throwable) {
                            ((OutputStream)bufferedOutputStream).close();
                            throw throwable;
                        }
                    } else if (bufferedOutputStream != null) {
                        ((OutputStream)bufferedOutputStream).close();
                    } else {
                        outputStream.close();
                    }
                }
            }
            catch (UndeclaredThrowableException undeclaredThrowableException) {
                throw new PersistenceServiceException(undeclaredThrowableException.getCause());
            }
            catch (Throwable throwable) {
                throw new PersistenceServiceException(throwable);
            }
        }
    }

    public static void itm(Object object, File file) throws NullPointerException, PersistenceServiceException {
        if (file == null) {
            throw new NullPointerException();
        }
        File file2 = null;
        boolean bl = false;
        try {
            file2 = qeu.zxn(file);
            bl = file.renameTo(file2);
            qeu.itm(object, new FileOutputStream(file));
            if (bl) {
                file2.delete();
            }
        }
        catch (Throwable throwable) {
            Throwable throwable2;
            if (bl) {
                try {
                    file.delete();
                }
                catch (Throwable throwable3) {
                    throwable2 = throwable3;
                }
                try {
                    file2.renameTo(file);
                }
                catch (Throwable throwable4) {
                    throwable2 = throwable4;
                }
            }
            throw throwable2 instanceof PersistenceServiceException ? (PersistenceServiceException)throwable2 : new PersistenceServiceException(throwable2);
        }
    }

    private static File zxn(File file) {
        File file2;
        String string = file.getPath();
        while ((file2 = new File(string = string + '~')).exists()) {
        }
        return file2;
    }

    public static byte[] zxn(Object object) throws PersistenceServiceException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            qeu.itm(object, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
        catch (PersistenceServiceException persistenceServiceException) {
            throw persistenceServiceException;
        }
        catch (Throwable throwable) {
            throw new PersistenceServiceException(throwable);
        }
    }

    public static String clk(Object object) throws PersistenceServiceException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            qeu.itm(object, byteArrayOutputStream);
            return byteArrayOutputStream.toString("UTF-8");
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            throw new AssertionError((Object)unsupportedEncodingException);
        }
        catch (PersistenceServiceException persistenceServiceException) {
            throw persistenceServiceException;
        }
        catch (Throwable throwable) {
            throw new PersistenceServiceException(throwable);
        }
    }

    public static Object itm(InputStream inputStream) throws NullPointerException, PersistenceServiceException {
        if (inputStream == null) {
            throw new NullPointerException();
        }
        XMLDecoder xMLDecoder = null;
        try {
            xMLDecoder = new XMLDecoder(new BufferedInputStream(inputStream, quo), null, qeu.ysn(), (ClassLoader)new ysn());
            Object object = xMLDecoder.readObject();
            return object;
        }
        catch (UndeclaredThrowableException undeclaredThrowableException) {
            throw new PersistenceServiceException(undeclaredThrowableException.getCause());
        }
        catch (Throwable throwable) {
            throw new PersistenceServiceException(throwable);
        }
        finally {
            if (xMLDecoder != null) {
                try {
                    xMLDecoder.close();
                }
                catch (Throwable throwable) {
                    throw new PersistenceServiceException(throwable);
                }
            }
        }
    }

    public static Object clk(File file) throws NullPointerException, PersistenceServiceException {
        if (file == null) {
            throw new NullPointerException();
        }
        try {
            return qeu.itm(new FileInputStream(file));
        }
        catch (PersistenceServiceException persistenceServiceException) {
            throw persistenceServiceException;
        }
        catch (Throwable throwable) {
            throw new PersistenceServiceException(throwable);
        }
    }

    public static Object clk(byte[] byArray) throws NullPointerException, PersistenceServiceException {
        if (byArray == null) {
            throw new NullPointerException();
        }
        try {
            return qeu.itm(new ByteArrayInputStream(byArray));
        }
        catch (PersistenceServiceException persistenceServiceException) {
            throw persistenceServiceException;
        }
        catch (Throwable throwable) {
            throw new PersistenceServiceException(throwable);
        }
    }

    public static Object zxn(String string) throws NullPointerException, PersistenceServiceException {
        if (string == null) {
            throw new NullPointerException();
        }
        try {
            return qeu.clk(string.getBytes("UTF-8"));
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            throw new AssertionError((Object)unsupportedEncodingException);
        }
        catch (PersistenceServiceException persistenceServiceException) {
            throw persistenceServiceException;
        }
        catch (Throwable throwable) {
            throw new PersistenceServiceException(throwable);
        }
    }

    protected qeu() {
    }
}

