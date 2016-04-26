# Top-level makefile.
#
# http://citeseerx.ist.psu.edu/viewdoc/summary?doi=10.1.1.20.2572

SUBMODULES=While2JasminCompiler s01

all:
	for i in $(SUBMODULES); do \
		(cd $$i; $(MAKE) all) \
	done

run-tests:
	cd While2JasminCompiler && $(MAKE) run-tests
