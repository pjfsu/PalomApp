package esei.uvigo.es.palomapp;

public class Piece {

    private String name;
    private int color;
    private int row;
    private int column;

    public Piece(String name, int color, int row, int column) {
        this.name = name;
        this.color = color;
        this.row = row;
        this.column = column;
    }

    public Piece(String name, int color) {
        this.name = name;
        this.color = color;
    }

    public Piece() {
        name = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }
}
