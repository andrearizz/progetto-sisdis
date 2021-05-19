package it.unical.progettosisdis.payload.generator.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PwdReq {

    @NotNull
    @Max(40)
    private int length;

    @NotNull
    private boolean containsChar;

    @NotNull
    private boolean containsDigit;

    @NotNull
    private boolean containsSymbols;

    public int getLenght() {
        return length;
    }

    public boolean isContainsChar() {
        return containsChar;
    }

    public boolean isContainsDigit() {
        return containsDigit;
    }

    public boolean isContainsSymbols() {
        return containsSymbols;
    }

    public void setLenght(int lenght) {
        this.length = lenght;
    }

    public void setContainsChar(boolean containsChar) {
        this.containsChar = containsChar;
    }

    public void setContainsDigit(boolean containsDigit) {
        this.containsDigit = containsDigit;
    }

    public void setContainsSymbols(boolean containsSymbols) {
        this.containsSymbols = containsSymbols;
    }
}
