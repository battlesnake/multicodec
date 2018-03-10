import { parse_spec, max_spec_length } from './spec';
import { get_codec } from './codec';

const split_input = data => {
	const x = data.indexOf ? data.indexOf(':') : -1;
	if (x === -1) {
		throw new Error('Invalid input data');
	}
	if (x > max_spec_length) {
		throw new Error(`Encoding specification is too long (${x})`);
	}
	const spec = Buffer.isBuffer(data) ? data.slice(0, x).toString('ascii') : data.substring(0, x);
	const buf = Buffer.isBuffer(data) ? data.slice(x + 1) : data.substring(x + 1);
	return { spec, buf };
};

const decode = async data => {
	let { spec, buf } = split_input(data);
	const layers = parse_spec(spec).reverse();
	for (const layer of layers) {
		const codec = get_codec(layer.codec);
		buf = await codec.decode(buf, layer.args);
	}
	return buf;
};

export default decode;
