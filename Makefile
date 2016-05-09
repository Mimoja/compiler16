# Top-level makefile.
#
# http://citeseerx.ist.psu.edu/viewdoc/summary?doi=10.1.1.20.2572

SUBMODULES=w2j s01 s02

all:
	for i in $(SUBMODULES); do \
		(cd $$i; $(MAKE) all) \
	done

run-tests:
	cd w2j && $(MAKE) run-tests
