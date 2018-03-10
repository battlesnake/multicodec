import _ from 'lodash';

export const max_spec_length = 1000;

export const spec_delim = '+';
export const spec_arg_delim = ';';

export const parse_spec = spec => spec
		.split(spec_delim)
		.filter(s => s.length)
		.map(x => {
			const m = x.match(/^(\w+)(?:\(([^)]*)\))?$/);
			if (!m) {
				throw new Error(`Invalid codec specification: "${x}"`);
			}
			const [, codec, arglist = ''] = m;
			const args = _(arglist)
				.split(spec_arg_delim)
				.filter(s => s.length)
				.map(s => {
					const n = s.match(/^(\w+)(?:=(.*))?$/);
					if (!n) {
						throw new Error(`Invalid codec specification: "${x}" (Failed to parse "${s}")`);
					}
					const [, key, value = true] = n;
					return [key, value];
				})
				.fromPairs()
				.value();
			return { codec, args };
		});

export const compile_spec = spec => spec
		.map(({ codec, args }) => {
			const arglist = _(args)
				.toPairs()
				.map(([key, value]) => value === null ? key : [key, value].join('='))
				.join(spec_arg_delim);
			return arglist.length ? `${codec}(${arglist})` : codec;
		})
		.join(spec_delim);
