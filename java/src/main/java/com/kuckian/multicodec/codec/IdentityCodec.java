package com.kuckian.multicodec.codec;

import java.io.InputStream;
import java.io.OutputStream;

import com.kuckian.multicodec.Codec;

public class IdentityCodec extends Codec {

	public IdentityCodec() {
		super("identity");
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
		return source;
	}

	@Override
	public OutputStream createEncoderOutputStream(Object options, OutputStream sink) {
		return sink;
	}

}
