package com.kuckian.multicodec;

public class CodecNameConflictError extends MulticodecError {

	private static final long serialVersionUID = -1677170865081735904L;

	public CodecNameConflictError(String name) {
		super("Codec already registered with name: \"" + name + "\"");
	}

}
