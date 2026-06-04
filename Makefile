six-one:
	javac -cp src:lib/java-diff-utils-4.15.jar src/com/pao/laboratory06/exercise1/Test.java
	java -cp src:lib/java-diff-utils-4.15.jar com.pao.laboratory06.exercise1.Test

proiect:
	javac -cp src:lib/postgresql-42.7.4.jar src/com/pao/proiectCabinetMedical/Main.java
	java -cp src:lib/postgresql-42.7.4.jar com.pao.proiectCabinetMedical.Main

check:
	javac -cp src:lib/postgresql-42.7.4.jar src/com/pao/proiectCabinetMedical/Checker.java
	java -cp src:lib/postgresql-42.7.4.jar com.pao.proiectCabinetMedical.Checker

demo:
	javac -cp src:lib/postgresql-42.7.4.jar src/com/pao/proiectCabinetMedical/DemoVizual.java
	java -cp src:lib/postgresql-42.7.4.jar com.pao.proiectCabinetMedical.DemoVizual
