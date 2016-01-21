public class TabbedPane extends JTabbedPane {

    int sysIndex = -1;
    String tool = "";
    MainPanel mp;

    public TabbedPane(MainPanel mp) {
        this.mp = mp;
        //
        addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                JScrollPane jsp = (JScrollPane) getSelectedComponent();
                JViewport view = jsp.getViewport();
                CanvasPanel c = (CanvasPanel) view.getView();
                if (c.comp != null || c.line != null) {
                    rmvEnabled(true);
                } else {
                    rmvEnabled(false);
                }
            }
        });
    }

    public void newCanvas(int index) {
        sysIndex += index;
        CanvasPanel cp = new CanvasPanel(this);
        cp.setName("system " + sysIndex);
        cp.addRoot(new Point(cp.getPreferredSize().width / 2, 10), "system " + sysIndex);
        addTab("system " + sysIndex, new JScrollPane(cp));
        setSelectedIndex(getComponentCount() - 1);
    }

    public void clsCanvas() {
        if (getComponentCount() == 1) {
            newCanvas(0);
            remove(getComponent(0));
        } else {
            remove(getSelectedComponent());
        }
    }

    public void setTool(String tool) {
        this.tool = tool;
    }

    private void rmvEnabled(boolean able) {
        mp.remove.setEnabled(able);
    }

    public void writeXML(CanvasPanel canvas, File file) throws
            ParserConfigurationException, TransformerConfigurationException,
            TransformerException, IOException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document xmlDoc = docBuilder.newDocument();
        // Element init
        Element rootElement = xmlDoc.createElement("AttackTree");
        xmlDoc.appendChild(rootElement);

        Element canvasElm = xmlDoc.createElement("canvas");
        Element compIndex = xmlDoc.createElement("compIndex");
        compIndex.setTextContent(Integer.toString(canvas.compIndex));
        canvasElm.appendChild(compIndex);
        Element leafIndex = xmlDoc.createElement("leafIndex");
        leafIndex.setTextContent(Integer.toString(canvas.leafIndex));
        canvasElm.appendChild(leafIndex);
        Element opIndex = xmlDoc.createElement("opIndex");
        opIndex.setTextContent(Integer.toString(canvas.opIndex));
        canvasElm.appendChild(opIndex);
        rootElement.appendChild(canvasElm);

        for (int i = 0; i < canvas.compList.size(); i++) {
            rootElement.appendChild(compElement(canvas.compList.get(i), xmlDoc));
        }

        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(xmlDoc);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);
        canvas.setSave(true);
    }

    private Element compElement(Component c, Document xmlDoc) {
        Element comp = xmlDoc.createElement("component");
        comp.setAttribute("id", c.id);

        Element name = xmlDoc.createElement("name");
        name.appendChild(xmlDoc.createTextNode(c.getName()));
        comp.appendChild(name);

        if (c.rateVal && c.phaseVal) {
            Element value = xmlDoc.createElement("value");

            Element phase = xmlDoc.createElement("phase");
            phase.appendChild(xmlDoc.createTextNode(Double.toString(c.phase)));
            value.appendChild(phase);

            Element rate = xmlDoc.createElement("rate");
            rate.appendChild(xmlDoc.createTextNode(Double.toString(c.rate)));
            value.appendChild(rate);
            comp.appendChild(value);

        } else if (c.rateVal && !c.phaseVal) {
            Element value = xmlDoc.createElement("value");

            Element rate = xmlDoc.createElement("rate");
            rate.appendChild(xmlDoc.createTextNode(Double.toString(c.rate)));
            value.appendChild(rate);
            comp.appendChild(value);
        }

        Element xLoc = xmlDoc.createElement("xLoc");
        xLoc.appendChild(xmlDoc.createTextNode(Integer.toString(c.getX())));
        comp.appendChild(xLoc);

        Element yLoc = xmlDoc.createElement("yLoc");
        yLoc.appendChild(xmlDoc.createTextNode(Integer.toString(c.getY())));
        comp.appendChild(yLoc);

        if (!c.id.contains("ern") || !c.id.contains("exn")) {
            if (!c.child.isEmpty()) {
                if (!c.node) {
                    Element input = xmlDoc.createElement("inputList");
                    Element[] inputIndex = new Element[c.child.size()];
                    for (int i = 0; i < c.child.size(); i++) {
                        inputIndex[i] = xmlDoc.createElement("input");
                        inputIndex[i].appendChild(xmlDoc.createTextNode(
                                c.child.get(i).id));
                        input.appendChild(inputIndex[i]);
                    }
                    comp.appendChild(input);
                } else {
                    Element input = xmlDoc.createElement("input");
                    input.appendChild(xmlDoc.createTextNode(
                            c.child.get(0).id));
                    comp.appendChild(input);
                }
            } else {
                Element input = xmlDoc.createElement("input");
                input.appendChild(xmlDoc.createTextNode("null"));
                comp.appendChild(input);
            }
        }

        if (!c.id.equals("root")) {
            if (c.parent != null) {
                Element output = xmlDoc.createElement("output");
                output.appendChild(xmlDoc.createTextNode(
                        c.parent.id));
                comp.appendChild(output);
            } else {
                Element output = xmlDoc.createElement("output");
                output.appendChild(xmlDoc.createTextNode("null"));
                comp.appendChild(output);
            }
        }

        Element description = xmlDoc.createElement("description");
        description.appendChild(xmlDoc.createTextNode(c.description));
        comp.appendChild(description);

        return comp;
    }

    public void readXML(File file) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document xmlDoc = docBuilder.parse(file);
        xmlDoc.getDocumentElement().normalize();
        NodeList canvasNL = xmlDoc.getElementsByTagName("AttackTree");
        Node node = canvasNL.item(0);
        if (node != null) {
            CanvasPanel canvas = new CanvasPanel(this);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                int compIndex = Integer.parseInt(element.getElementsByTagName("compIndex").item(0).getTextContent());
                int leafIndex = Integer.parseInt(element.getElementsByTagName("leafIndex").item(0).getTextContent());
                int opIndex = Integer.parseInt(element.getElementsByTagName("opIndex").item(0).getTextContent());
                canvas.setIndex(compIndex, leafIndex, opIndex);
            }
            drawComponent(canvas, xmlDoc);
            drawConnection(canvas, xmlDoc);
            canvas.setName(file.getName());
            canvas.setSaveFileLocation(file);
            canvas.adjCanvas();
            addTab(file.getName(), new JScrollPane(canvas));
            setSelectedIndex(getComponentCount() - 1);
        } else {
            String message = "Missing root element <AttackTree>";
            JOptionPane.showMessageDialog(mp, message, "XML Document Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void drawComponent(CanvasPanel canvas, Document xmlDoc) {
        NodeList components = xmlDoc.getElementsByTagName("component");
        for (int i = 0; i < components.getLength(); i++) {
            Node node = components.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String id = element.getAttribute("id");
                String name = element.getElementsByTagName("name").item(0).getTextContent();
                int x = Integer.parseInt(element.getElementsByTagName("xLoc").item(0).getTextContent());
                int y = Integer.parseInt(element.getElementsByTagName("yLoc").item(0).getTextContent());
                String desc = element.getElementsByTagName("description").item(0).getTextContent();
                NodeList value = element.getElementsByTagName("value");
                if (value.getLength() != 0) {
                    Node valNode = value.item(0);
                    if (valNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element valElement = (Element) valNode;
                        double rate = Double.parseDouble(valElement.getElementsByTagName("rate").item(0).getTextContent());
                        if (valElement.getElementsByTagName("phase").getLength() != 0) {
                            double phase = Double.parseDouble(valElement.getElementsByTagName("phase").item(0).getTextContent());
                            NodeErlang ern = new NodeErlang(canvas, id);
                            ern.setName(name);
                            ern.setLoc(new Point(x, y));
                            ern.setPhase(phase);
                            ern.setRate(rate);
                            ern.setDescription(desc);
                        } else {
                            NodeExponential exn = new NodeExponential(canvas, id);
                            exn.setName(name);
                            exn.setLoc(new Point(x, y));
                            exn.setRate(rate);
                            exn.setDescription(desc);
                        }
                    }
                } else {
                    switch (id.substring(0, 3)) {
                        case "ang":
                            OpMax ang = new OpMax(canvas, id);
                            ang.setName(name);
                            ang.setLoc(new Point(x, y));
                            ang.setDescription(desc);
                            break;
                        case "org":
                            OpMin org = new OpMin(canvas, id);
                            org.setName(name);
                            org.setLoc(new Point(x, y));
                            org.setDescription(desc);
                            break;
                        case "cog":
                            OpCon cog = new OpCon(canvas, id);
                            cog.setName(name);
                            cog.setLoc(new Point(x, y));
                            cog.setDescription(desc);
                            break;
                        case "roo":
                            NodeRoot root = new NodeRoot(canvas, id);
                            root.setName(name);
                            root.setLoc(new Point(x, y));
                            break;
                    }
                }
            }
        }
    }

    private void drawConnection(CanvasPanel cp, Document xmlDoc) {
        NodeList components = xmlDoc.getElementsByTagName("component");
        for (int i = 0; i < components.getLength(); i++) {
            Node node = components.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                NodeList inputList = element.getElementsByTagName("inputList");
                if (inputList.getLength() != 0) {
                    Node nodeInput = inputList.item(0);
                    if (nodeInput.getNodeType() == Node.ELEMENT_NODE) {
                        Element inputs = (Element) nodeInput;
                        NodeList input = inputs.getElementsByTagName("input");
                        for (int j = 0; j < input.getLength(); j++) {
                            Node inputNode = input.item(j);
                            cp.connection.setInput(cp.getComponent(element.getAttribute("id")).input);
                            cp.connection.setOutput(cp.getComponent(inputNode.getTextContent()).output);
                            cp.newConnection();
                        }
                    }
                } else {
                    NodeList input = element.getElementsByTagName("input");
                    Node inputNode = input.item(0);
                    if (!inputNode.getTextContent().equals("null")) {
                            cp.connection.setInput(cp.getComponent(element.getAttribute("id")).input);
                            cp.connection.setOutput(cp.getComponent(inputNode.getTextContent()).output);
                            cp.newConnection();
                    }
                }
            }
        }
    }
    
    private void cekRoot(Document xmlDoc){
        NodeList components = xmlDoc.getElementsByTagName("component");
        for(int i = 0; i < components.getLength(); i++){
            
        }
    }
    
    private void cekLeaf(Document XMLDoc){
        
    }
    
    private void cekOperator(Document XMLDoc){
        
    }
}
