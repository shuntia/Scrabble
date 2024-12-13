public enum Orientation {
    HORIZONTAL(1, 0),
    VERTICAL(0, 1);

    int dx, dy;

    Orientation(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }
}