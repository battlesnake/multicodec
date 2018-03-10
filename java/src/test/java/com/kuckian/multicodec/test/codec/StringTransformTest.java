package com.kuckian.multicodec.test.codec;

import static org.junit.Assert.assertArrayEquals;

import java.io.IOException;

import org.junit.Test;

import com.kuckian.multicodec.InvalidCodingSchemeException;
import com.kuckian.multicodec.InvalidOptionsException;
import com.kuckian.multicodec.util.StringTransform;

public class StringTransformTest {

	private static final String scheme = "gzip(mode=size)+base64";

	@Test
	public final void testBasic() throws IOException, InvalidOptionsException, InvalidCodingSchemeException {
		byte[] rawInputData = TestUtils.TEST_STRING.getBytes("utf-8");
		String wireData = StringTransform.encode(scheme, rawInputData);
		byte[] rawDecodedData = StringTransform.decode(scheme, wireData);
		assertArrayEquals(rawInputData, rawDecodedData);
	}

	@Test
	public final void testTagged() throws IOException, InvalidOptionsException, InvalidCodingSchemeException {
		byte[] rawInputData = TestUtils.TEST_STRING.getBytes("utf-8");
		String wireData = StringTransform.encodeTagged(scheme, rawInputData);
		byte[] rawDecodedData = StringTransform.decodeTagged(wireData);
		assertArrayEquals(rawInputData, rawDecodedData);
	}

}
