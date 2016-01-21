public class OpMin extends Component {

    public OpMin(CanvasPanel canvas, String id) {
        super(canvas, id);
        node = false;
        removable = true;
        rateVal = false;
        phaseVal = false;
        width = 100;
        height = 110;
        print = "min";
        defBorder = new Color(14, 107, 0);
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
        GeneralPath gateShape = new GeneralPath();
        gateShape.moveTo(15, 98);
        gateShape.curveTo(15, 98, 20, 42, 50, 19);
        gateShape.curveTo(50, 19, 80, 42, 83, 98);
        gateShape.curveTo(83, 98, 50, 78, 15, 98);
        gateShape.closePath();
        g2d.setColor(swnBorder);
        outer = gateShape;
        g2d.fill(gateShape);
        g2d.draw(gateShape);

        //inner shape
        GeneralPath gateShapes = new GeneralPath();
        gateShapes.moveTo(18, 92);
        gateShapes.curveTo(18, 92, 23, 45, 50, 22);
        gateShapes.curveTo(50, 22, 77, 45, 80, 92);
        gateShapes.curveTo(80, 92, 50, 75, 18, 92);
        gateShapes.closePath();
        g2d.setColor(Color.white);
        g2d.fill(gateShapes);
        g2d.draw(gateShapes);

        //draw component name
        g2d.setColor(Color.black);
        if (cp.compValue) {
            drawCenteredString("MIN", 100, 100, g2d);
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
