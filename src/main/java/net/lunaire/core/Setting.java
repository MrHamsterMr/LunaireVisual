package net.lunaire.core;

public class Setting {
    public String name;
    public boolean bVal; // Для галочек
    public double dVal;  // Для чисел
    public int color;    // Для цветов
    public String type;  // "bool", "num", "color"

    public Setting(String name, boolean val) { this.name = name; this.bVal = val; this.type = "bool"; }
    public Setting(String name, double val) { this.name = name; this.dVal = val; this.type = "num"; }
    public Setting(String name, int color) { this.name = name; this.color = color; this.type = "color"; }
}
