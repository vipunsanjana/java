import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class login extends JDialog {

    private JPanel loginPanel;
    private JTextField tfEmail;
    private JPasswordField pfPassword;
    private JButton btnSignUp;
    private JButton btnLogIn;

    public login(JFrame parent) {
        super(parent);
        setTitle("Login Form");
        setContentPane(loginPanel);
        setMinimumSize(new Dimension(650, 600));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btnLogIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = tfEmail.getText();
                String password = String.valueOf(pfPassword.getPassword());

                user = getAuthenticatedUser(email, password);

                if (user != null){
                    dispose();

                    JOptionPane.showMessageDialog(null, "You can login now!");

                }else {
                    JOptionPane.showMessageDialog(login.this,
                            "Email or password invalid",
                            "Try again",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        btnSignUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                register regi = new register(null);
                regi.show();
                dispose();
            }
        });
        setVisible(true);
    }

    public User user;
    private User getAuthenticatedUser(String email,String password){
        User user = null;

        final String DB_URL = "jdbc:mysql://localhost:3306/form";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try {
            Connection conn = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM users WHERE email=? AND password=?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,email);
            preparedStatement.setString(2,password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                user = new User();
                user.name = resultSet.getString("name");
                user.email = resultSet.getString("email");
                user.phone = resultSet.getString("phone");
                user.address = resultSet.getString("address");
                user.password = resultSet.getString("password");
            }

            stmt.close();
            conn.close();

        }catch (Exception e){
            e.printStackTrace();
        }

        return user;
    }


    public static void main(String[] args) {

        login loginForm = new login(null);
        User user = loginForm.user;

        if (user != null){
            System.out.println("successful authentication of : "+user.name);
            System.out.println("                       Email : "+user.email);
            System.out.println("                       Phone : "+user.phone);
            System.out.println("                     address : "+user.address);
        }else {
            System.out.println("authentication cancelled");
        }
    }
}
