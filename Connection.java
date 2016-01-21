public class Connection {
    
    CanvasPanel cp;
    Input input;
    Output output;
    Line2D.Double[] line;
    Color lineColor;
    boolean isSelect = false;
    
    public Connection(CanvasPanel cp) {
        this.input = null;
        this.output = null;
        this.cp = cp;
    }
    
    public void setInput(Input input) {
        this.input = input;
    }
    
    public void setOutput(Output output) {
        this.output = output;
    }
    
    public void connect(boolean connect) {
        if (connect) {
            if (input.owner.node) {
                input.pair(true);
                input.setAvailable(false);
            }
            output.pair(true);
            output.setAvailable(false);
            input.owner.child.add(output.owner);
            output.owner.parent = input.owner;
            input.owner.lines.add(this);
            output.owner.lines.add(this);
            cp.rootLvl.remove(output.owner);
            output.owner.setRoots(input.owner.roots);
            lineColor = Color.darkGray;
        } else {
            if (input.owner.node) {
                input.pair(false);
                input.setAvailable(true);
            }
            output.pair(false);
            output.setAvailable(true);
            input.owner.child.remove(output.owner);
            output.owner.parent = null;
            input.owner.lines.remove(this);
            output.owner.lines.remove(this);
            cp.rootLvl.add(output.owner);
            output.owner.setRoots(output.owner);
            cp.lines.remove(this);
            cp.setSave(false);
            cp.repaint();
            cp.tp.mp.remove.setEnabled(false);
        }
    }
    
    public boolean isLine(Rectangle r) {
        for (Line2D.Double l : line) {
            if (l.intersects(r)) {
                isSelect = true;
                break;
            } else {
                isSelect = false;
            }
        }
        return isSelect;
    }
    
    public void select(boolean select) {
        if (select) {
            lineColor = Color.green;
            cp.line = this;
            cp.tp.mp.remove.setEnabled(true);
        } else {
            lineColor = Color.darkGray;
            cp.line = null;
            cp.tp.mp.remove.setEnabled(false);
        }
    }
    
    public void paintConnection(Graphics2D g2d) {
        line = new Line2D.Double[3];
        int x1 = output.getXs(), y1 = output.getYs(), x2 = input.getXs(), y2 = input.getYs();
        
        if (x1 == x2 || y1 < y2) {
            line[2] = new Line2D.Double(x1 + (x2 - x1) / 2, y1, x2 - (x2 - x1) / 2, y2);
        } else {
            line[2] = new Line2D.Double(x1, y1 + (y2 - y1) / 2, x2, y2 - (y2 - y1) / 2);
        }
        line[0] = new Line2D.Double(x1, y1, line[2].getX1(), line[2].getY1());
        line[1] = new Line2D.Double(x2, y2, line[2].getX2(), line[2].getY2());
        
        g2d.setColor(lineColor);
        for (Line2D.Double l : line) {
            if (line != null) {
                g2d.setStroke(new BasicStroke(2));
                g2d.draw(l);
            }
        }
    }
}
