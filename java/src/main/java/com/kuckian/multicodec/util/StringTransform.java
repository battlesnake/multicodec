package com.kuckian.multicodec.util;

import java.io.IOException;

import com.kuckian.multicodec.CodingScheme;
import com.kuckian.multicodec.Direction;
import com.kuckian.multicodec.InvalidCodingSchemeException;
import com.kuckian.multicodec.InvalidOptionsException;

public abstract class StringTransform {

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

}
