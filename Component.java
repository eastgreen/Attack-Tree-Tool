public class Component extends JPanel implements MouseInputListener {

    Component parent = null;
    Component roots = this;
    ArrayList<Component> child = new ArrayList<>();
    ArrayList<Connection> lines = new ArrayList<>();
    boolean node, removable, rateVal, phaseVal;
    boolean onDrag = false;
    int vLvl = 0;
    double hLvl = 0.0;
    double hMin = 0.0, hMax = 0.0;
    int x, y, width, height;
    int startDragX, startDragY, prevX, prevY;
    double phase = 0.0;
    double rate = 0.0;
    String id, print, description = "";
    Shape outer, compNma = null;
    Color defBorder, selBorder = new Color(255, 193, 5), swnBorder;
    Input input = null;
    Output output = null;
    CanvasPanel cp;

    public Component(CanvasPanel cp, String id) {
        addMouseListener(this);
        addMouseMotionListener(this);
        this.cp = cp;
        this.id = id;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public void setPhase(double phase) {
        this.phase = phase;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public void setRoots(Component roots) {
        this.roots = roots;
        for (Component c : child) {
            c.setRoots(roots);
        }
    }

    public void tree(double hMin) {
        vLvl = 0;
        this.hLvl = hMin;
        this.hMin = hMin;
        this.hMax = hMin;
        adjChild();
        for (Component c : child) {
            c.adjSiblings();
            adjParent();
        }
    }

    private void centerChild() {
        double center = (child.size() - 1.0) / 2.0;
        if (hLvl - center >= roots.hMin) {
            for (Component c : child) {
                c.vLvl = vLvl + 1;
                c.hLvl = hLvl + child.indexOf(c) - center;
                c.adjChild();
                if (roots.hMax <= c.hLvl) {
                    roots.hMax = c.hLvl;
                }
            }
        } else {
            for (Component c : child) {
                c.vLvl = vLvl + 1;
                c.hLvl = child.indexOf(c) + roots.hMin;
                c.adjChild();
                if (roots.hMax <= c.hLvl) {
                    roots.hMax = c.hLvl;
                }
            }
            adjParent();
        }
    }

    private void adjChild() {
        if (!child.isEmpty()) {
            centerChild();
            if (prevComp(child.get(0).hLvl) != null) {
                double prev = prevComp(child.get(0).hLvl).hLvl;
                for (Component c : child) {
                    c.hLvl = child.indexOf(c) + prev + 1.0;
                    c.adjChild();
                    if (roots.hMax <= c.hLvl) {
                        roots.hMax = c.hLvl;
                    }
                }
                adjParent();
            }
        }
    }

    private Component prevComp(double limit) {
        for (Component c : cp.compList) {
            if (c.roots.equals(roots) && c.vLvl == vLvl && c.hLvl < hLvl) {
                if (!c.child.isEmpty()) {
                    if (c.child.get(c.child.size() - 1).hLvl + 0.5 > limit) {
                        return c.child.get(c.child.size() - 1);
                    }
                }
            }
        }
        return null;
    }

    private void adjParent() {
        double left = child.get(0).hLvl;
        double right = child.get(child.size() - 1).hLvl;
        if ((left + right) % 1 != 0) {
            hLvl = (left + right - 0.5) / 2.0;
        } else {
            hLvl = (left + right) / 2.0;
        }
        if (parent != null) {
            parent.adjParent();
        }
    }

    private void adjSiblings() {
        for (Component c : child) {
            c.adjSiblings();
            adjParent();
        }

        for (Component c : cp.compList) {
            if (c.roots.equals(roots) && !c.equals(roots) && !c.equals(this)) {
                if (c.vLvl == vLvl) {
                    if (c.parent.equals(parent) && c.parent.child.indexOf(c) > parent.child.indexOf(this) && hLvl >= c.hLvl) {
                        c.hLvl = hLvl + 1.0;
                        if (roots.hMax <= c.hLvl) {
                            roots.hMax = c.hLvl;
                        }
                        c.adjChild();
                        c.adjSiblings();
                        return;
                    } else if (c.hLvl > hLvl && hLvl + 0.5 >= c.hLvl) {
                        c.hLvl = hLvl + 1.0;
                        if (roots.hMax <= c.hLvl) {
                            roots.hMax = c.hLvl;
                        }
                        c.adjChild();
                        c.adjSiblings();
                        return;
                    }
                }
            }
        }
    }

    public void addExpression(String tab) {
        if (rateVal && phaseVal) {
            print = "erl(" + phase + ", " + rate + ")";
        } else if (rateVal && !phaseVal) {
            print = "exp(" + rate + ")";
        }
        if (node) {
            cp.expression = cp.expression + tab + this.print;
        } else if (!node) {
            if (!child.isEmpty()) {
                cp.expression = cp.expression + tab + this.print + "(\n";
                for (int i = 0; i < child.size(); i++) {
                    if ((i + 1) < child.size()) {
                        child.get(i).addExpression(tab + "\t");
                        cp.expression = cp.expression + ",\n";
                    } else if ((i + 1) >= child.size()) {
                        child.get(i).addExpression(tab + "\t");
                        cp.expression = cp.expression + "\n" + tab + ")";
                    }
                }
            } else {
                cp.expression = "error";
            }
        }
    }

    public void remove() {
        while (!lines.isEmpty()) {
            lines.get(0).connect(false);
        }
        this.select(false);
        cp.compList.remove(this);
        cp.remove(this);
        cp.setSave(false);
    }

    public void select(boolean select) {
        if (select) {
            if (cp.comp != null && !cp.comp.equals(this)) {
                cp.comp.select(false);
            }
            if (cp.line != null) {
                cp.line.select(false);
            }
            cp.comp = this;
            cp.tp.mp.pp.setComponent(this);
            if (removable) {
                cp.tp.mp.remove.setEnabled(true);
            }
            swnBorder = selBorder;
        } else {
            cp.comp = null;
            cp.tp.mp.pp.setComponent(null);
            cp.tp.mp.remove.setEnabled(false);
            swnBorder = defBorder;
            if (cp.connection.input != null && cp.connection.output == null) {
                cp.connection.input.select(false);
                cp.connection.setInput(null);
            }
            if (cp.connection.output != null && cp.connection.input == null) {
                cp.connection.output.select(false);
                cp.connection.setOutput(null);
            }
        }
        System.out.println(phase + " " + rate);
        repaint();
    }

    private boolean inArea(Point p) {
        return (outer.contains(p) || (compNma != null && compNma.contains(p)));
    }

    protected void drawCenteredString(String s, int w, int h, Graphics2D g2d) {
        FontMetrics fm = g2d.getFontMetrics();
        int x1 = (w - fm.stringWidth(s)) / 2;
        int y1 = (fm.getAscent() + (h - (fm.getAscent() + fm.getDescent())) / 2);
        g2d.drawString(s, x1, y1);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            switch (cp.tp.tool) {
                case "con":
                    if (input != null && input.contain(e.getPoint()) && input.able) {
                        cp.selectInput(input);
                    } else if (output != null && output.contain(e.getPoint()) && output.able) {
                        cp.selectOutput(output);
                    }
                    break;
                case "frh":
                    select(true);
                    break;
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            if (cp.tp.tool.equals("frh") && inArea(e.getPoint())) {
                onDrag = true;
                startDragX = e.getX();
                startDragY = e.getY();
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (onDrag) {
            int newX = getX() + (e.getX() - startDragX);
            int newY = getY() + (e.getY() - startDragY);
            setLocation(newX, newY);
            cp.repaint();
            cp.setSave(false);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        onDrag = false;
        cp.adjCanvas();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (cp.tp.tool.equals("frh") && inArea(e.getPoint())) {
            swnBorder = selBorder;
            repaint();
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (cp.tp.tool.equals("frh")) {
            if (this != cp.comp) {
                swnBorder = defBorder;
                repaint();
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (cp.tp.tool.equals("frh")) {
            if (inArea(e.getPoint())) {
                swnBorder = selBorder;
                repaint();
            } else {
                if (this != cp.comp) {
                    swnBorder = defBorder;
                    repaint();
                }
            }
        }
    }
}
