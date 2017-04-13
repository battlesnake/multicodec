src = src
bin = bin

in = $(wildcard $(src)/*.js $(src)/*/*.js)
out = $(in:$(src)/%=$(bin)/%)

.PHONY: all
all: build

.PHONY: clean
clean:
	rm -rf -- $(bin)

.PHONY: build
build: $(out) $(bin)/index.js

$(out): $(bin)/%: $(src)/%
	@mkdir -p $(@D)
	babel --source-maps inline --presets latest -o $@ $^

$(bin)/index.js:
	@mkdir -p $(@D)
	printf "%s\n" '#!/usr/bin/env nodejs' 'require("source-map-support").install();' 'require("babel-polyfill");' 'require("./main").default.test();' > $@
	chmod +x $@
