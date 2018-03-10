const trial_size = 65536;

/* Lazy load zlib so we don't break front-end builds which don't require it */
let _zlib_wrapper = null;
const lazy_zlib = () => {
	if (!_zlib_wrapper) {
		/* To fool browserify */
		const zlib = require(['z', 'l', 'i', 'b'].join(''));
		const wrap = func => (...args) => new Promise((res, rej) => {
			zlib[func](...args, (err, out) => {
				/* istanbul ignore if */
				if (err) {
					return rej(err);
				} else {
					return res(out);
				}
			});
		});
		_zlib_wrapper = {
			deflateRaw: wrap('deflateRaw'),
			inflateRaw: wrap('inflateRaw')
		};
	}
	return _zlib_wrapper;
};

export const encode = async (buf, opts) => {
	const zlib = lazy_zlib();
	if (!Buffer.isBuffer(buf)) {
		throw new Error('Input is not a buffer');
	}
	/* For big buffer, test small chunk of start to guess whether data is incompressible */
	if (buf.length > trial_size && !opts.force) {
		const trial = await zlib.deflateRaw(buf.slice(0, trial_size));
		if (trial.length >= trial_size) {
			return null;
		}
	}
	const out = await zlib.deflateRaw(buf);
	/* If not "forced", then instruct engine to skip this stage if data is incompressible */
	if (out.length >= buf.length && !opts.force) {
		return null;
	}
	return out;
};

export const decode = async buf => {
	const zlib = lazy_zlib();
	if (!Buffer.isBuffer(buf)) {
		throw new Error('Input is not a buffer');
	}
	return await new Promise((res, rej) => {
		zlib.inflateRaw(buf, (err, out) => {
			/* istanbul ignore if */
			if (err) {
				return rej(err);
			} else {
				return res(out);
			}
		});
	});
};
