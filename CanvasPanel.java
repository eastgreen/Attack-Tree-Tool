public class CanvasPanel extends JPanel implements MouseInputListener {

    TabbedPane tp;
    Connection connection = new Connection(this);
    ArrayList<Component> compList = new ArrayList<>();
    ArrayList<Component> rootLvl = new ArrayList<>();
    ArrayList<Connection> lines = new ArrayList<>();
    Component comp = null;
    Connection line = null;
    int compIndex = 0, leafIndex = 0, opIndex = 0;
    boolean compValue = true;
    boolean save = true;
    String expression = "";
    File sLoc = null;
    JPopupMenu pop;
    JRadioButtonMenuItem name, value;
    Shape outerShape = new Rectangle2D.Double();
    Shape innerShape = new Rectangle2D.Double();
    String shape = "";

    public CanvasPanel(TabbedPane tp) {
        addMouseListener(this);
        addMouseMotionListener(this);
        this.tp = tp;
        initUI();
    }

    private void initUI() {
        pop = new JPopupMenu();
        JMenu compMenu = new JMenu("Component");
        pop.add(compMenu);
        ButtonGroup bg = new ButtonGroup();
        name = new JRadioButtonMenuItem("Name");
        bg.add(name);
        compMenu.add(name);
        value = new JRadioButtonMenuItem("Value", true);
        bg.add(value);
        compMenu.add(value);

        ActionListener compMenuListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                compValue = value.isSelected();
                repaint();
            }
        };
        name.addActionListener(compMenuListener);
        value.addActionListener(compMenuListener);
        JMenuItem layout = new JMenuItem("Layout");
        layout.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setLayout();
                repaint();
            }
        });
        pop.add(layout);

        JMenuItem close = new JMenuItem("Close");
        close.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (save) {
                    tp.clsCanvas();
                } else {
                    int option = JOptionPane.showOptionDialog(
                            null,
                            "This model are not saved!\n"
                            + "Are you sure you want to close?",
                            "Close", JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE, null, null,
                            null);
                    if (option == JOptionPane.YES_OPTION) {
                        tp.clsCanvas();
                    }
                }
            }
        });

        pop.add(close);

        setBackground(Color.white);
        adjCanvas();
        setLayout(null);
    }

    public void setIndex(int comp, int leaf, int gate) {
        compIndex = comp;
        leafIndex = leaf;
        opIndex = gate;
    }

    public void setSaveFileLocation(File sLoc) {
        this.sLoc = sLoc;
    }

    public void setSave(boolean save) {
        int index = tp.getSelectedIndex();
        String title = tp.getTitleAt(tp.getSelectedIndex());
        if (this.save && !save) {
            tp.setTitleAt(index, title + "*");
        } else if (!this.save && save) {
            tp.setTitleAt(index, title.substring(0, title.length() - 1));
        }
        this.save = save;
    }

    public void addRoot(Point p, String name) {
        NodeRoot root = new NodeRoot(this, "root");
        root.setName(name);
        root.setLoc(p);
    }

    public void newComp(boolean mouse, Point p, String comp) {
        switch (comp) {
            case "ern":
                NodeErlang ern = new NodeErlang(this, "ern" + compIndex);
                ern.setName("Leaf " + leafIndex);
                if (mouse) {
                    ern.setLoc(new Point(p.x - ern.width / 2, p.y - ern.height / 2));
                } else {
                    ern.setLoc(p);
                }
                compIndex++;
                leafIndex++;
                break;
            case "exn":
                NodeExponential exn = new NodeExponential(this, "exn" + compIndex);
                exn.setName("Leaf " + leafIndex);
                if (mouse) {
                    exn.setLoc(new Point(p.x - exn.width / 2, p.y - exn.height / 2));
                } else {
                    exn.setLoc(p);
                }
                compIndex++;
                leafIndex++;
                break;
            case "ang":
                OpMax ag = new OpMax(this, "ang" + compIndex);
                ag.setName("Op " + opIndex);
                if (mouse) {
                    ag.setLoc(new Point(p.x - ag.width / 2, p.y - ag.height / 2));
                } else {
                    ag.setLoc(p);
                }
                compIndex++;
                opIndex++;
                break;
            case "org":
                OpMin og = new OpMin(this, "org" + compIndex);
                og.setName("Op " + opIndex);
                if (mouse) {
                    og.setLoc(new Point(p.x - og.width / 2, p.y - og.height / 2));
                } else {
                    og.setLoc(p);
                }
                compIndex++;
                opIndex++;
                break;
            case "cog":
                OpCon cg = new OpCon(this, "cog" + compIndex);
                cg.setName("Op " + opIndex);
                if (mouse) {
                    cg.setLoc(new Point(p.x - cg.width / 2, p.y - cg.height / 2));
                } else {
                    cg.setLoc(p);
                }
                compIndex++;
                opIndex++;
                break;
            default:
                break;
        }
    }

    public void selectInput(Input input) {
        if (connection.input == null && connection.output == null) {
            input.select(true);
            input.owner.select(true);
            connection.setInput(input);
        } else if (connection.input != null) {
            connection.input.select(false);
            connection.input.owner.select(false);
            input.select(true);
            input.owner.select(true);
            connection.setInput(input);
        } else if (connection.output != null) {
            connection.output.select(false);
            connection.setInput(input);
            connection.connect(true);
            lines.add(connection);
            connection.output.owner.select(false);
            connection = new Connection(this);
            setSave(false);
            repaint();
        }
    }

    public void selectOutput(Output output) {
        if (connection.input == null && connection.output == null) {
            output.select(true);
            output.owner.select(true);
            connection.setOutput(output);
        } else if (connection.output != null) {
            connection.output.select(false);
            connection.output.owner.select(false);
            output.select(true);
            output.owner.select(true);
            connection.setOutput(output);
        } else if (connection.input != null) {
            connection.input.select(false);
            connection.setOutput(output);
            connection.connect(true);
            lines.add(connection);
            connection.input.owner.select(false);
            connection = new Connection(this);
            setSave(false);
            repaint();
        }
    }

    public void newConnection() {
        connection.connect(true);
        lines.add(connection);
        connection = new Connection(this);
    }

    public Component getComponent(String id) {
        for (Component c : compList) {
            if (c.id.equals(id)) {
                return c;
            }
        }
        return null;
    }

    public void adjCanvas() {
        int width = 800;
        int height = 550;
        int hMax = 0;
        int vMax = 0;
        for (Component c : compList) {
            if (c.getX() + 100 >= hMax) {
                hMax = c.getX() + 200;
            }
            if (c.getY() + 110 >= vMax) {
                vMax = c.getY() + 200;
            }
        }
        if (width > hMax && height > vMax) {
            setPreferredSize(new Dimension(width, height));
            revalidate();
        } else if (width <= hMax && height > vMax) {
            setPreferredSize(new Dimension(hMax, height));
            revalidate();
        } else if (width > hMax && height <= vMax) {
            setPreferredSize(new Dimension(width, vMax));
            revalidate();
        } else {
            setPreferredSize(new Dimension(hMax, vMax));
            revalidate();
        }
        repaint();
    }

    public void setLayout() {
        for (Component c : rootLvl) {
            if (rootLvl.indexOf(c) == 0) {
                c.tree(0.0);
            } else {
                Component cs = rootLvl.get(rootLvl.indexOf(c) - 1);
                c.tree(cs.hMax + 1.0);
            }
        }
        for (Component c : compList) {
            layout(c);
        }
    }

    private void layout(Component c) {
        int x, y;
        x = (int) (40 + c.hLvl * 120);
        y = 10 + c.vLvl * 140;
        c.setLocation(x, y);
        adjCanvas();
        setSave(false);
    }

    public void genExps() {
        expression = "";
        if (!compList.get(0).child.isEmpty()) {
            compList.get(0).child.get(0).addExpression("");
            if (expression.contains("error")) {
                expression = "Format Error \nAll leaf node must either exponential node or erlang node";
            }
        }
        tp.mp.ep.area.setText(expression);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        for (Connection l : lines) {
            l.paintConnection(g2d);
        }
        //outer shape
        g2d.setColor(Color.gray);
        g2d.fill(outerShape);
        //inner shape
        g2d.setColor(Color.white);
        g2d.fill(innerShape);
        //String
        g2d.setColor(Color.black);
        int x = (int) outerShape.getBounds2D().getX();
        int y = (int) outerShape.getBounds2D().getY();
        g2d.drawString(shape, x + 5, y + 65);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //not used
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Point p = e.getPoint();
        Rectangle net = new Rectangle(6, 6);
        net.setFrameFromCenter(p.x, p.y, p.x + 3, p.y + 3);
        if (SwingUtilities.isLeftMouseButton(e)) {
            if (lines.isEmpty()) {
                if (comp != null) {
                    comp.select(false);
                }
            } else {
                for (Connection c : lines) {
                    if (comp != null) {
                        comp.select(false);
                    }
                    if (line != null) {
                        line.select(false);
                    }
                    if (c.isLine(net)) {
                        c.select(true);
                        break;
                    }
                }
                repaint();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            pop.show(e.getComponent(), e.getX(), e.getY());
        } else if (SwingUtilities.isLeftMouseButton(e)) {
            if (!tp.tool.equals("frh") && !tp.tool.equals("con")) {
                if (comp != null) {
                    comp.select(false);
                }
                newComp(true, e.getPoint(), tp.tool);
                setSave(false);
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //not used
    }

    @Override
    public void mouseExited(MouseEvent e) {
        outerShape = new Rectangle2D.Double();
        innerShape = new Rectangle2D.Double();
        shape = "";
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        //not used
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        //not used
        switch (tp.tool) {
            case "ern":
                outerShape = new Rectangle2D.Double(e.getX() - 50, e.getY() - 25, 100, 70);
                innerShape = new Rectangle2D.Double(e.getX() - 47, e.getY() - 22, 94, 64);
                shape = "ERLANG";
                break;
            case "exn":
                outerShape = new Rectangle2D.Double(e.getX() - 50, e.getY() - 25, 100, 70);
                innerShape = new Rectangle2D.Double(e.getX() - 47, e.getY() - 22, 94, 64);
                shape = "EXPONENTIAL";
                break;
            case "ang":
                Shape square = new Rectangle2D.Double(e.getX() - 30, e.getY(), 70, 40);
                Shape arc = new Arc2D.Double(e.getX() - 30, e.getY() - 30, 70, 60, 180, -180, Arc2D.OPEN);
                Area rnd = new Area(arc);
                Area sqr = new Area(square);
                rnd.add(sqr);
                outerShape = rnd;

                square = new Rectangle2D.Double(e.getX() - 27, e.getY(), 64, 37);
                arc = new Arc2D.Double(e.getX() - 27, e.getY() - 27, 64, 54, 180, -180, Arc2D.OPEN);
                rnd = new Area(arc);
                sqr = new Area(square);
                rnd.add(sqr);
                innerShape = rnd;
                shape = "MAX";
                break;
            case "org":
                GeneralPath gateShape = new GeneralPath();
                gateShape.moveTo(e.getX() - 30, e.getY() + 43);
                gateShape.curveTo(e.getX() - 30, e.getY() + 43, e.getX() - 25, e.getY() - 13, e.getX() + 5, e.getY() - 36);
                gateShape.curveTo(e.getX() + 5, e.getY() - 36, e.getX() + 35, e.getY() - 13, e.getX() + 38, e.getY() + 43);
                gateShape.curveTo(e.getX() + 38, e.getY() + 43, e.getX() + 5, e.getY() + 23, e.getX() - 30, e.getY() + 43);
                gateShape.closePath();
                outerShape = gateShape;

                GeneralPath gateShapes = new GeneralPath();
                gateShapes.moveTo(e.getX() - 27, e.getY() + 37);
                gateShapes.curveTo(e.getX() - 27, e.getY() + 37, e.getX() - 22, e.getY() - 10, e.getX() + 5, e.getY() - 33);
                gateShapes.curveTo(e.getX() + 5, e.getY() - 33, e.getX() + 32, e.getY() - 10, e.getX() + 35, e.getY() + 37);
                gateShapes.curveTo(e.getX() + 35, e.getY() + 37, e.getX() + 5, e.getY() + 20, e.getX() - 27, e.getY() + 37);
                gateShapes.closePath();
                innerShape = gateShapes;
                shape = "MIN";
                break;
            case "cog":
                square = new Rectangle2D.Double(e.getX() - 30, e.getY() - 5, 70, 40);
                arc = new Arc2D.Double(e.getX() - 30, e.getY() - 35, 70, 60, 180, -180, Arc2D.OPEN);
                rnd = new Area(arc);
                sqr = new Area(square);
                rnd.add(sqr);
                outerShape = rnd;

                square = new Rectangle2D.Double(e.getX() - 27, e.getY() - 5, 64, 37);
                arc = new Arc2D.Double(e.getX() - 27, e.getY() - 32, 64, 54, 180, -180, Arc2D.OPEN);
                rnd = new Area(arc);
                sqr = new Area(square);
                rnd.add(sqr);
                innerShape = rnd;
                shape = "CON";
                break;
        }
        repaint();
    }
}
