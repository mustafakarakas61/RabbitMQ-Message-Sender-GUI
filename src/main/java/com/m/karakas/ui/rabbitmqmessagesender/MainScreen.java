/*
 * Created by JFormDesigner on Sat Feb 04 10:23:10 TRT 2023
 */

package com.m.karakas.ui.rabbitmqmessagesender;

import com.rabbitmq.client.*;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author musta
 */
public class MainScreen extends JFrame {
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel lblQueueName;
    private JTextField txtQueueName;
    private JScrollPane scrollPane1;
    private JTable table;
    private JLabel lblQueueName2;
    private JPanel buttonBar;
    private JButton btnSenderButton, btnAddRow, btnDeleteRow;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainScreen frame = new MainScreen();
                    frame.setTitle("RabbitMQ Message Sender");
                    frame.setSize(810,470);
                    frame.setLocationRelativeTo(null);
                    frame.setResizable(false);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public MainScreen() throws IOException, TimeoutException {
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        lblQueueName = new JLabel();
        txtQueueName = new JTextField();
        scrollPane1 = new JScrollPane();
        table = new JTable();
        lblQueueName2 = new JLabel();
        buttonBar = new JPanel();
        btnSenderButton = new JButton();
        btnAddRow = new JButton();
        btnDeleteRow = new JButton();

        //======== this ========
        setBackground(new Color(0x999999));
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setBackground(new Color(0xff9900));
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setBackground(new Color(0x666666));
                contentPanel.setLayout(null);

                //---- lblQueueName ----
                lblQueueName.setText("Queue Name");
                lblQueueName.setHorizontalAlignment(SwingConstants.CENTER);
                lblQueueName.setForeground(new Color(0xffcc33));
                lblQueueName.setFont(new Font("Segoe UI Black", Font.BOLD, 20));
                contentPanel.add(lblQueueName);
                lblQueueName.setBounds(20, 10, 145, 40);

                //---- txtQueueName ----
                txtQueueName.setFont(new Font("Segoe UI Historic", Font.PLAIN, 20));
                contentPanel.add(txtQueueName);
                txtQueueName.setBounds(15, 45, 155, 40);


                String[] columnNames = {"Name", "Value", "Type"};
                String[] types = {"String", "Integer", "Float"};
                DefaultTableModel model = new DefaultTableModel(columnNames, 0);
                JComboBox typeCombo = new JComboBox(types);
                String[] rowData = {"", "", "String"};
                model.addRow(rowData);
                table = new JTable(model);

                JTableHeader header = table.getTableHeader();
                header.setFont(new Font("Segoe UI Historic", Font.PLAIN, 18));
                table.setFont(new Font("Arial", Font.PLAIN, 15));
                table.setRowHeight(25);
                table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(typeCombo));
                table.getColumnModel().getColumn(0).setPreferredWidth(100);
                table.getColumnModel().getColumn(1).setPreferredWidth(400);
                table.getColumnModel().getColumn(2).setPreferredWidth(57);

                //======== scrollPane1 ========
                {
                    scrollPane1.setViewportView(table);
                }
                contentPanel.add(scrollPane1);
                scrollPane1.setBounds(185, 45, 560, 300);

                //---- lblQueueName2 ----
                lblQueueName2.setText("Json Message");
                lblQueueName2.setHorizontalAlignment(SwingConstants.CENTER);
                lblQueueName2.setForeground(new Color(0xffcc33));
                lblQueueName2.setFont(new Font("Segoe UI Black", Font.BOLD, 20));
                contentPanel.add(lblQueueName2);
                lblQueueName2.setBounds(185, 10, 555, 40);

