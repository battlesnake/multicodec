package com.kuckian.multicodec;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.kuckian.multicodec.stream.MulticodecDecoderInputStream;
import com.kuckian.multicodec.stream.MulticodecEncoderOutputStream;

public class CodingScheme implements Iterable<CodingLayer> {

	/* In order of how to apply for *encoding* */
	private final List<CodingLayer> layers;

	public CodingScheme(CodingLayer... layers) {
		this(Arrays.asList(layers));
	}

	public CodingScheme(Collection<CodingLayer> layers) {
		this.layers = new ArrayList<>(layers);
	}

	public int size() {
		return layers.size();
	}

	public CodingLayer get(int index) {
		return layers.get(index);
	}

	@Override
	public Iterator<CodingLayer> iterator() {
		return layers.iterator();
	}

	public List<CodingLayer> getLayers() {
		return layers;
	}

	public String serialize() {
		return OptionsSerialisation.serialiseScheme(this);
	}

	public static CodingScheme deserialize(String s, Direction direction) throws InvalidCodingSchemeException {
		return OptionsSerialisation.deserialiseScheme(s, direction);
	}

	@Override
	public String toString() {
		return serialize();
	}

	public InputStream createDecoder(InputStream source) throws IOException, InvalidOptionsException {
		return new MulticodecDecoderInputStream(this, source);
	}

	public OutputStream createEncoder(OutputStream sink) throws IOException, InvalidOptionsException {
		return new MulticodecEncoderOutputStream(this, sink);
	}

}
