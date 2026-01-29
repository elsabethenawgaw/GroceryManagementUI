// GroceryManagementUI.java
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class GroceryManagementUI extends JFrame {

    private ArrayList<Product> products = new ArrayList<>();
    private ArrayList<Customer> customers = new ArrayList<>();
    private ArrayList<Sale> sales = new ArrayList<>();

    private JTextField productNameField, productPriceField, productStockField;
    private DefaultTableModel productTableModel;
    private JTable productTable;
    private JComboBox<String> productDropdown;

    private JTextField customerNameField, customerPhoneField;
    private DefaultTableModel customerTableModel;
    private JTable customerTable;
    private JComboBox<String> customerDropdown;

    private JTextField saleQuantityField;
    private DefaultTableModel salesTableModel;
    private JTable salesTable;
    private JTextArea reportArea;

    public GroceryManagementUI() {
        setTitle("Grocery Management");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // center window

        UIManager.put("Button.font", new Font("Arial", Font.BOLD, 14));
        UIManager.put("Label.font", new Font("Arial", Font.PLAIN, 14));
        UIManager.put("TextField.font", new Font("Arial", Font.PLAIN, 14));
        UIManager.put("Table.font", new Font("Arial", Font.PLAIN, 13));
        UIManager.put("Table.background", new Color(245, 245, 245));
        UIManager.put("Table.selectionBackground", new Color(100, 149, 237)); // Cornflower Blue
        UIManager.put("Table.selectionForeground", Color.WHITE);
        // --- END GLOBAL STYLING ---

        productDropdown = new JComboBox<>();
        customerDropdown = new JComboBox<>();

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Products", buildProductPanel());
        tabs.addTab("Customers", buildCustomerPanel());
        tabs.addTab("Sales", buildSalesPanel());
        tabs.addTab("Reports", buildReportPanel());

        add(tabs);
    }
    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
    }

    private JPanel gradientPanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, Color.CYAN, 0, getHeight(), Color.LIGHT_GRAY));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
    }

    private void styleTable(JTable table) {
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setRowHeight(25);
        table.getTableHeader().setBackground(new Color(100, 149, 237));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
    }

    private JPanel buildProductPanel() {
        JPanel panel = gradientPanel();
        panel.setLayout(new BorderLayout());
        JPanel form = new JPanel(new GridLayout(4, 2, 5, 5));
        form.setOpaque(false);

        form.add(new JLabel("Name:"));
        productNameField = new JTextField();
        form.add(productNameField);

        form.add(new JLabel("Price:"));
        productPriceField = new JTextField();
        form.add(productPriceField);

        form.add(new JLabel("Stock:"));
        productStockField = new JTextField();
        form.add(productStockField);

        JButton addBtn = new JButton("Add Product");
        styleButton(addBtn, new Color(100, 149, 237));
        addBtn.addActionListener(e -> addProduct());
        JButton delBtn = new JButton("Delete Product");
        styleButton(delBtn, new Color(220, 20, 60));
        delBtn.addActionListener(e -> deleteProduct());

        form.add(addBtn);
        form.add(delBtn);

        panel.add(form, BorderLayout.NORTH);

        productTableModel = new DefaultTableModel(new String[]{"Name", "Price", "Stock"}, 0);
        productTable = new JTable(productTableModel);
        styleTable(productTable);
        panel.add(new JScrollPane(productTable), BorderLayout.CENTER);

        return panel;
    }

    private void addProduct() {
        String name = productNameField.getText().trim();
        String priceText = productPriceField.getText().trim();
        String stockText = productStockField.getText().trim();
        if (name.isEmpty() || priceText.isEmpty() || stockText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fill all fields!");
            return;
        }
        try {
            double price = Double.parseDouble(priceText);
            int stock = Integer.parseInt(stockText);
            for (int i = 0; i < products.size(); i++) {
                Product p = products.get(i);
                if (p.name.equalsIgnoreCase(name)) {
                    if (JOptionPane.showConfirmDialog(this, "Product exists. Add stock?", "Product Exists", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        p.stock += stock;
                        productTableModel.setValueAt(p.stock, i, 2);
                    }
                    return;
                }
            }
            Product p = new Product(name, price, stock);
            products.add(p);
            productTableModel.addRow(new Object[]{p.name, p.price, p.stock});
            productDropdown.addItem(p.name);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number format!");
        }
    }

    private void deleteProduct() {
        if (products.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No products to delete!");
            return;
        }
        int index = productDropdown.getSelectedIndex();
        products.remove(index);
        productTableModel.removeRow(index);
        productDropdown.removeItemAt(index);
    }

    private JPanel buildCustomerPanel() {
        JPanel panel = gradientPanel();
        panel.setLayout(new BorderLayout());
        JPanel form = new JPanel(new GridLayout(3, 2, 5, 5));
        form.setOpaque(false);

        form.add(new JLabel("Name:"));
        customerNameField = new JTextField();
        form.add(customerNameField);

        form.add(new JLabel("Phone:"));
        customerPhoneField = new JTextField();
        form.add(customerPhoneField);

        JButton addBtn = new JButton("Add Customer");
        styleButton(addBtn, new Color(100, 149, 237));
        addBtn.addActionListener(e -> addCustomer());
        JButton delBtn = new JButton("Delete Customer");
        styleButton(delBtn, new Color(220, 20, 60));
        delBtn.addActionListener(e -> deleteCustomer());

        form.add(addBtn);
        form.add(delBtn);

        panel.add(form, BorderLayout.NORTH);

        customerTableModel = new DefaultTableModel(new String[]{"Name", "Phone"}, 0);
        customerTable = new JTable(customerTableModel);
        styleTable(customerTable);
        panel.add(new JScrollPane(customerTable), BorderLayout.CENTER);

        return panel;
    }

    private void addCustomer() {
        String name = customerNameField.getText().trim();
        String phone = customerPhoneField.getText().trim();
        if (name.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fill all fields!");
            return;
        }
        for (Customer c : customers)
            if (c.phone.equals(phone)) {
                JOptionPane.showMessageDialog(this, "Phone already registered!");
                return;
            }
        Customer c = new Customer(name, phone);
        customers.add(c);
        customerTableModel.addRow(new Object[]{c.name, c.phone});
        customerDropdown.addItem(c.name);
    }

    private void deleteCustomer() {
        if (customers.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No customers to delete!");
            return;
        }
        int index = customerDropdown.getSelectedIndex();
        customers.remove(index);
        customerTableModel.removeRow(index);
        customerDropdown.removeItemAt(index);
    }

    private JPanel buildSalesPanel() {
        JPanel panel = gradientPanel();
        panel.setLayout(new BorderLayout());
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setOpaque(false);

        JPanel pPanel = new JPanel(new BorderLayout());
        pPanel.setOpaque(false);
        pPanel.add(new JLabel("Product:"), BorderLayout.NORTH);
        pPanel.add(productDropdown, BorderLayout.CENTER);
        form.add(pPanel);

        JPanel qtyPanel = new JPanel(new BorderLayout());
        qtyPanel.setOpaque(false);
        qtyPanel.add(new JLabel("Quantity:"), BorderLayout.NORTH);
        saleQuantityField = new JTextField();
        qtyPanel.add(saleQuantityField, BorderLayout.CENTER);
        form.add(qtyPanel);

        JPanel buyerPanel = new JPanel(new BorderLayout());
        buyerPanel.setOpaque(false);
        buyerPanel.add(new JLabel("Buyer:"), BorderLayout.NORTH);
        buyerPanel.add(customerDropdown, BorderLayout.CENTER);
        form.add(buyerPanel);

        JButton sellBtn = new JButton("Sell");
        styleButton(sellBtn, new Color(34, 139, 34)); // green
        sellBtn.addActionListener(e -> makeSale());
        form.add(sellBtn);

        panel.add(form, BorderLayout.NORTH);

        salesTableModel = new DefaultTableModel(new String[]{"Product", "Qty", "Total", "Buyer"}, 0);
        salesTable = new JTable(salesTableModel);
        styleTable(salesTable);
        panel.add(new JScrollPane(salesTable), BorderLayout.CENTER);

        return panel;
    }

    private void makeSale() {
        String productName = (String) productDropdown.getSelectedItem();
        String buyerName = (String) customerDropdown.getSelectedItem();
        String qtyText = saleQuantityField.getText().trim();
        if (productName == null || buyerName == null || qtyText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select product, customer, and enter quantity!");
            return;
        }
        try {
            int qty = Integer.parseInt(qtyText);
            for (int i = 0; i < products.size(); i++) {
                Product p = products.get(i);
                if (p.name.equals(productName)) {
                    if (p.stock < qty) {
                        JOptionPane.showMessageDialog(this, "Not enough stock!");
                        return;
                    }
                    p.stock -= qty;
                    double total = qty * p.price;
                    sales.add(new Sale(p.name, qty, total, buyerName));
                    salesTableModel.addRow(new Object[]{p.name, qty, total, buyerName});
                    productTableModel.setValueAt(p.stock, i, 2);
                    updateReport();
                    return;
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid quantity!");
        }
    }

    private JPanel buildReportPanel() {
        JPanel panel = gradientPanel();
        panel.setLayout(new BorderLayout());
        reportArea = new JTextArea();
        reportArea.setBackground(Color.WHITE);
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(new JScrollPane(reportArea), BorderLayout.CENTER);
        return panel;
    }

    private void updateReport() {
        StringBuilder report = new StringBuilder();
        double totalRevenue = 0;
        for (Sale s : sales) {
            report.append(s.buyer).append(" bought ").append(s.qty).append(" x ").append(s.product).append(" = ").append(s.total).append(" birr\n");
            totalRevenue += s.total;
        }
        report.append("\nOverall Revenue: ").append(totalRevenue).append(" birr");
        reportArea.setText(report.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GroceryManagementUI().setVisible(true));
    }

    class Product { String name; double price; int stock; Product(String n,double p,int s){name=n;price=p;stock=s;} }
    class Customer{ String name,phone; Customer(String n,String ph){name=n;phone=ph;} }
    class Sale{ String product; int qty; double total; String buyer; Sale(String p,int q,double t,String b){product=p;qty=q;total=t;buyer=b;} }
}