                //======== buttonBar ========
                {
                    buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                    buttonBar.setBackground(new Color(0x666666));
                    buttonBar.setLayout(new GridBagLayout());
                    ((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 80};
                    ((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0};

                    // ------------------------------------------ BUTTON
                    btnAddRow.setText("Add Row");

                    btnAddRow.setFont(new Font("Segoe UI Black", Font.PLAIN, 20));
                    btnAddRow.setBackground(Color.GREEN);

                    buttonBar.add(btnAddRow, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 0), 0, 0));

                    btnAddRow.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        DefaultTableModel model = (DefaultTableModel) table.getModel();
                        Object[] rowData = { "", "", "String" };
                        model.addRow(rowData);
                    }
                });


                    btnDeleteRow.setText("Delete Row");

                    btnDeleteRow.setFont(new Font("Segoe UI Black", Font.PLAIN, 20));
                    btnDeleteRow.setBackground(Color.red);

                    buttonBar.add(btnDeleteRow, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 0), 0, 0));

                    btnDeleteRow.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            int selectedRow = table.getSelectedRow();
                            if (selectedRow != -1) {
                                int response = JOptionPane.showConfirmDialog(contentPane, "Are you sure you want to delete this row?", "Confirm",
                                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                                if (response == JOptionPane.YES_OPTION) {
                                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                                    model.removeRow(selectedRow);
                                }
                            } else {
                                JOptionPane.showMessageDialog(contentPane, "Please select a row to delete", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    });

                    btnSenderButton.setText("Send");
                    btnSenderButton.setFont(new Font("Segoe UI Black", Font.PLAIN, 20));
                    btnSenderButton.setBackground(Color.cyan);
                    buttonBar.add(btnSenderButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));

                    btnSenderButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            int rowCount = table.getRowCount();
                            JSONObject json = new JSONObject();
                            for (int i = 0; i < rowCount; i++) {
                                String name = (String) table.getValueAt(i, 0);
                                String value = (String) table.getValueAt(i, 1);
                                String type = (String) table.getValueAt(i, 2);
                                if(table.getValueAt(i, 0).toString().length() < 1 || table.getValueAt(i, 1).toString().length() < 1)
                                {
                                    JOptionPane.showMessageDialog(contentPane, "Please enter the correct parameters for " +(i+1) +". row", "Error", JOptionPane.ERROR_MESSAGE);
                                }
                                else if (type.equals("String")) {
                                    json.put(name, value);
                                } else if (type.equals("Integer")) {
                                    json.put(name, Integer.parseInt(value));
                                } else if (type.equals("Float")) {
                                    json.put(name, Float.parseFloat(value));
                                }
                            }
                            if(txtQueueName.getText()!=null){
                                if(json.isEmpty()){
                                    JOptionPane.showMessageDialog(contentPane, "Message is null!", "Error", JOptionPane.ERROR_MESSAGE);
                                } else {
                                    System.out.println(json);

                                    ConnectionFactory factory = new ConnectionFactory();
                                    factory.setHost("localhost");
                                    try (Connection connection = factory.newConnection();
                                         Channel channel = connection.createChannel()) {
                                        channel.queueDeclare("bc-clip", true, false, false, null);
                                        channel.basicPublish("", "bc-clip",
                                                new AMQP.BasicProperties.Builder()
                                                        .contentType("application/json")
                                                        .deliveryMode(2)
                                                        .build(), json.toString().getBytes("UTF-8"));
                                    } catch (IOException ex) {
                                        throw new RuntimeException(ex);
                                    } catch (TimeoutException ex) {
                                        throw new RuntimeException(ex);
                                    }


                                }
                            } else{
                                JOptionPane.showMessageDialog(contentPane, "Queue name is not be null!", "Error", JOptionPane.ERROR_MESSAGE);
                            }

                        }
                    });

                }
                contentPanel.add(buttonBar);
                buttonBar.setBounds(0, 355, 749, buttonBar.getPreferredSize().height);

                {
                    // compute preferred size
                    Dimension preferredSize = new Dimension();
                    for(int i = 0; i < contentPanel.getComponentCount(); i++) {
                        Rectangle bounds = contentPanel.getComponent(i).getBounds();
                        preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                        preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                    }
                    Insets insets = contentPanel.getInsets();
                    preferredSize.width += insets.right;
                    preferredSize.height += insets.bottom;
                    contentPanel.setMinimumSize(preferredSize);
                    contentPanel.setPreferredSize(preferredSize);
                }
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
    }
}
