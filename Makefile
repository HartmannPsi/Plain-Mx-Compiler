.PHONY: build
build:
	find antlr4 -name '*.java' | xargs javac -d bin -cp /ulib/antlr-4.13.2-complete.jar


.PHONY: run
run:
	java -cp /ulib/antlr-4.13.2-complete.jar:bin Main