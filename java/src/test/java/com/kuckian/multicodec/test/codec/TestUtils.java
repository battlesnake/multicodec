package com.kuckian.multicodec.test.codec;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Date;
import java.util.Random;

import com.kuckian.multicodec.InvalidCodingSchemeException;
import com.kuckian.multicodec.InvalidOptionsException;
import com.kuckian.multicodec.util.MulticodecTransform;

public abstract class TestUtils {

	public static final String TEST_STRING = "Hän katsoi maan reunalta tähteä putoavaa";
	public static final int TEST_RANDOM_BYTE_COUNT = 1 << 20;
	public static final int TEST_NON_RANDOM_BYTE_COUNT = 1 << 20;

	public static boolean showInfo = true;

	public static void printInfo(String scheme, String test, byte[] plain, byte[] coded) {
		if (showInfo) {
			System.err.println(String.format(" * %s / %s :: %d -> %d (%.1f)", scheme, test,
					plain.length, coded.length, coded.length * 100.0f / plain.length));
		}
	}

	public static void testStringRoundTrip(String scheme)
			throws IOException, InvalidOptionsException, InvalidCodingSchemeException {
		String inputString = TEST_STRING;
		byte[] inputBytes = inputString.getBytes("utf-8");
		byte[] encodedBytes = MulticodecTransform.encodeBytes(scheme, inputBytes);
		byte[] decodedBytes = MulticodecTransform.decodeBytes(scheme, encodedBytes);
		String decodedString = new String(decodedBytes, "utf-8");
		assertEquals(inputString, decodedString);
		printInfo(scheme, "text", inputBytes, encodedBytes);
	}

	public static void testRandomBinaryRoundTrip(String scheme)
			throws IOException, InvalidOptionsException, InvalidCodingSchemeException {
		byte[] inputBytes = new byte[TEST_RANDOM_BYTE_COUNT];
		new Random().nextBytes(inputBytes);
		byte[] encodedBytes = MulticodecTransform.encodeBytes(scheme, inputBytes);
		byte[] decodedBytes = MulticodecTransform.decodeBytes(scheme, encodedBytes);
		assertArrayEquals(inputBytes, decodedBytes);
		printInfo(scheme, "random", inputBytes, encodedBytes);
	}

	public static void testNonRandomBinaryRoundTrip(String scheme)
			throws IOException, InvalidOptionsException, InvalidCodingSchemeException {
		/* Repeat data string with a little bit of variation */
		byte[] nonRandom = (new Date().toString() + "\n").getBytes("utf-8");
		byte[] inputBytes = new byte[TEST_NON_RANDOM_BYTE_COUNT];
		for (int i = 0, j = 0; i < inputBytes.length; i++, j++) {
			if (j == nonRandom.length) {
				j = 0;
				inputBytes[i] = (byte) i;
			} else {
				inputBytes[i] = nonRandom[j];
			}
		}
		byte[] encodedBytes = MulticodecTransform.encodeBytes(scheme, inputBytes);
		byte[] decodedBytes = MulticodecTransform.decodeBytes(scheme, encodedBytes);
		assertArrayEquals(inputBytes, decodedBytes);
		printInfo(scheme, "nonrandom", inputBytes, encodedBytes);
	}
}
