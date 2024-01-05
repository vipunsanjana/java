import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class register extends JDialog{
    private JPanel registerPanel;
    private JTextField tfName;
    private JTextField tfEmail;
    private JTextField tfPhoneNumber;
    private JTextField tfAddress;
    private JPasswordField pfPassword;
    private JPasswordField pfConformPassword;
    private JButton btnSignUp;
    private JButton btnLogIn;

    public register(JFrame parent) {
        super(parent);
        setTitle("Register Form");
        setContentPane(registerPanel);
        setMinimumSize(new Dimension(650, 600));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btnSignUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });

        btnLogIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //future
                login log = new login(null);
                log.show();
                dispose();
            }
        });
        setVisible(true);
    }

    private void registerUser() {
        String name = tfName.getText();
        String email = tfEmail.getText();
        String phone = tfPhoneNumber.getText();
        String address = tfAddress.getText();
        String password = String.valueOf(pfPassword.getPassword());
        String conformPassword = String.valueOf(pfConformPassword.getPassword());

        if(name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty() || password.isEmpty()){
            JOptionPane.showMessageDialog(this,
                    "Please Enter All Fields",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(conformPassword)){
            JOptionPane.showMessageDialog(this,
                    "Conform Password Does Not Match",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        user = addUserDatabase(name,email,phone,address,password);

        if (user != null){
            dispose();JOptionPane.showMessageDialog(null, "successful registation!");


        }
        else {
            JOptionPane.showMessageDialog(this,
                    "Failed To New User",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    public User user;
    private User addUserDatabase(String name, String email, String phone, String address, String password) {
        User user = null;

        final String DB_URL = "jdbc:mysql://localhost:3306/form";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try {
            Connection conn = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO users (name,email,phone,address,password) " + "VALUES(?, ?, ?, ?, " +
                    "?)";

            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,email);
            preparedStatement.setString(3,phone);
            preparedStatement.setString(4,address);
            preparedStatement.setString(5,password);

            int addRows = preparedStatement.executeUpdate();
            if (addRows > 0){
                user = new User();
                user.name = name;
                user.email = email;
                user.phone = phone;
                user.address = address;
                user.password = password;
            }


            stmt.close();
            conn.close();

        }catch (Exception e){
            e.printStackTrace();
        }

        return user;
    }


    public static void main(String[] args) {
        register registerForm = new register(null);
        User user = registerForm.user;

        if (user != null){
            System.out.println("successful registation of name : "+user.name);
            System.out.println("                         email : "+user.email);
            System.out.println("                         Phone : "+user.phone);
            System.out.println("                       address : "+user.address);
        }else {
            System.out.println("registation cancelled");
        }
    }
}
