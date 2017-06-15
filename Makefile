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
	printf "%s\n" 'require("source-map-support").install();' 'require("babel-polyfill");' 'module.exports = require("./main").default;' > $@
	chmod +x $@
