CP = src:lib/java-diff-utils-4.15.jar:lib/sqlite-jdbc-3.47.1.0.jar
PKG = com.pao.laboratory14

ifeq ($(EX),2)
  CP := $(CP):src/com/pao/laboratory14/exercise2/resources
endif

ifeq ($(EX),3)
  TARGET = $(PKG).exercise$(EX).Main
else
  TARGET = $(PKG).exercise$(EX).Checker
endif

run:
	@java -cp $(CP) $(TARGET)
