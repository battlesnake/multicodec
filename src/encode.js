import { parse_spec, compile_spec, max_spec_length } from './spec';
import { get_codec } from './codec';

const default_spec = 'deflate+base64';

const encode = async (buf, spec = default_spec) => {
	if (typeof spec !== 'string') {
		throw new Error('Invalid encoding specification');
	}
	if (spec.length > max_spec_length) {
		throw new Error(`Encoding specification is too long (${spec.length})`);
	}
	const layers_used = [];
	const layers = parse_spec(spec);
	for (const layer of layers) {
		const codec = get_codec(layer.codec);
		const newbuf = await codec.encode(buf, layer.args);
		if (newbuf !== null) {
			layers_used.push(layer);
			buf = newbuf;
		}
	}
	const real_spec = compile_spec(layers_used);
	if (Buffer.isBuffer(buf)) {
		const specbuf = Buffer.from(`${real_spec}:`, 'ascii');
		return Buffer.concat(specbuf, buf).toString('ascii');
	} else if (typeof buf === 'string') {
		return [real_spec, buf].join(':');
	} else {
		throw new Error('Invalid type');
	}
};

export default encode;
