package com.kuckian.multicodec.stream;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

import com.kuckian.multicodec.CodingLayer;
import com.kuckian.multicodec.CodingScheme;
import com.kuckian.multicodec.InvalidOptionsException;

public class MulticodecEncoderOutputStream extends OutputStream {

	private final Closeable[] streams;

	private final OutputStream endpoint;

	public MulticodecEncoderOutputStream(CodingScheme scheme, OutputStream sink)
			throws IOException, InvalidOptionsException {
		streams = new Closeable[scheme.size()];
		OutputStream next = sink;
		for (int i = scheme.size() - 1; i >= 0; i--) {
			CodingLayer layer = scheme.get(i);
			OutputStream now = layer.codec.createEncoderOutputStream(layer.options, next);
			streams[i] = now;
			next = now;
		}
		endpoint = next;
	}

	@Override
	public void close() throws IOException {
		if (streams != null) {
			for (Closeable stream : streams) {
				if (stream != null) {
					stream.close();
				}
			}
		}
		super.close();
	}

	public void write(int b) throws IOException {
		endpoint.write(b);
	}

	public void write(byte[] b) throws IOException {
		endpoint.write(b);
	}

	public void write(byte[] b, int off, int len) throws IOException {
		endpoint.write(b, off, len);
	}

}
