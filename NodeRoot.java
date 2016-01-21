public class NodeRoot extends Component {

    public NodeRoot(CanvasPanel cp, String id) {
        super(cp, id);
        node = true;
        removable = false;
        rateVal = false;
        phaseVal = false;
        width = 100;
        height = 90;
        print = "";
        defBorder = Color.black;
        swnBorder = defBorder;
    }

    public void setLoc(Point p) {
        this.x = p.x;
        this.y = p.y;
        initUI();
    }

    private void initUI() {
        setOpaque(false);
        setLayout(null);
        input = new Input(this, width / 2 - 7, height - 20);
        add(input);
        setBounds(x, y, width, height);
        cp.compList.add(this);
        cp.rootLvl.add(this);
        cp.add(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        //outer shape
        g2d.setColor(swnBorder);
        Shape outerShape = new Rectangle2D.Double(0, 0, width, height - 20);
        this.outer = outerShape;
        g2d.fill(outerShape);

        //inner shape
        g2d.setColor(Color.lightGray);
        g2d.fillRect(3, 3, width - 6, height - 26);

        //draw component name
        g2d.setColor(Color.black);
        if (cp.compValue) {
            drawCenteredString("Root", 100, 60, g2d);
        } else {
            if (getName().length() > 15) {
                if (getName().length() > 30) {
                    drawCenteredString(getName().substring(0, 15), 100, 80, g2d);
                    drawCenteredString(getName().substring(15, 27) + "...", 100, 110, g2d);
                } else {
                    drawCenteredString(getName().substring(0, 15), 100, 80, g2d);
                    drawCenteredString(getName().substring(15, getName().length()), 100, 110, g2d);
                }
            } else {
                drawCenteredString(getName(), 100, 60, g2d);
            }
        }
    }
}
