Multicodec
==========

Convenient library for encoding and decoding arbitrary data, using `string` or `Buffer` for data type.

Encoded data is tagged with encoding used, so the decoder only needs to support each of the layers used for encoding - it does not need to be configured to the particular set of encodings used.

Example
-------

	import { encode, decode } from 'multicodec';
	
	/* Encodings to use: Compression (deflate) + stringify (base64) */
	const format = 'deflate+base64';
	
	/* Input data to encode */
	const input = Buffer.from('Some input data', 'utf8');
	
	/* Encode using given format specification */
	const output = encode(input, format);
	
	...
	
	/* Note: we do not separately specify the format to the decoder, the actual encodings used is embedded as a string at the start of the encoder output */
	const decoded = decode(output);

Tests
-----

	npm test

Encoding specification
----------------------

	codec1+codec2(param1=value,param2)+codec3

Encodings
---------

### deflate

Deflate/inflate compression/decompression.

Arguments:

 * force (optional) - force encoding to be used even if output is not smaller than input

### base64

Base64 encoder/decoder to allow data to be transmitted as ASCII strings.

### identity

Passes data through unmodified.
