public class OpMax extends Component {

    public OpMax(CanvasPanel canvas, String id) {
        super(canvas, id);
        node = false;
        removable = true;
        rateVal = false;
        phaseVal = false;
        width = 100;
        height = 110;
        print = "max";
        defBorder = new Color(39, 0, 107);
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
        input = new Input(this, width / 2 - 7, height - 20);
        add(output);
        add(input);
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
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        //outer shape
        Shape square = new Rectangle2D.Double(15, 50, 70, 40);
        Shape arc = new Arc2D.Double(15, 20, 70, 60, 180, -180, Arc2D.OPEN);
        Area rnd = new Area(arc);
        Area sqr = new Area(square);
        rnd.add(sqr);
        g2d.setColor(swnBorder);
        outer = rnd;
        g2d.fill(rnd);

        //inner shape
        square = new Rectangle2D.Double(18, 50, 64, 37);
        arc = new Arc2D.Double(18, 23, 64, 54, 180, -180, Arc2D.OPEN);
        rnd = new Area(arc);
        sqr = new Area(square);
        rnd.add(sqr);
        g2d.setColor(Color.white);
        g2d.fill(rnd);

        //draw component name
        g2d.setColor(Color.black);
        if (cp.compValue) {
            drawCenteredString("MAX", 100, 100, g2d);
        } else {

            //area for component name
            g2d.setColor(Color.gray);
            Shape nameR = new Rectangle2D.Double(0, 30, width, 40);
            compNma = nameR;
            g2d.fill(nameR);
            g2d.setColor(Color.white);
            Shape nameRs = new Rectangle2D.Double(3, 33, width - 6, 34);
            g2d.fill(nameRs);

            g2d.setColor(Color.black);
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
