rm -r antlr4/.antlr/
find ./ -name '*.java' | xargs javac -d bin -cp lib/antlr-4.13.2-complete.jar