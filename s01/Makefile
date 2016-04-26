all: s01.pdf

s01.pdf: s01.md s01e2c.pdf
	pandoc $< -o $@

s01e2c.pdf: s01e2c.tex
	pdflatex $< </dev/null >/dev/null
