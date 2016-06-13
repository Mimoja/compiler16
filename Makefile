# Top-level makefile.
#
# http://citeseerx.ist.psu.edu/viewdoc/summary?doi=10.1.1.20.2572

SUBMODULES=w2j s01 s02 s03 s04 s05

all:
	for i in $(SUBMODULES); do \
		$(MAKE) -C $$i all; \
	done

clean:
	for i in $(SUBMODULES); do \
		$(MAKE) -C $$i clean; \
	done

run-tests:
	$(MAKE) -C w2j run-tests

.PHONY: all clean run-tests
