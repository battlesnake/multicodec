import * as deflate from './deflate';
import * as base64 from './base64';

export const codecs = {
	deflate,
	base64
};

export const get_codec = name => {
	if (name in codecs) {
		return codecs[name];
	} else {
		throw new Error(`Invalid/unsupported codec name: ${name}`);
	}
};
