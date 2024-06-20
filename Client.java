package chatting.application;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.net.*;
import java.io.*;

public class Client implements ActionListener {

    JTextField textField;
    static JPanel messagePanel;
    static Box vertical = Box.createVerticalBox();
    static JFrame frame = new JFrame();
    static DataOutputStream dout;

    Client() {
        frame.setLayout(null);

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(7, 94, 84));
        headerPanel.setBounds(0, 0, 450, 70);
        headerPanel.setLayout(null);
        frame.add(headerPanel);

        ImageIcon backIcon = new ImageIcon(ClassLoader.getSystemResource("icons/3.png"));
        Image backImg = backIcon.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        JLabel backLabel = new JLabel(new ImageIcon(backImg));
        backLabel.setBounds(5, 20, 25, 25);
        headerPanel.add(backLabel);

        backLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent ae) {
                System.exit(0);
            }
        });

        ImageIcon profileIcon = new ImageIcon(ClassLoader.getSystemResource("icons/Teacher.jpeg"));
        Image profileImg = profileIcon.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);
        JLabel profileLabel = new JLabel(new ImageIcon(profileImg));
        profileLabel.setBounds(40, 10, 50, 50);
        headerPanel.add(profileLabel);

        ImageIcon videoIcon = new ImageIcon(ClassLoader.getSystemResource("icons/video.png"));
        Image videoImg = videoIcon.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        JLabel videoLabel = new JLabel(new ImageIcon(videoImg));
        videoLabel.setBounds(300, 20, 30, 30);
        headerPanel.add(videoLabel);

        ImageIcon phoneIcon = new ImageIcon(ClassLoader.getSystemResource("icons/phone.png"));
        Image phoneImg = phoneIcon.getImage().getScaledInstance(35, 30, Image.SCALE_DEFAULT);
        JLabel phoneLabel = new JLabel(new ImageIcon(phoneImg));
        phoneLabel.setBounds(360, 20, 35, 30);
        headerPanel.add(phoneLabel);

        ImageIcon moreIcon = new ImageIcon(ClassLoader.getSystemResource("icons/3icon.png"));
        Image moreImg = moreIcon.getImage().getScaledInstance(10, 25, Image.SCALE_DEFAULT);
        JLabel moreLabel = new JLabel(new ImageIcon(moreImg));
        moreLabel.setBounds(420, 20, 10, 25);
        headerPanel.add(moreLabel);

        JLabel nameLabel = new JLabel("Teacher");
        nameLabel.setBounds(110, 15, 100, 18);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("SAN_SERIF", Font.BOLD, 18));
        headerPanel.add(nameLabel);

        JLabel statusLabel = new JLabel("Active Now");
        statusLabel.setBounds(110, 35, 100, 18);
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(new Font("SAN_SERIF", Font.BOLD, 14));
        headerPanel.add(statusLabel);

        messagePanel = new JPanel();
        messagePanel.setBounds(5, 75, 440, 570);
        messagePanel.setLayout(new BorderLayout());
        frame.add(messagePanel);

        textField = new JTextField();
        textField.setBounds(5, 655, 310, 40);
        textField.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        frame.add(textField);

        JButton sendButton = new JButton("Send");
        sendButton.setBounds(320, 655, 123, 40);
        sendButton.setBackground(new Color(7, 94, 84));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        sendButton.addActionListener(this);
        frame.add(sendButton);

        frame.setSize(450, 700);
        frame.setLocation(800, 50);
        frame.setUndecorated(true);
        frame.getContentPane().setBackground(Color.WHITE);
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        try {
            String out = textField.getText();
            JPanel formattedMessage = formatLabel(out);
            JPanel right = new JPanel(new BorderLayout());
            right.add(formattedMessage, BorderLayout.LINE_END);
            vertical.add(right);
            vertical.add(Box.createVerticalStrut(15));
            messagePanel.add(vertical, BorderLayout.PAGE_START);
            dout.writeUTF(out);
            textField.setText("");
            frame.repaint();
            frame.invalidate();
            frame.validate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JPanel formatLabel(String out) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel output = new JLabel("<html><p style=\"width: 150px\">" + out + "</p></html>");
        output.setFont(new Font("Tahoma", Font.PLAIN, 16));
        output.setBackground(new Color(37, 211, 102));
        output.setOpaque(true);
        output.setBorder(new EmptyBorder(15, 15, 15, 50));
        output.setBorder(new RoundedBorder(15));
        panel.add(output);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        JLabel time = new JLabel();
        time.setText(sdf.format(cal.getTime()));
        panel.add(time);

        return panel;
    }

    static class RoundedBorder extends AbstractBorder {
        private int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(c.getBackground());
            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
    }

    public static void main(String[] args) {
        new Client();
        try {
            Socket s = new Socket("127.0.0.1", 6001);
            DataInputStream din = new DataInputStream(s.getInputStream());
            dout = new DataOutputStream(s.getOutputStream());
            while (true) {
                messagePanel.setLayout(new BorderLayout());
                String msg = din.readUTF();
                JPanel panel = formatLabel(msg);
                JPanel left = new JPanel(new BorderLayout());
                left.add(panel, BorderLayout.LINE_START);
                vertical.add(left);
                vertical.add(Box.createVerticalStrut(15));
                messagePanel.add(vertical, BorderLayout.PAGE_START);
                frame.validate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
