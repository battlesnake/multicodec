import zlib from 'zlib';

export const encode = async (buf, opts) => {
	if (!Buffer.isBuffer(buf)) {
		throw new Error('Input is not a buffer');
	}
	return await new Promise((res, rej) => {
		zlib.deflateRaw(buf, (err, out) => {
			if (err) {
				return rej(err);
			} else {
				return opts.force || out.length < buf.length ? res(out) : res(null);
			}
		});
	});
};

export const decode = async buf => {
	if (!Buffer.isBuffer(buf)) {
		throw new Error('Input is not a buffer');
	}
	return await new Promise((res, rej) => {
		zlib.inflateRaw(buf, (err, out) => {
			if (err) {
				return rej(err);
			} else {
				return res(out);
			}
		});
	});
};
