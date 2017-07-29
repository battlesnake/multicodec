srcdir = src
outdir = bin

export PATH := $(CURDIR)/node_modules/.bin:$(PATH)

.PHONY: all
all: build

.PHONY: clean
clean:
	rm -rf -- $(outdir) coverage

.PHONY: build
build:
	babel --out-dir $(outdir) $(srcdir)

.PHONY: test
test: build
	rm -rf coverage
	DEBUG=y BABEL_ENV=test istanbul cover _mocha -- --require babel-polyfill --use_strict $(outdir)/test
