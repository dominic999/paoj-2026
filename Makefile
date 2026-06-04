CP = src:lib/java-diff-utils-4.15.jar
PKG = com.pao.laboratory13

ifeq ($(EX),2)
  TARGET = $(PKG).exercise$(EX).Main
else
  TARGET = $(PKG).exercise$(EX).Checker
endif

run:
	@java -cp $(CP) $(TARGET)
