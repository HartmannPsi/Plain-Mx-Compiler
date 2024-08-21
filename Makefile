.PHONY: build
build:
	find ./ -name '*.java' | xargs javac -d bin -cp /ulib/antlr-4.13.2-complete.jar


.PHONY: run
run:
	java -Xss8m -cp /ulib/antlr-4.13.2-complete.jar:bin Main

.PHONY: compile
compile:
	find ./ -name '*.java' | xargs javac -d bin -cp lib/antlr-4.13.2-complete.jar

.PHONY: start
start:
	java -Xss8m -cp lib/antlr-4.13.2-complete.jar:bin Main
