package net.lunaire.core;
public class Setting {
    public String name;
    public boolean bVal;
    public double dVal;
    public String type;
    public Setting(String name, boolean val) { this.name = name; this.bVal = val; this.type = "bool"; }
    public Setting(String name, double val) { this.name = name; this.dVal = val; this.type = "num"; }
}
