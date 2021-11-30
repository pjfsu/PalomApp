package esei.uvigo.es.palomapp;

import android.graphics.Color;

public enum BLACKS {
    EMPTY(Color.TRANSPARENT),

    PAWN(R.drawable.ic_black_pawn),
    BISHOP(R.drawable.ic_black_bishop),
    KNIGHT(R.drawable.ic_black_knight),
    QUEEN(R.drawable.ic_black_queen),
    KING(R.drawable.ic_black_king),
    ROOK(R.drawable.ic_black_rook);

    private int icon;

    private BLACKS(int value) {
        this.icon = value;
    }

    public int getIcon() {
        return icon;
    }
}
