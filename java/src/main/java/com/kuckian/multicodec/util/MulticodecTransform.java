package com.kuckian.multicodec.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import com.kuckian.multicodec.CodingScheme;
import com.kuckian.multicodec.Direction;
import com.kuckian.multicodec.InvalidCodingSchemeException;
import com.kuckian.multicodec.InvalidOptionsException;
import com.kuckian.multicodec.stream.MulticodecDecoderInputStream;
import com.kuckian.multicodec.stream.MulticodecEncoderOutputStream;

public abstract class MulticodecTransform {

	public static byte[] encodeBytes(CodingScheme scheme, byte[] input) throws IOException, InvalidOptionsException {
		try (ByteArrayOutputStream sink = new ByteArrayOutputStream()) {
			try (MulticodecEncoderOutputStream stream = new MulticodecEncoderOutputStream(scheme, sink)) {
				stream.write(input);
			}
			return sink.toByteArray();
		}
	}

	public static byte[] decodeBytes(CodingScheme scheme, byte[] input) throws IOException, InvalidOptionsException {
		try (ByteArrayInputStream source = new ByteArrayInputStream(input)) {
			try (MulticodecDecoderInputStream stream = new MulticodecDecoderInputStream(scheme, source)) {
				return IOUtils.toByteArray(stream);
			}
		}
	}

	public static byte[] encodeBytes(String scheme, byte[] input)
			throws IOException, InvalidOptionsException, InvalidCodingSchemeException {
		return encodeBytes(CodingScheme.deserialize(scheme, Direction.ENCODE), input);
	}

	public static byte[] decodeBytes(String scheme, byte[] input)
			throws IOException, InvalidOptionsException, InvalidCodingSchemeException {
		return decodeBytes(CodingScheme.deserialize(scheme, Direction.DECODE), input);
	}

}
