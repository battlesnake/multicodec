package com.kuckian.multicodec.codec;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;

import com.kuckian.multicodec.Codec;

public class Base64Codec extends Codec {

	public Base64Codec() {
		super("base64");
	}

	@Override
	public Class<?> getDecoderOptionsClass() {
		return null;
	}

	@Override
	public Class<?> getEncoderOptionsClass() {
		return null;
	}

	@Override
	public InputStream createDecoderInputStream(Object options, InputStream source) {
		return new Base64InputStream(source);
	}

	@Override
	public OutputStream createEncoderOutputStream(Object options, OutputStream sink) {
		return new Base64OutputStream(sink);
	}

}
