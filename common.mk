all: s$(NUMBER).pdf

%.pdf: %.md $(EXTRA_GENERATED) $(EXTRA_STATIC)
	pandoc $< -o $@

clean:
	rm -f s$(NUMBER).pdf $(EXTRA_GENERATED)
