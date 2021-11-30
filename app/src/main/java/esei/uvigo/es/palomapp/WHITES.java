package esei.uvigo.es.palomapp;

import android.graphics.Color;

public enum WHITES {
    EMPTY(Color.TRANSPARENT),

    PAWN(R.drawable.ic_white_pawn),
    BISHOP(R.drawable.ic_white_bishop),
    KNIGHT(R.drawable.ic_white_knight),
    QUEEN(R.drawable.ic_white_queen),
    KING(R.drawable.ic_white_king),
    ROOK(R.drawable.ic_white_rook);

    private int icon;

    private WHITES(int value) {
        this.icon = value;
    }

    public int getIcon() {
        return icon;
    }
}
