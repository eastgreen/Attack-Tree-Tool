public class PropertiesPanel extends JPanel {

    int x = 15, y = 25;
    Component owner;
    JButton save;
    JLabel name, value, rate, phase, input, output, description;
    JTextField[] inputField;
    JTextField nameField, rateField, phaseField, outputField;
    JTextArea descArea;

    public PropertiesPanel() {
        initUI();
    }

    private void initUI() {
        setPreferredSize(new Dimension(250, 200));
        setLayout(null);

        if (owner != null) {
            name = new JLabel("Name :");
            addComp(name);

            nameField = new JTextField(owner.getName());
            addComp(nameField);
            try {
                if (owner.rateVal && owner.phaseVal) {
                    value = new JLabel("Value :");
                    addComp(value);

                    phase = new JLabel("Phase :");
                    phase.setBounds(x + 20, y, 180, 20);
                    add(phase);
                    y += 25;

                    phaseField = new JTextField(Double.toString(owner.phase));
                    phaseField.setBounds(x + 20, y, 180, 20);
                    add(phaseField);
                    y += 25;

                    rate = new JLabel("Rate :");
                    rate.setBounds(x + 20, y, 180, 20);
                    add(rate);
                    y += 25;

                    rateField = new JTextField(Double.toString(owner.rate));
                    rateField.setBounds(x + 20, y, 180, 20);
                    add(rateField);
                    y += 25;
                } else if (owner.rateVal && !owner.phaseVal) {
                    value = new JLabel("Value :");
                    addComp(value);

                    rate = new JLabel("Rate :");
                    rate.setBounds(x + 20, y, 180, 20);
                    add(rate);
                    y += 25;

                    rateField = new JTextField(Double.toString(owner.rate));
                    rateField.setBounds(x + 20, y, 180, 20);
                    add(rateField);
                    y += 25;
                }
            } catch (NumberFormatException e) {
                System.out.println("hehehe");
            }

            if (!owner.child.isEmpty()) {
                input = new JLabel("Input :");
                addComp(input);
                if (owner.child.isEmpty()) {
                    inputField = new JTextField[1];
                    inputField[0] = new JTextField("null");
                    inputField[0].setEditable(false);
                    addComp(inputField[0]);
                } else {
                    inputField = new JTextField[owner.child.size()];
                    for (int i = 0; i < owner.child.size(); i++) {
                        inputField[i] = new JTextField(owner.child.get(i).getName());
                        inputField[i].setEditable(false);
                        addComp(inputField[i]);
                    }
                }
            }

            if (owner.parent != null) {
                output = new JLabel("Output :");
                addComp(output);
                outputField = new JTextField(owner.parent.getName());
                outputField.setEditable(false);
                addComp(outputField);
            }

            description = new JLabel("Description :");
            addComp(description);

            descArea = new JTextArea(owner.description);
            JScrollPane jsp = new JScrollPane(descArea);
            descArea.setBounds(x, y, 200, 100);
            jsp.setBounds(x, y, 200, 100);
            add(jsp);

            save = new JButton("save");
            save.setBounds(140, y + 120, 80, 30);
            save.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    owner.setName(nameField.getText());
                    owner.setDescription(descArea.getText());
                    if (owner.rateVal && owner.phaseVal) {
                        owner.setRate(Double.parseDouble(rateField.getText()));
                        owner.setPhase(Double.parseDouble(phaseField.getText()));
                    } else if (owner.rateVal && !owner.phaseVal) {
                        owner.setRate(Double.parseDouble(rateField.getText()));
                    }
                    owner.repaint();
                    owner.cp.setSave(false);
                }
            });
            add(save);
            setPreferredSize(new Dimension(250, y + 200));
            revalidate();
        } else {
            setPreferredSize(new Dimension(250, 200));
            revalidate();
            name = new JLabel("Properties Panel");
            name.setBounds(20, 30, 200, 20);
            add(name);
        }
    }

    public void setComponent(Component owner) {
        this.owner = owner;
        y = 25;
        removeAll();
        initUI();
        repaint();
    }

    private void addComp(java.awt.Component comp) {
        comp.setBounds(x, y, 200, 20);
        add(comp);
        y += 25;
    }
}
