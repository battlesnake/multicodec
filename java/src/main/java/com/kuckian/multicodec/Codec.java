package com.kuckian.multicodec;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class Codec {

	private final String name;

	protected Codec(String name) {
		this.name = name;
	}

	public final String getName() {
		return name;
	}

	public abstract Class<?> getDecoderOptionsClass();

	public abstract Class<?> getEncoderOptionsClass();

	public abstract InputStream createDecoderInputStream(Object options, InputStream source)
			throws IOException, InvalidOptionsException;

	public abstract OutputStream createEncoderOutputStream(Object options, OutputStream sink)
			throws IOException, InvalidOptionsException;

}
