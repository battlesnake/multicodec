const chai = require('chai');
chai.should();
const expect = chai.expect;
const { describe, it } = global;

const { encode, decode } = require('../bin');

const test_string = Buffer.from('I am a test string, behold my stringiness', 'ascii');

const rt = (fmt, data) => Promise.resolve(null)
	.then(() => encode(data, fmt))
	.then(enc => decode(enc))
	.then(dec => expect(dec.toString('ascii')).to.equal(data.toString('ascii')));

describe('identity', () => {
	it('should pass round-trip test', () => rt('identity', test_string));
});

describe('identity()', () => {
	it('should pass round-trip test', () => rt('identity()', test_string));
});

describe('identity+identity()', () => {
	it('should pass round-trip test', () => rt('identity+identity()', test_string));
});

describe('base64', () => {
	it('should pass round-trip test', () => rt('base64', test_string));
});

describe('deflate(force)', () => {
	it('should pass round-trip test', () => rt('deflate', test_string));
});

describe('deflate', () => {
	it('should pass round-trip test', () => rt('deflate', test_string));
});

describe('deflate(force)+base64', () => {
	it('should pass round-trip test', () => rt('deflate(force)+base64', test_string));
});

describe('deflate+base64', () => {
	it('should pass round-trip test', () => rt('deflate+base64', test_string));
});
