import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JScrollPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
import java.awt.Color;
import java.util.ArrayList;


public class WhiteBoardGUI extends JFrame {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private ArrayList<User> newUserList = new ArrayList<User>();
    private ArrayList<String> chatList = new ArrayList<String>();
    private DefaultListModel dlm;
    private DefaultListModel chat;
    private User user;
    private boolean isManager;
    private PaintBoard drawBoard;
    private Color selectColor = Color.WHITE;
    private Container c = getContentPane();

    public WhiteBoardGUI(Socket socket, User user, Boolean isManager, ArrayList<User> init_users) throws IOException {
        this.socket = socket;
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        this.user = user;
        this.isManager = isManager;
        this.newUserList = init_users;
        selectColor = Color.WHITE;

        this.GUIInitialize();
        this.setVisible(true);
    }


    public void GUIInitialize() throws IOException {
        setTitle("Whiteboard - " + user.getName());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int response = JOptionPane.showConfirmDialog(null,
                        "Do you want to quit?", "Confirm",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.CANCEL_OPTION);

                if (response == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });


        this.setSize(1100, 650);
        this.setLocationRelativeTo(null);
        setResizable(false);
        if(isManager) {
            JMenuBar menuBar = initialMenuBar();
            this.setJMenuBar(menuBar);
        }


        JPanel drawToolPanel = initialDrawTool();
        drawToolPanel.setBounds(0,0,140,650);
        drawToolPanel.setBackground(Color.GRAY);
//        this.add(drawToolPanel);
        c.add(drawToolPanel);

        JPanel paintPanel = initialPaintArea();
        paintPanel.setBounds(140,0,560,650);
        paintPanel.setBackground(Color.WHITE);
        paintPanel.setLayout(null);
        this.add(paintPanel);
        c.add(paintPanel);

        JPanel chatPanel = initialChatBox();
        chatPanel.setBounds(700,0,200,650);
//        this.add(chatPanel);
        c.add(chatPanel);


        JPanel userListPanel = initialUserList();
        userListPanel.setBounds(900,0,200,650);
//        this.add(userListPanel);
        c.add(userListPanel);



