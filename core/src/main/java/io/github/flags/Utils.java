package io.github.flags;

import com.badlogic.gdx.math.Vector2;

public class Utils {
    public static boolean isAlmostEqual(Vector2 firstValue, Vector2 secondValue, int margin) {
        int diffX = (int) Math.abs(secondValue.x - firstValue.x);
        int diffY = (int) Math.abs(secondValue.y - firstValue.y);
        return diffX < margin && diffY < margin;
    }
}
