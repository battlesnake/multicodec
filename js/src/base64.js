export const encode = buf => {
	if (!Buffer.isBuffer(buf)) {
		throw new Error('Input is not a buffer');
	}
	return buf.toString('base64');
};

export const decode = str => {
	if (typeof str !== 'string') {
		throw new Error('Input is not a string');
	}
	return Buffer.from(str, 'base64');
};