        setLayout(null);
        setVisible(true);


    }

    public JMenuBar initialMenuBar(){
        JMenuBar menu = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        menu.add(fileMenu);
        JMenuItem newBtn = new JMenuItem("New");
        JMenuItem openBtn = new JMenuItem("Open");
        JMenuItem saveBtn = new JMenuItem("Save");
        JMenuItem saveAsBtn = new JMenuItem("SaveAs");
        JMenuItem closeBtn = new JMenuItem("Close");

        fileMenu.add(newBtn);
        fileMenu.addSeparator();
        fileMenu.add(openBtn);
        fileMenu.addSeparator();
        fileMenu.add(saveBtn);
        fileMenu.addSeparator();
        fileMenu.add(saveAsBtn);
        fileMenu.addSeparator();
        fileMenu.add(closeBtn);



        newBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawBoard.clear();
            }
        });

        closeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });



        return menu;
    }





    public JPanel initialDrawTool(){

        JPanel btnPanel = new JPanel();
        JButton lineBtn = new JButton("Line");
        JButton circleBtn = new JButton("Circle");
        JButton ovalBtn = new JButton("Oval");
        JButton rectBtn = new JButton("Rect");
        JButton textBtn = new JButton("Text");
        JButton colorChooseBtn = new JButton("Choose Color");

        btnPanel.add(lineBtn);
        btnPanel.add(circleBtn);
        btnPanel.add(ovalBtn);
        btnPanel.add(rectBtn);
        btnPanel.add(textBtn);
        btnPanel.add(colorChooseBtn);
        lineBtn.setBounds(10,100,120,50);
        circleBtn.setBounds(10,150,120,50);
        ovalBtn.setBounds(10,200,120,50);
        rectBtn.setBounds(10,250,120,50);
        textBtn.setBounds(10,300,120,50);
        colorChooseBtn.setBounds(10,350,120,50);



        colorChooseBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(null, "Select Color", selectColor);
                selectColor = color;
                colorChooseBtn.setForeground(selectColor);
                drawBoard.setDrawColor(selectColor);
            }
        });



        lineBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawBoard.setDrawType("LINE");
            }
        });

        circleBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawBoard.setDrawType("CIRCLE");
            }
        });

        ovalBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawBoard.setDrawType("OVAL");
            }
        });

        rectBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawBoard.setDrawType("RECT");
            }
        });


        textBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawBoard.setDrawType("TEXT");
                String text = JOptionPane.showInputDialog("Input the text:");
                drawBoard.setText(text);
            }
        });





        btnPanel.setLayout(null);
        return btnPanel;
    }

    public JPanel initialPaintArea() throws IOException {

        drawBoard = new PaintBoard(socket, user);
        JPanel paintPanel = new JPanel();
        paintPanel.add(drawBoard);
        drawBoard.setBounds(0, 0, 550, 640);


        return paintPanel;
    }



    public JPanel initialUserList(){
        dlm = new DefaultListModel();
        dlm.clear();
        for(User usrc: newUserList){
            dlm.addElement(usrc.getName());
        }
        JList userList = new JList();
        userList.setModel(dlm);


        JPanel usersBlock = new JPanel();
        usersBlock.setBounds(0,100,200, 450);
        JPanel managerOpr = new JPanel();
        usersBlock.setLayout(new BorderLayout(0, 0));
        usersBlock.add(userList);

        JButton removeBtn = new JButton("Remove");

        removeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                System.out.println("Remove user clicked:" + userList.getSelectedIndex());
                User selectedUser = newUserList.get(userList.getSelectedIndex());

                if (selectedUser.getName().equals(newUserList.get(0).getName())) {
                    JOptionPane.showMessageDialog(null,
                            "You cannot kick out yourself.");
                }
                else {
                    try {
                        out.writeUTF("REMOVE_USER:" + selectedUser.getName());
                        out.flush();
                    } catch (IOException ioException) {
                        JOptionPane.showMessageDialog(null,
                                "Kick " + selectedUser.getName() + " failed.");
                    }

                }

            }
        });

        JLabel title = new JLabel("User List");
        usersBlock.add(new JScrollPane(userList), BorderLayout.CENTER);
        managerOpr.add(removeBtn);
        JPanel userListPanel = new JPanel();
        title.setBounds(60, 0, 200, 50);
        usersBlock.setBounds(0, 50, 200, 500);
        managerOpr.setBounds(0, 550, 200, 100);
        managerOpr.setLayout(null);
        userListPanel.add(title);
        userListPanel.add(usersBlock);
        userListPanel.setLayout(null);

        if (isManager) {
            userListPanel.add(managerOpr);
            removeBtn.setBounds(100,0,100,50);
        }

        return userListPanel;

    }

    public JPanel initialChatBox(){
        JPanel chatBox = new JPanel();

        chat = new DefaultListModel();
        chat.clear();
        for(String msg: chatList){
            chat.addElement(msg);
        }
        JList chatInfoList = new JList();
        chatInfoList.setModel(chat);
        chatBox.setBounds(0,100,200, 450);
        chatBox.setLayout(new BorderLayout(0, 0));
        chatBox.add(chatInfoList);
        chatBox.add(new JScrollPane(chatInfoList), BorderLayout.CENTER);


        JPanel sendBox = new JPanel();
        JTextArea inputText = new JTextArea();

        inputText.setBounds(0, 0, 200, 200);
        inputText.setBackground(Color.WHITE);
        JScrollPane newScroll=new JScrollPane(inputText);
        newScroll.setBounds(0, 0, 150, 50);
        JButton send = new JButton("Send");
        sendBox.add(newScroll);
        sendBox.add(send);
        send.setBounds(150,0,50,50);

        JLabel title = new JLabel("Chat Box");
        title.setBounds(60, 0, 200, 50);
        chatBox.setBounds(0,50,200, 500);
        sendBox.setBounds(0,550,200,100);
        JPanel chatTotalPanel = new JPanel();
        chatTotalPanel.add(title);
        chatTotalPanel.add(chatBox);
        chatTotalPanel.add(sendBox);
        sendBox.setLayout(null);
        chatTotalPanel.setLayout(null);

        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(inputText.getText().equals("")){
                        JOptionPane.showMessageDialog(null,
                                "Msg cannot be empty");
                    }
                    else{
                        out.writeUTF("SEND_MSG:" + user.getName() + " "+ inputText.getText());
                        inputText.setText("");
                    }

                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        return  chatTotalPanel;
    }


    public PaintBoard getDrawBoard(){
        return drawBoard;
    }


    public void updateUserList(ArrayList<User> users) throws IOException {
        this.newUserList = users;
        dlm.clear();
        for(User usrc: newUserList){
            dlm.addElement(usrc.getName());
        }
    }


    public void updateChatList(ArrayList<String> info) throws IOException {
        this.chatList = info;
        chat.clear();
        for(String msg: chatList){
            chat.addElement(msg);
        }
    }


}
