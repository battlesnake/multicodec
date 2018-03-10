package com.kuckian.multicodec.test.codec;

import static org.junit.Assert.assertArrayEquals;

import java.io.IOException;

import org.junit.Test;

import com.kuckian.multicodec.InvalidCodingSchemeException;
import com.kuckian.multicodec.InvalidOptionsException;
import com.kuckian.multicodec.util.StringTransform;

public class StringTransformTest {

	@Test
	public final void test() throws IOException, InvalidOptionsException, InvalidCodingSchemeException {
		byte[] rawInputData = TestUtils.TEST_STRING.getBytes("utf-8");
		String wireData = StringTransform.encode("gzip+base64", rawInputData);
		byte[] rawDecodedData = StringTransform.decode("gzip+base64", wireData);
		assertArrayEquals(rawInputData, rawDecodedData);
	}

}
