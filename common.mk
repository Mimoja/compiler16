all: s$(NUMBER).pdf

%.pdf: %.md $(EXTRA_GENERATED) $(EXTRA_STATIC)
	pandoc $< -o $@

ZIP=ex$(NUMBER)_282245_331410_334424_335148.zip

zip: $(ZIP)

$(ZIP): ../w2j all
	rm -f $(ZIP)
	cd ../w2j && zip -q $(realpath .)/$(ZIP) -r . -i '*.java' -i '*.txt' -i 'Makefile'
	zip -q $(ZIP) s$(NUMBER).pdf

clean:
	rm -f s$(NUMBER).pdf $(EXTRA_GENERATED) $(ZIP)
