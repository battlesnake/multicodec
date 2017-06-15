 * base64: pass-through (no-op) when input data is already in base64 character set (similar to deflate when ratio is >= 100%).

 * parallel xz: once we find a suitable library for it, or spawn `pixz` as a child process and pipe through it.

 * AES encryption: requires a way to specify key to decoder.

 * signing: requires a way to specify public key to decoder.


Decoder should take object of options since layer count may not be as originally requested due to no-ops.

For example:

	{
		"xz": {
			"max_cores": 4,
			"nice": 5
		},
		"aes-crypt": {
			"key": "some secret key"
		},
		"rsa-sign": {
			"certificate": "certificate data"
		},
	}

This would not permit multiple layers of the same type to have different decoder parameters.
