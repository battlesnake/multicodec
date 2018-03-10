package com.kuckian.multicodec;

public class InvalidCodingSchemeException extends Exception {

	private static final long serialVersionUID = -2396810199765080089L;

	public InvalidCodingSchemeException() {
		this("Invalid coding scheme");
	}

	public InvalidCodingSchemeException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public InvalidCodingSchemeException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public InvalidCodingSchemeException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public InvalidCodingSchemeException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}

}
