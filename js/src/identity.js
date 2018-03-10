const transform = buf => {
	if (Buffer.isBuffer(buf)) {
		return Buffer.from(buf);
	} else if (typeof buf === 'string') {
		return buf;
	} else {
		throw new Error('Unknown data type');
	}
};

export const encode = transform;
export const decode = transform;
