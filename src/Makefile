JAVAC = javac
JAVA = java
DIFFLIB_JAR ?=
TEST_CP = .:$(DIFFLIB_JAR)

.PHONY: help dev cabinet cabinet-compile cabinet-run lab6-ex1-compile lab6-ex1-run lab6-ex1-test lab6-ex2-compile lab6-ex2-test clean

help:
	@echo "Targets disponibile:"
	@echo "  make dev              - compileaza si ruleaza proiectul CabinetMedical"
	@echo "  make cabinet          - alias pentru dev"
	@echo "  make cabinet-compile  - compileaza CabinetMedical"
	@echo "  make cabinet-run      - ruleaza CabinetMedical"
	@echo "  make lab6-ex1-compile - compileaza Test.java pentru laborator06/exercise1"
	@echo "  make lab6-ex1-run     - ruleaza Main pentru laborator06/exercise1"
	@echo "  make lab6-ex1-test    - ruleaza testele pentru laborator06/exercise1"
	@echo "  make lab6-ex2-compile - compileaza Test.java pentru laborator06/exercise2"
	@echo "  make lab6-ex2-test    - ruleaza testele pentru laborator06/exercise2"
	@echo "  make clean            - sterge fisierele .class generate"
	@echo ""
	@echo "Pentru testele lab6 care folosesc IOTest, seteaza calea catre jar:"
	@echo "  make lab6-ex1-test DIFFLIB_JAR=/cale/catre/java-diff-utils-4.15.jar"

dev: cabinet

cabinet: cabinet-compile cabinet-run

cabinet-compile:
	$(JAVAC) com/pao/proiectCabinetMedical/Main.java

cabinet-run:
	$(JAVA) com.pao.proiectCabinetMedical.Main

lab6-ex1-compile:
	@if [ -z "$(DIFFLIB_JAR)" ]; then echo "Lipseste DIFFLIB_JAR."; exit 1; fi
	$(JAVAC) -cp $(TEST_CP) com/pao/laboratory06/exercise1/Test.java

lab6-ex1-run:
	$(JAVAC) com/pao/laboratory06/exercise1/Main.java
	$(JAVA) com.pao.laboratory06.exercise1.Main

lab6-ex1-test:
	@if [ -z "$(DIFFLIB_JAR)" ]; then echo "Lipseste DIFFLIB_JAR."; exit 1; fi
	$(JAVAC) -cp $(TEST_CP) com/pao/laboratory06/exercise1/Test.java
	cd .. && $(JAVA) -cp src:$(DIFFLIB_JAR) com.pao.laboratory06.exercise1.Test

lab6-ex2-compile:
	@if [ -z "$(DIFFLIB_JAR)" ]; then echo "Lipseste DIFFLIB_JAR."; exit 1; fi
	$(JAVAC) -cp $(TEST_CP) com/pao/laboratory06/exercise2/Test.java

lab6-ex2-test:
	@if [ -z "$(DIFFLIB_JAR)" ]; then echo "Lipseste DIFFLIB_JAR."; exit 1; fi
	$(JAVAC) -cp $(TEST_CP) com/pao/laboratory06/exercise2/Test.java
	cd .. && $(JAVA) -cp src:$(DIFFLIB_JAR) com.pao.laboratory06.exercise2.Test

clean:
	find . -name '*.class' -delete
