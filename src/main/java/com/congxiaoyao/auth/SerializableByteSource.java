package com.congxiaoyao.auth;

import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.SimpleByteSource;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;

/**
 * Created by Jayce on 2016/11/10.
 */
public class SerializableByteSource implements Serializable, ByteSource {
    private transient SimpleByteSource byteSource;
    public SerializableByteSource() {

    }

    public SerializableByteSource(byte[] bytes) {
        byteSource = new SimpleByteSource(bytes);
    }

    public SerializableByteSource(char[] chars) {
        byteSource = new SimpleByteSource(chars);;
    }

    public SerializableByteSource(String string) {
        byteSource = new SimpleByteSource(string);
    }

    public SerializableByteSource(ByteSource source) {
        byteSource = new SimpleByteSource(source);
    }

    public SerializableByteSource(File file) {
        byteSource = new SimpleByteSource(file);
    }

    public SerializableByteSource(InputStream stream) {
        byteSource = new SimpleByteSource(stream);
    }


    @Override
    public byte[] getBytes() {
        return byteSource.getBytes();
    }

    @Override
    public String toHex() {
        return byteSource.toHex();
    }

    @Override
    public String toBase64() {
        return byteSource.toBase64();
    }

    @Override
    public boolean isEmpty() {
        return byteSource.isEmpty();
    }
}
