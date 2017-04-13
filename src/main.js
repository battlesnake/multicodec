import { parse_spec, compile_spec } from './spec';
import encode from './encode';
import decode from './decode';

const test = async () => {
	const spec = 'deflate+base64';
	const orig = 'I am a test string hello behold my stringiness';
	const str = await encode(Buffer.from(orig, 'ascii'), spec);
	const buf = await decode(str);
	console.log('Original spec:', spec);
	console.log('Rebuilt spec:', compile_spec(parse_spec(spec)));
	console.log('Original:', orig);
	console.log('Encoded:', str);
	console.log('Decoded:', buf.toString('utf8'));
};

export default {
	encode,
	decode,
	parse_spec,
	compile_spec,
	test
};
