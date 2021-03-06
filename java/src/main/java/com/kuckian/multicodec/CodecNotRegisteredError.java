package com.kuckian.multicodec;

public class CodecNotRegisteredError extends MulticodecError {

	private static final long serialVersionUID = 4345895933528884582L;

	public CodecNotRegisteredError(String name) {
		super("Codec not registered: \"" + name + "\"");
	}

}
