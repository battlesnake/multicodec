import chai from 'chai';
import chai_as_promised from 'chai-as-promised';

import { encode, decode } from '../';

chai.use(chai_as_promised);


const { expect } = chai;
const { describe, it } = global;

const test_string = 'I am a test string, behold my stringiness';
const test_buffer = Buffer.from(test_string, 'ascii');

/* For testing "incompressible" data, TODO use some function to generate deterministic data that's guaranteed ot be hard to deflate */
const random_buffer = new Buffer(1000 * 1000);
for (let i = 0; i < random_buffer.length; i++) {
	random_buffer.writeUInt8((Math.random() * 0x100) | 0, i, true);
}

const rt = async (fmt, data) => {
	const enc = await encode(data, fmt);
	const dec = await decode(enc);
	expect(dec.toString('ascii')).to.equal(data.toString('ascii'));
};

describe('engine', () => {
	it('should fail to encode on invalid codec specification', async () => {
		const invalid_spec = [
			'{odec',
			'codec(*key)'
		];
		for (const spec of invalid_spec) {
			await expect(encode('some data', spec)).to.be.rejectedWith(Error);
		}
	});
	it('should fail to decode on invalid codec specification', async () => {
		const invalid_spec = [
			'codec;',
			'{odec:',
			'codec(*key):'
		];
		for (const spec of invalid_spec) {
			await expect(decode(`${spec}some data`)).to.be.rejectedWith(Error);
		}
	});
	it('should fail to encode if invalid codec is requested', async () => {
		await expect(encode('some data', 'potato')).to.be.rejectedWith(Error);
	});
	it('should fail to decode if invalid codec is requested', async () => {
		await expect(decode('potato:some data')).to.be.rejectedWith(Error);
	});
	it('should fail to decode on invalid input data type', async () => {
		await expect(decode(Math.PI)).to.be.rejectedWith(Error);
	});
	it('should fail to decode on missing/too-long codec specification', async () => {
		const very_long = [Array(100 * 100 * 10).join('x'), 'data'].join(':');
		await expect(decode(very_long)).to.be.rejectedWith(Error);
	});
});

describe('identity', () => {
	it('should pass round-trip test with string', () => rt('identity', test_string));
	it('should pass round-trip test with buffer', () => rt('identity', test_buffer));
	it('should fail to encode on invalid input data type', async () => {
		await expect(encode(Math.PI, 'identity')).to.be.rejectedWith(Error);
	});
	it('should fail to decode on invalid input data type', async () => {
		await expect(decode(Math.PI)).to.be.rejectedWith(Error);
	});
});

describe('identity()', () => {
	it('should pass round-trip test', () => rt('identity()', test_buffer));
});

describe('identity+identity()', () => {
	it('should pass round-trip test', () => rt('identity+identity()', test_buffer));
});

describe('base64', () => {
	it('should pass round-trip test', () => rt('base64', test_buffer));
	it('should fail if encoder input is not a buffer', async () => {
		await expect(encode('not a buffer', 'base64')).to.be.rejectedWith(Error);
	});
	it('should fail if decoder input is not a string', async () => {
		await expect(decode(Buffer.from('base64:not a string'))).to.be.rejectedWith(Error);
	});
});

describe('deflate(force)', () => {
	it('should pass round-trip test', () => rt('deflate', test_buffer));
	it('should pass round-trip test', () => rt('deflate', random_buffer));
});

describe('deflate', () => {
	it('should pass round-trip test', () => rt('deflate', test_buffer));
	it('should pass round-trip test', () => rt('deflate', random_buffer));
});

describe('deflate(force)+base64', () => {
	it('should pass round-trip test', () => rt('deflate(force)+base64', test_buffer));
	it('should pass round-trip test', () => rt('deflate(force)+base64', random_buffer));
});

describe('deflate+base64', () => {
	it('should pass round-trip test', () => rt('deflate+base64', test_buffer));
	it('should pass round-trip test', () => rt('deflate+base64', random_buffer));
});
