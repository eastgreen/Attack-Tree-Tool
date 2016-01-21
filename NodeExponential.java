public class NodeExponential extends Component {

    public NodeExponential(CanvasPanel canvas, String id) {
        super(canvas, id);
        node = true;
        removable = true;
        rateVal = true;
        phaseVal = false;
        width = 100;
        height = 90;
        defBorder = Color.gray;
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
        output = new Output(this, width / 2 - 7, 0);
        add(output);
        setBounds(x, y, width, height);
        cp.compList.add(this);
        cp.rootLvl.add(this);
        cp.add(this);
        cp.adjCanvas();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        //outer shape
        g2d.setColor(swnBorder);
        Shape outerShape = new Rectangle2D.Double(0, 20, width, height - 20);
        this.outer = outerShape;
        g2d.fill(outerShape);

        //inner shape
        g2d.setColor(Color.white);
        Shape innerShape = new Rectangle2D.Double(3, 23, width - 6, height - 26);
        g2d.fill(innerShape);

        //draw component name
        g2d.setColor(Color.black);
        if (cp.compValue) {
            drawCenteredString("Exp(" + rate + ")", 100, 100, g2d);
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
                drawCenteredString(getName(), 100, 100, g2d);
            }
        }
        //draw number
        if (parent != null && parent.id.substring(0, 3).equals("cog")) {
            g2d.drawString(Integer.toString(parent.child.indexOf(this)), 60, 18);
        }
    }
}
