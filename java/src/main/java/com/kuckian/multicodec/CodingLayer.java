package com.kuckian.multicodec;

public class CodingLayer {

	public Codec codec;

	public Object options;

	public CodingLayer() {

	}

	public CodingLayer(Codec codec, Object options) {
		this.codec = codec;
		this.options = options;
	}

	public String serialise() {
		return OptionsSerialisation.serialiseLayer(this);
	}

	public static CodingLayer deserialise(String s, Direction direction) throws InvalidCodingSchemeException {
		return OptionsSerialisation.deserialiseLayer(s, direction);
	}

	@Override
	public String toString() {
		return serialise();
	}

}
