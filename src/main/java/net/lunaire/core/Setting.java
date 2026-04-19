package net.lunaire.core;

public class Setting {
    public String name;
    public boolean bVal;
    public double dVal;
    public int color;
    public String type;

    public Setting(String name, boolean val) { this.name = name; this.bVal = val; this.type = "bool"; }
    public Setting(String name, double val) { this.name = name; this.dVal = val; this.type = "num"; }
    public Setting(String name, int color) { this.name = name; this.color = color; this.type = "color"; }
}
