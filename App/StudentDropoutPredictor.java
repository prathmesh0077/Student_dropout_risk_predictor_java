import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDropoutPredictor extends JFrame {

    // --- Design Constants ---
    private static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 28);
    private static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font FONT_LARGE = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font FONT_PLAIN = new Font("Segoe UI", Font.PLAIN, 16);
    private static final Color COL_BG = new Color(240, 244, 248);
    private static final Color COL_PRIMARY = new Color(66, 133, 244);
    private static final Color COL_CARD = Color.WHITE;

    // --- Data ---
    private final List<Student> studentList = new ArrayList<>();
    private DefaultTableModel tableModel;

    // --- UI References ---
    private JTabbedPane tabbedPane;
    private JLabel lblTotal, lblHigh, lblMed, lblLow;
    private PieChartPanel pieChartPanel;

    public StudentDropoutPredictor() {
        setTitle("Student Dropout Risk Predictor");
        setSize(1200, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setBackground(COL_BG);

        // Custom Tabbed Pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(FONT_HEADER);
        tabbedPane.setBackground(Color.WHITE);

        tabbedPane.addTab("  Dashboard  ", createDashboardPanel());
        tabbedPane.addTab("  Add Student  ", createAddStudentPanel());
        tabbedPane.addTab("  Student List  ", createViewStudentsPanel());

        add(tabbedPane);
    }

    // ==========================================
    // 1. DASHBOARD
    // ==========================================
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(COL_BG);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("Risk Overview", SwingConstants.LEFT);
        title.setFont(FONT_TITLE);
        panel.add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 30, 0));
        centerPanel.setOpaque(false);

        // Stats
        JPanel statsGrid = new JPanel(new GridLayout(2, 2, 20, 20));
        statsGrid.setOpaque(false);
        lblTotal = createStatCard(statsGrid, "Total Students", "0", Color.DARK_GRAY);
        lblHigh = createStatCard(statsGrid, "High Risk", "0", new Color(231, 76, 60));
        lblMed = createStatCard(statsGrid, "Medium Risk", "0", new Color(241, 196, 15));
        lblLow = createStatCard(statsGrid, "Safe Zone", "0", new Color(46, 204, 113));
        centerPanel.add(statsGrid);

        // Chart
        pieChartPanel = new PieChartPanel();
        RoundedPanel chartContainer = new RoundedPanel(30, COL_CARD);
        chartContainer.setLayout(new BorderLayout());
        chartContainer.setBorder(new EmptyBorder(20, 20, 20, 20));
        JLabel chartLabel = new JLabel("Distribution", SwingConstants.CENTER);
        chartLabel.setFont(FONT_HEADER);
        chartContainer.add(chartLabel, BorderLayout.NORTH);
        chartContainer.add(pieChartPanel, BorderLayout.CENTER);
        centerPanel.add(chartContainer);

        panel.add(centerPanel, BorderLayout.CENTER);
        return panel;
    }

    private JLabel createStatCard(JPanel parent, String title, String value, Color color) {
        RoundedPanel card = new RoundedPanel(25, COL_CARD);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(20, 25, 20, 25));
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(FONT_PLAIN);
        lblTitle.setForeground(Color.GRAY);
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 48));
        lblValue.setForeground(color);
        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);
        parent.add(card);
        return lblValue;
    }

    // ==========================================
    // 2. ADD STUDENT TAB
    // ==========================================
    private JPanel createAddStudentPanel() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(COL_BG);

        RoundedPanel form = new RoundedPanel(40, COL_CARD);
        form.setLayout(new GridBagLayout());
        form.setBorder(new EmptyBorder(40, 50, 40, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Inputs
        JTextField txtName = createStyledField();
        JTextField txtContact = createStyledField();
        SliderGroup attendanceGroup = new SliderGroup(0, 100, 75);
        SliderGroup marksGroup = new SliderGroup(0, 100, 60);
        JCheckBox chkFees = new JCheckBox(" Fees Fully Paid?");
        chkFees.setFont(FONT_HEADER);
        chkFees.setBackground(COL_CARD);

        // Layout
        int row = 0;
        JLabel header = new JLabel("New Student Record");
        header.setFont(FONT_TITLE);
        gbc.gridwidth = 2; gbc.gridx = 0; gbc.gridy = row++; gbc.anchor = GridBagConstraints.CENTER;
        form.add(header, gbc);

        gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST;
        addFormRow(form, "Student Name:", txtName, gbc, row++);
        addFormRow(form, "Contact Number:", txtContact, gbc, row++);

        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2; gbc.insets = new Insets(20, 10, 5, 10);
        JLabel lblAtt = new JLabel("Attendance (%)"); lblAtt.setFont(FONT_LARGE);
        form.add(lblAtt, gbc);
        gbc.gridy = row++; gbc.insets = new Insets(0, 10, 10, 10);
        form.add(attendanceGroup.panel, gbc);

        gbc.gridy = row++; gbc.insets = new Insets(15, 10, 5, 10);
        JLabel lblMarks = new JLabel("Marks (Avg %)"); lblMarks.setFont(FONT_LARGE);
        form.add(lblMarks, gbc);
        gbc.gridy = row++; gbc.insets = new Insets(0, 10, 10, 10);
        form.add(marksGroup.panel, gbc);

        gbc.gridy = row++;
        form.add(chkFees, gbc);

        RoundedButton btnSave = new RoundedButton("Save Record");
        btnSave.setBackground(COL_PRIMARY);
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(FONT_HEADER);
        btnSave.setPreferredSize(new Dimension(200, 50));
        gbc.gridy = row++; gbc.insets = new Insets(30, 10, 10, 10); gbc.anchor = GridBagConstraints.CENTER;
        form.add(btnSave, gbc);

        mainPanel.add(form);

        btnSave.addActionListener(e -> {
            if (txtName.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this, "Name is required!"); return; }
            Student s = new Student(txtName.getText(), txtContact.getText(), attendanceGroup.getValue(), marksGroup.getValue(), chkFees.isSelected());
            studentList.add(s);
            refreshAll();
            txtName.setText(""); txtContact.setText(""); attendanceGroup.reset(75); marksGroup.reset(60); chkFees.setSelected(false);
            JOptionPane.showMessageDialog(this, "Student Added!");
            tabbedPane.setSelectedIndex(2);
        });

        return mainPanel;
    }

    // ==========================================
    // 3. STUDENT LIST TAB
    // ==========================================
    private JPanel createViewStudentsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COL_BG);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        String[] cols = {"Name", "Contact", "Att.", "Marks", "Paid", "Risk", "Cat", "Msg", "Edit"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int column) { return column >= 7; }
        };

        JTable table = new JTable(tableModel);
        table.setRowHeight(50);
        table.setFont(FONT_PLAIN);
        table.getTableHeader().setFont(FONT_HEADER);
        table.getTableHeader().setBackground(new Color(230, 230, 230));
        table.setShowVerticalLines(false);

        // Center Alignment
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 2; i < 7; i++) table.getColumnModel().getColumn(i).setCellRenderer(center);

        // Risk Coloring
        table.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
                Component comp = super.getTableCellRendererComponent(t, v, s, f, r, c);
                String cat = (String) v;
                if (!s) {
                    if ("High".equals(cat)) comp.setBackground(new Color(255, 235, 238));
                    else if ("Medium".equals(cat)) comp.setBackground(new Color(255, 248, 225));
                    else comp.setBackground(new Color(232, 245, 233));
                    setHorizontalAlignment(JLabel.CENTER);
                }
                return comp;
            }
        });

        // Icons for Buttons
        table.getColumn("Msg").setCellRenderer(new ButtonRenderer(new VectorIcon(VectorIcon.Type.ENVELOPE, 24, Color.WHITE), new Color(100, 181, 246)));
        table.getColumn("Msg").setCellEditor(new ButtonEditor(new JCheckBox(), "Msg"));

        table.getColumn("Edit").setCellRenderer(new ButtonRenderer(new VectorIcon(VectorIcon.Type.PENCIL, 22, Color.WHITE), new Color(255, 183, 77)));
        table.getColumn("Edit").setCellEditor(new ButtonEditor(new JCheckBox(), "Edit"));

        table.getColumnModel().getColumn(7).setMaxWidth(70);
        table.getColumnModel().getColumn(8).setMaxWidth(70);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        RoundedPanel listCard = new RoundedPanel(25, COL_CARD);
        listCard.setLayout(new BorderLayout());
        listCard.setBorder(new EmptyBorder(15, 15, 15, 15));
        listCard.add(scroll, BorderLayout.CENTER);
        panel.add(listCard, BorderLayout.CENTER);
        return panel;
    }

    private void refreshAll() {
        long h = studentList.stream().filter(s -> s.getRiskCategory().equals("High")).count();
        long m = studentList.stream().filter(s -> s.getRiskCategory().equals("Medium")).count();
        long l = studentList.stream().filter(s -> s.getRiskCategory().equals("Low")).count();
        lblTotal.setText(String.valueOf(studentList.size()));
        lblHigh.setText(String.valueOf(h));
        lblMed.setText(String.valueOf(m));
        lblLow.setText(String.valueOf(l));
        pieChartPanel.updateData((int) h, (int) m, (int) l);

        tableModel.setRowCount(0);
        for (Student s : studentList) {
            tableModel.addRow(new Object[]{s.name, s.contact, (int) s.attendance + "%", (int) s.marks + "%", s.feesPaid ? "Yes" : "No", String.format("%.1f", s.riskScore), s.getRiskCategory(), "", ""});
        }
    }

    // ==========================================
    // 4. BEAUTIFUL EDIT DIALOG
    // ==========================================
    private void openEditDialog(int rowIndex) {
        Student s = studentList.get(rowIndex);
        JDialog d = new JDialog(this, "Edit Student Details", true);
        d.setSize(500, 600);
        d.setLocationRelativeTo(this);
        d.setLayout(new BorderLayout());
        d.setBackground(COL_BG);

        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(COL_BG);
        content.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        RoundedPanel card = new RoundedPanel(30, Color.WHITE);
        card.setLayout(new GridBagLayout());
        card.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        // Title
        JLabel title = new JLabel("Edit Student");
        title.setFont(FONT_TITLE);
        gbc.gridwidth = 2; gbc.gridx = 0; gbc.gridy = row++;
        card.add(title, gbc);

        // Fields
        JTextField tName = createStyledField(); tName.setText(s.name);
        JTextField tCont = createStyledField(); tCont.setText(s.contact);
        gbc.gridwidth = 1;
        addFormRow(card, "Name:", tName, gbc, row++);
        addFormRow(card, "Contact:", tCont, gbc, row++);

        // Sliders
        SliderGroup attGroup = new SliderGroup(0, 100, (int)s.attendance);
        SliderGroup mrkGroup = new SliderGroup(0, 100, (int)s.marks);
        
        gbc.gridx=0; gbc.gridy=row++; gbc.gridwidth=2; 
        JLabel la = new JLabel("Attendance"); la.setFont(FONT_LARGE); card.add(la, gbc);
        gbc.gridy=row++; card.add(attGroup.panel, gbc);

        gbc.gridy=row++; 
        JLabel lm = new JLabel("Marks"); lm.setFont(FONT_LARGE); card.add(lm, gbc);
        gbc.gridy=row++; card.add(mrkGroup.panel, gbc);

        JCheckBox cFee = new JCheckBox("Fees Paid?", s.feesPaid);
        cFee.setFont(FONT_HEADER); cFee.setBackground(Color.WHITE);
        gbc.gridy=row++; card.add(cFee, gbc);

        RoundedButton btnUpdate = new RoundedButton("Update Record");
        btnUpdate.setBackground(COL_PRIMARY); btnUpdate.setForeground(Color.WHITE);
        btnUpdate.setFont(FONT_HEADER); btnUpdate.setPreferredSize(new Dimension(150, 45));
        
        btnUpdate.addActionListener(ev -> {
            s.name = tName.getText();
            s.contact = tCont.getText();
            s.attendance = attGroup.getValue();
            s.marks = mrkGroup.getValue();
            s.feesPaid = cFee.isSelected();
            s.calculateRisk();
            refreshAll();
            d.dispose();
        });

        gbc.gridy=row++; gbc.anchor = GridBagConstraints.CENTER;
        card.add(btnUpdate, gbc);
        
        content.add(card);
        d.add(content);
        d.setVisible(true);
    }

    private void openMessageDialog(int rowIndex) {
        Student s = studentList.get(rowIndex);
        String msg = JOptionPane.showInputDialog(this, "Message to " + s.name + ":", "Risk Alert: Score " + String.format("%.1f", s.riskScore));
        if (msg != null) JOptionPane.showMessageDialog(this, "Message sent to " + s.contact);
    }

    // ==========================================
    // HELPERS & COMPONENTS
    // ==========================================
    
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new StudentDropoutPredictor().setVisible(true));
    }

    private void addFormRow(JPanel p, String label, Component c, GridBagConstraints gbc, int row) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0; gbc.gridwidth = 1;
        JLabel l = new JLabel(label); l.setFont(FONT_PLAIN); p.add(l, gbc);
        gbc.gridx = 1; gbc.weightx = 1; p.add(c, gbc);
    }

    private JTextField createStyledField() {
        JTextField f = new JTextField(15);
        f.setFont(FONT_PLAIN);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1), BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        return f;
    }

    static class Student {
        String name, contact; double attendance, marks, riskScore; boolean feesPaid;
        Student(String n, String c, double a, double m, boolean f) { name=n; contact=c; attendance=a; marks=m; feesPaid=f; calculateRisk(); }
        void calculateRisk() { riskScore = (0.50 * (100-attendance)) + (0.35 * (100-marks)) + (0.15 * (feesPaid?0:100)); }
        String getRiskCategory() { return riskScore >= 60 ? "High" : riskScore >= 30 ? "Medium" : "Low"; }
    }

    // --- CUSTOM VECTOR ICONS (SVG-like) ---
    static class VectorIcon implements Icon {
        enum Type { ENVELOPE, PENCIL }
        private Type type; private int size; private Color color;
        VectorIcon(Type t, int s, Color c) { type=t; size=s; color=c; }
        public int getIconWidth() { return size; }
        public int getIconHeight() { return size; }
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            if (type == Type.ENVELOPE) {
                int pad = size/6;
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(x+pad, y+pad+2, size-2*pad, size-2*pad-2, 4, 4);
                g2.drawLine(x+pad, y+pad+2, x+size/2, y+size/2+2);
                g2.drawLine(x+size-pad, y+pad+2, x+size/2, y+size/2+2);
            } else if (type == Type.PENCIL) {
                AffineTransform old = g2.getTransform();
                g2.rotate(Math.toRadians(45), x+size/2.0, y+size/2.0);
                g2.fillRect(x+size/2-2, y+4, 4, size-10); // Body
                g2.fillPolygon(new int[]{x+size/2-2, x+size/2+2, x+size/2}, new int[]{y+size-6, y+size-6, y+size}, 3); // Tip
                g2.setTransform(old);
            }
            g2.dispose();
        }
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        private Color c;
        ButtonRenderer(Icon icon, Color c) { setIcon(icon); this.c=c; setOpaque(true); setBorderPainted(false); }
        public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int co) {
            setBackground(c); return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        protected JButton btn; private String type; private int row;
        ButtonEditor(JCheckBox cb, String type) {
            super(cb); this.type=type; btn = new JButton(); btn.setOpaque(true);
            btn.addActionListener(e -> fireEditingStopped());
        }
        public Component getTableCellEditorComponent(JTable t, Object v, boolean s, int r, int c) {
            row = r; 
            if(type.equals("Msg")) btn.setIcon(new VectorIcon(VectorIcon.Type.ENVELOPE, 24, Color.WHITE));
            else btn.setIcon(new VectorIcon(VectorIcon.Type.PENCIL, 22, Color.WHITE));
            btn.setBackground(type.equals("Msg") ? new Color(100, 181, 246) : new Color(255, 183, 77));
            return btn;
        }
        public Object getCellEditorValue() {
            if (type.equals("Msg")) openMessageDialog(row); else openEditDialog(row);
            return "";
        }
    }

    static class SliderGroup {
        JPanel panel; JSlider slider; JTextField field;
        SliderGroup(int min, int max, int val) {
            panel = new JPanel(new BorderLayout(15, 0)); panel.setOpaque(false);
            slider = new JSlider(min, max, val); slider.setBackground(Color.WHITE);
            field = new JTextField(String.valueOf(val), 3); field.setFont(FONT_HEADER); field.setHorizontalAlignment(JTextField.CENTER);
            slider.addChangeListener(e -> field.setText(String.valueOf(slider.getValue())));
            field.addActionListener(e -> { try { slider.setValue(Integer.parseInt(field.getText())); } catch(Exception x){} });
            panel.add(slider, BorderLayout.CENTER); panel.add(field, BorderLayout.EAST);
        }
        int getValue() { return slider.getValue(); }
        void reset(int v) { slider.setValue(v); field.setText(String.valueOf(v)); }
    }

    static class RoundedPanel extends JPanel {
        private int r; private Color c;
        RoundedPanel(int r, Color c) { this.r=r; this.c=c; setOpaque(false); }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D)g; g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c); g2.fillRoundRect(0, 0, getWidth(), getHeight(), r, r);
            super.paintComponent(g);
        }
    }
    
    static class RoundedButton extends JButton {
        RoundedButton(String t) { super(t); setContentAreaFilled(false); setFocusPainted(false); setBorderPainted(false); }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D)g; g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if(getModel().isPressed()) g2.setColor(getBackground().darker()); else g2.setColor(getBackground());
            g2.fillRoundRect(0,0,getWidth(),getHeight(),25,25);
            super.paintComponent(g);
        }
    }

    static class PieChartPanel extends JPanel {
        private int h, m, l;
        void updateData(int h, int m, int l) { this.h=h; this.m=m; this.l=l; repaint(); }
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); int t = h+m+l; if(t==0) return;
            Graphics2D g2 = (Graphics2D)g; g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int d = Math.min(getWidth(), getHeight()) - 20, x = (getWidth()-d)/2, y = (getHeight()-d)/2, s = 90;
            draw(g2, x, y, d, s, h, t, new Color(231, 76, 60)); s+=arc(h,t);
            draw(g2, x, y, d, s, m, t, new Color(241, 196, 15)); s+=arc(m,t);
            draw(g2, x, y, d, s, l, t, new Color(46, 204, 113));
        }
        void draw(Graphics2D g, int x, int y, int d, int s, int v, int t, Color c) { g.setColor(c); g.fillArc(x,y,d,d,s,arc(v,t)); }
        int arc(int v, int t) { return (int)Math.round((v/(double)t)*360); }
    }
}