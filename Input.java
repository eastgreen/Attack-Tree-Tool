public class Input extends JPanel {

    boolean able = true, pair = false;
    Color color;
    Component owner;
    int x, y, weight = 14, height = 20;

    public Input(Component owner, int x, int y) {
        this.owner = owner;
        this.x = x;
        this.y = y;
        initUI();
    }

    private void initUI() {
        setBounds(x, y, weight, height);
        setOpaque(false);
        setLayout(null);
        color = Color.blue;
    }

    public boolean contain(Point p) {
        return contains(p.x - getX(), p.y - getY());
    }

    public void pair(boolean pair) {
        this.pair = pair;
    }

    public void setAvailable(boolean able) {
        this.able = able;
        if (able) {
            color = Color.blue;
        } else {
            color = Color.gray;
        }
        repaint();
    }

    public void select(boolean select) {
        if (select) {
            color = Color.yellow;
        } else {
            color = Color.blue;
        }
        if (owner.output != null && !owner.output.pair) {
            owner.output.setAvailable(!select);
        }
        repaint();
    }

    public int getXs() {
        return owner.getX() + x + 7;
    }

    public int getYs() {
        return owner.getY() + y + 13;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.darkGray);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawLine(7, 0, 7, 15);
        g2d.rotate(Math.toRadians(45), 7, 14);
        g2d.setColor(color);
        g2d.fillRect(2, 8, 10, 10);
        g2d.setColor(Color.white);
        g2d.fillRect(4, 10, 6, 6);

    }
}
