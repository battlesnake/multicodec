package com.kuckian.multicodec;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.kuckian.multicodec.codec.Base64Codec;
import com.kuckian.multicodec.codec.DeflateCodec;
import com.kuckian.multicodec.codec.GzipCodec;
import com.kuckian.multicodec.codec.IdentityCodec;

public class CodecRegistry {

	private static final Map<String, Codec> codecs = new HashMap<>();

	public static void register(Codec codec) {
		if (codecs.putIfAbsent(codec.getName(), codec) != null) {
			throw new CodecNameConflictError(codec.getName());
		}
	}

	public static Codec getCodec(String name) {
		Codec codec = codecs.get(name);
		if (codec == null) {
			throw new CodecNotRegisteredError(name);
		}
		return codec;
	}

	public static InputStream createDecoder(String name, Object options, InputStream source)
			throws IOException, InvalidOptionsException {
		return getCodec(name).createDecoderInputStream(options, source);
	}

	public static OutputStream createEncoder(String name, Object options, OutputStream sink)
			throws IOException, InvalidOptionsException {
		return getCodec(name).createEncoderOutputStream(options, sink);
	}

	static {
		register(new IdentityCodec());
		register(new Base64Codec());
		register(new DeflateCodec());
		register(new GzipCodec());
	}

}
