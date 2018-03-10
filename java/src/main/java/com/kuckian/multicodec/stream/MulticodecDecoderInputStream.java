package com.kuckian.multicodec.stream;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

import com.kuckian.multicodec.CodingLayer;
import com.kuckian.multicodec.CodingScheme;
import com.kuckian.multicodec.InvalidOptionsException;

public class MulticodecDecoderInputStream extends InputStream {

	private final Closeable[] streams;

	private final InputStream endpoint;

	public MulticodecDecoderInputStream(CodingScheme scheme, InputStream source)
			throws IOException, InvalidOptionsException {
		streams = new Closeable[scheme.size()];
		InputStream prev = source;
		for (int idx = 0, i = scheme.size() - 1; i >= 0; i--, idx++) {
			CodingLayer layer = scheme.get(i);
			InputStream now = layer.codec.createDecoderInputStream(layer.options, prev);
			streams[idx] = now;
			prev = now;
		}
		endpoint = prev;
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

	@Override
	public int read() throws IOException {
		return endpoint.read();
	}

	public int read(byte[] b) throws IOException {
		return endpoint.read(b);
	}

	public int read(byte[] b, int off, int len) throws IOException {
		return endpoint.read(b, off, len);
	}

}
