all: s$(NUMBER).pdf

%.pdf: %.md $(EXTRA_GENERATED) $(EXTRA_STATIC)
	pandoc $< -o $@

ZIP=ex$(NUMBER)_282245_331410_334424_335148.zip

zip: $(ZIP)

$(ZIP): ../w2j all
	rm -f $(ZIP)
	cd ../w2j && zip -q $(realpath .)/$(ZIP) -r src tests -i '*.java' -i '*.txt'

clean:
	rm -f s$(NUMBER).pdf $(EXTRA_GENERATED) $(ZIP)
