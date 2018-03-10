package com.kuckian.multicodec.test.codec;

import java.io.IOException;

import org.junit.Test;

import com.kuckian.multicodec.InvalidCodingSchemeException;
import com.kuckian.multicodec.InvalidOptionsException;

public abstract class AbstractRoundTripTest {

	public final String scheme;

	public AbstractRoundTripTest(String scheme) {
		this.scheme = scheme;
	}

	@Test
	public final void testStringRoundTrip() throws IOException, InvalidOptionsException, InvalidCodingSchemeException {
		TestUtils.testStringRoundTrip(scheme);
	}

	@Test
	public final void testRandomBytesRoundTrip()
			throws IOException, InvalidOptionsException, InvalidCodingSchemeException {
		TestUtils.testRandomBinaryRoundTrip(scheme);
	}

	@Test
	public final void testNonRandomBytesRoundTrip()
			throws IOException, InvalidOptionsException, InvalidCodingSchemeException {
		TestUtils.testNonRandomBinaryRoundTrip(scheme);
	}
}
