public class MainPanel extends JPanel implements ActionListener {

    TabbedPane tp = new TabbedPane(this);
    ExpressionPanel ep = new ExpressionPanel(this);
    PropertiesPanel pp = new PropertiesPanel();
    JToolBar menu = new JToolBar();
    JFileChooser fc = new JFileChooser();
    JButton newFile, openFile, saveFile,
            freeHand, addErlNode, addExpNode,
            addMaxGate, addMinGate, addConGate,
            addConnection, remove, generate, layout;

    public MainPanel() {
        initUI();
    }

    private void initUI() {
        setPreferredSize(new Dimension(1200, 700));
        setLayout(new BorderLayout(2, 2));

        //Create a toolbar
        menu.setFloatable(false);

        newFile = new JButton(new ImageIcon("icon\\newFile.png"));
        newFile.setToolTipText("<html><p style='font-style:bold;'>New"
                + "<br><p style='font-style:italic;'>Create new canvas</html>");
        newFile.addActionListener(this);
        menu.add(newFile);

        openFile = new JButton(new ImageIcon("icon\\openFile.png"));
        openFile.setToolTipText("<html><p style='font-style:bold;'>Open"
                + "<br><p style='font-style:italic;'>Open saved canvas</html>");
        openFile.addActionListener(this);
        menu.add(openFile);

        saveFile = new JButton(new ImageIcon("icon\\saveFile.png"));
        saveFile.setToolTipText("<html><p style='font-style:bold;'>Save"
                + "<br><p style='font-style:italic;'>Save selected canvas</html>");
        saveFile.addActionListener(this);
        menu.add(saveFile);

        menu.addSeparator();

        addExpNode = new JButton(new ImageIcon("icon\\expNode.png"));
        addExpNode.setToolTipText("<html><p style='font-style:bold;'>Exponential Node"
                + "<br><p style='font-style:italic;'>Represent exponent type event with rate and phase value</html>");
        addExpNode.addActionListener(this);
        menu.add(addExpNode);

        addErlNode = new JButton(new ImageIcon("icon\\erlNode.png"));
        addErlNode.setToolTipText("<html><p style='font-style:bold;'>Erlang Node"
                + "<br><p style='font-style:italic;'>Represent erlang type event with rate value</html>");
        addErlNode.addActionListener(this);
        menu.add(addErlNode);

        addMaxGate = new JButton(new ImageIcon("icon\\and.png"));
        addMaxGate.setToolTipText("<html><p style='font-style:bold;'>Maximum Operator");
        addMaxGate.addActionListener(this);
        menu.add(addMaxGate);

        addMinGate = new JButton(new ImageIcon("icon\\orGate.png"));
        addMinGate.setToolTipText("<html><p style='font-style:bold;'>Minimum Operator");
        addMinGate.addActionListener(this);
        menu.add(addMinGate);

        addConGate = new JButton(new ImageIcon("icon\\conGate.png"));
        addConGate.setToolTipText("<html><p style='font-style:bold;'>Convolution Operator");
        addConGate.addActionListener(this);
        menu.add(addConGate);

        menu.addSeparator();

        freeHand = new JButton(new ImageIcon("icon\\freeHand.png"));
        freeHand.setToolTipText("<html><p style='font-style:bold;'>Free Hand"
                + "<br><p style='font-style:italic;'>Used to move and select model's parts</html>");
        freeHand.addActionListener(this);
        menu.add(freeHand);

        addConnection = new JButton(new ImageIcon("icon\\connect.png"));
        addConnection.setToolTipText("<html><p style='font-style:bold;'>Connection"
                + "<br><p style='font-style:italic;'>Select both input and output from different components to connect</html>");
        addConnection.addActionListener(this);
        menu.add(addConnection);

        remove = new JButton(new ImageIcon("icon\\remove.png"));
        remove.setToolTipText("<html><p style='font-style:bold;'>Remove"
                + "<br><p style='font-style:italic;'>Remove the current selection model's parts</html>");
        remove.addActionListener(this);
        remove.setEnabled(false);
        menu.add(remove);

        generate = new JButton(new ImageIcon("icon\\generate.png"));
        generate.setToolTipText("<html><p style='font-style:bold;'>Generate"
                + "<br><p style = 'font-style:italic;'>Generate model expression</html>");
        generate.addActionListener(this);
        menu.add(generate);

        layout = new JButton(new ImageIcon("icon\\layout.png"));
        layout.setToolTipText("<html><p style='font-style:bold;'>Auto Layout"
                + "<br><p style = 'font-style:italic;'>Arrange model with tree layout</html>");
        layout.addActionListener(this);
        menu.add(layout);

        add(menu, BorderLayout.NORTH);

        tp.newCanvas(1);
        JSplitPane sp1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(pp), tp);
        sp1.setDividerLocation(250);
        JSplitPane sp2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, sp1, ep);
        sp2.setDividerLocation(550);

        add(sp2, BorderLayout.CENTER);
    }

    public void saveFile() {
        JScrollPane jsp = (JScrollPane) tp.getSelectedComponent();
        JViewport view = jsp.getViewport();
        CanvasPanel c = (CanvasPanel) view.getView();
        if (c.sLoc != null) {
            try {
                try {
                    tp.writeXML(c, c.sLoc);
                } catch (TransformerConfigurationException | IOException ex) {
                    Logger.getLogger(MainPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (ParserConfigurationException | TransformerException ex) {
                System.out.println("nxkanxka" + ex.getMessage());
            }
        } else {
            fc.setFileFilter(new FileNameExtensionFilter("eXtensible Markup Language (*.xml)", "xml"));
            int returnVal = fc.showSaveDialog(this);
            System.getProperty("user.dir");
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                String fileName = file.toString();

                //Make sure input in JFileChooser end with .xml
                if (!fileName.endsWith(".xml")) {
                    fileName += ".xml";
                    file = new File(fileName);
                }

                System.out.println(file.getAbsolutePath());
                try {
                    try {
                        tp.writeXML(c, file);
                    } catch (TransformerConfigurationException | IOException ex) {
                        Logger.getLogger(MainPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    c.setName(file.getName());
                    c.setSaveFileLocation(file);
                    tp.setTitleAt(tp.getSelectedIndex(), file.getName());
                } catch (ParserConfigurationException | TransformerException ex) {
                    System.out.println("nxkanxka" + ex.getMessage());
                }
            }
        }
    }

    public void openFile() {
        fc.setFileFilter(new FileNameExtensionFilter("eXtensible Markup Language (*.xml)", "xml"));
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            String fileName = file.toString();
            if (!fileName.endsWith(".xml")) {
                fileName += ".xml";
                file = new File(fileName);
            }

            try {
                //Make sure same XML not open twice
                for (int i = 0; i < tp.getComponentCount(); i++) {
                    JScrollPane jsp = (JScrollPane) tp.getComponentAt(i);
                    JViewport view = jsp.getViewport();
                    CanvasPanel c = (CanvasPanel) view.getView();
                    if (c.sLoc != null
                            && c.sLoc.equals(file)) {
                        tp.setSelectedIndex(i);
                        return;
                    }
                }
                tp.readXML(file);
                if (tp.getComponentCount() == 2) {
                    if (!tp.getTitleAt(0).endsWith(".xml") && !tp.getTitleAt(0).endsWith("*")) {
                        tp.remove(tp.getComponent(0));
                    }
                }

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "File Not Found", JOptionPane.ERROR_MESSAGE);
            } catch (SAXException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "XML Format Error", JOptionPane.ERROR_MESSAGE);
            } catch (ParserConfigurationException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Parser Configuration Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    public void remove() {
        JScrollPane jsp = (JScrollPane) tp.getSelectedComponent();
        JViewport view = jsp.getViewport();
        CanvasPanel c = (CanvasPanel) view.getView();
        if (c.comp != null) {
            c.comp.remove();
        } else if (c.line != null) {
            c.line.connect(false);
        }
        c.repaint();
    }

    public void generate() {
        JScrollPane jsp = (JScrollPane) tp.getSelectedComponent();
        JViewport view = jsp.getViewport();
        CanvasPanel c = (CanvasPanel) view.getView();
        c.genExps();
        ep.area.repaint();
    }

    private void autoLayout() {
        JScrollPane jsp = (JScrollPane) tp.getSelectedComponent();
        JViewport view = jsp.getViewport();
        CanvasPanel c = (CanvasPanel) view.getView();
        c.setLayout();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == newFile) {
            tp.newCanvas(1);
        } else if (source == saveFile) {
            saveFile();
        } else if (source == openFile) {
            openFile();
        } else if (source == addExpNode) {
            tp.setTool("exn");
        } else if (source == addErlNode) {
            tp.setTool("ern");
        } else if (source == addMaxGate) {
            tp.setTool("ang");
        } else if (source == addMinGate) {
            tp.setTool("org");
        } else if (source == addConGate) {
            tp.setTool("cog");
        } else if (source == freeHand) {
            tp.setTool("frh");
        } else if (source == addConnection) {
            tp.setTool("con");
        } else if (source == remove) {
            remove();
        } else if (source.equals(generate)) {
            generate();
        } else if (source.equals(layout)) {
            autoLayout();
        }
    }
}
