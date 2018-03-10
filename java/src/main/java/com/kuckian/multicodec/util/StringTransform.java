package com.kuckian.multicodec.util;

import java.io.IOException;

import com.kuckian.multicodec.CodingScheme;
import com.kuckian.multicodec.Direction;
import com.kuckian.multicodec.InvalidCodingSchemeException;
import com.kuckian.multicodec.InvalidOptionsException;

/* TODO: Optimise out the excessive copyslicing */
public abstract class StringTransform {

	public static final char tagDelim = ':';
	public static final int MAX_TAGGED_SCHEME_LENGTH = 10000;

	public static String encode(String scheme, byte[] input)
			throws IOException, InvalidOptionsException, InvalidCodingSchemeException {
		return new String(MulticodecTransform.encodeBytes(CodingScheme.deserialize(scheme, Direction.ENCODE), input),
				"utf-8");
	}

	public static byte[] decode(String scheme, String input)
			throws IOException, InvalidOptionsException, InvalidCodingSchemeException {
		return MulticodecTransform.decodeBytes(CodingScheme.deserialize(scheme, Direction.DECODE),
				input.getBytes("utf-8"));
	}

	public static String encodeTagged(String scheme, byte[] input)
			throws IOException, InvalidOptionsException, InvalidCodingSchemeException {
		return scheme + tagDelim + new String(
				MulticodecTransform.encodeBytes(CodingScheme.deserialize(scheme, Direction.ENCODE), input), "utf-8");
	}

	public static byte[] decodeTagged(String input)
			throws IOException, InvalidOptionsException, InvalidCodingSchemeException {
		int delim = input.indexOf(tagDelim);
		if (delim == -1 || delim > MAX_TAGGED_SCHEME_LENGTH) {
			throw new InvalidCodingSchemeException("Failed to find tagged coding scheme in input data");
		}
		String scheme = input.substring(0, delim);
		byte[] data = input.substring(delim + 1).getBytes("utf-8");
		return MulticodecTransform.decodeBytes(CodingScheme.deserialize(scheme, Direction.DECODE), data);
	}
}
