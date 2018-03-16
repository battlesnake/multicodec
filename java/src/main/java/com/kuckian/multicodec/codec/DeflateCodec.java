package com.kuckian.multicodec.codec;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import com.kuckian.multicodec.Codec;
import com.kuckian.multicodec.InvalidOptionsException;

public class DeflateCodec extends Codec {

	public enum Mode {
		SIZE, SPEED
	}

	public static class EncoderOptions {

		public Mode mode = Mode.SPEED;

	}

	public DeflateCodec() {
		super("deflate");
	}

	@Override
	public Class<?> getDecoderOptionsClass() {
		return null;
	}

	@Override
	public Class<?> getEncoderOptionsClass() {
		return EncoderOptions.class;
	}

	@Override
	public InputStream createDecoderInputStream(Object options, InputStream source) throws IOException {
		return new InflaterInputStream(source);
	}

	@Override
	public OutputStream createEncoderOutputStream(Object options, OutputStream sink)
			throws IOException, InvalidOptionsException {
		EncoderOptions opts = options != null ? (EncoderOptions) options : new EncoderOptions();
		int level;
		switch (opts.mode) {
		case SIZE:
			level = Deflater.BEST_COMPRESSION;
			break;
		case SPEED:
			level = Deflater.BEST_SPEED;
			break;
		default:
			throw new InvalidOptionsException("Invalid compression level");
		}
		return new DeflaterOutputStreamEx(sink, level);
	}

	private static class DeflaterOutputStreamEx extends DeflaterOutputStream {

		public DeflaterOutputStreamEx(OutputStream out, int level) throws IOException {
			super(out);
			def.setLevel(level);
		}

	}

}
