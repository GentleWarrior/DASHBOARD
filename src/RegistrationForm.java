import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;


public class RegistrationForm extends JDialog {
    private JTextField tfNAME;
    private JTextField tfAddress;
    private JTextField tfPass;
    private JTextField tfconfirmPassword;
    private JButton btnRegister;
    private JButton btnCancel;
    private JTextField tfPhone;
    private JTextField tfEmail;
    private JPanel registrationPanel;

    public RegistrationForm(JFrame parent) {
        super(parent);
        setTitle("CREATE A NEW ACCOUNT");
        setContentPane(registrationPanel);
        setMinimumSize(new Dimension(450,475));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                registerUser();
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                dispose();
            }
        });

        setVisible(true);
    }

    private void registerUser() {
        String name = tfNAME.getText();
        String email = tfEmail.getText();
        String phone = tfPhone.getText();
        String address = tfAddress.getText();
        String password = String.valueOf(tfPass.getText());
        String confirmPassword =  String.valueOf(tfconfirmPassword.getText());

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty() || password.isEmpty()){
            JOptionPane.showMessageDialog(this,
                    "Please enter all Fields",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(!password.equals(confirmPassword)){
            JOptionPane.showMessageDialog(this,
                    "Confirm Password does not match",
                    "Try Again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

       user = addUserToDatabase(name, email, phone, password,address);
        if (user != null){
            dispose();
        }
        else {
            JOptionPane.showMessageDialog(this,
                    "Failed to register new user",
                    "Try Again",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public User user;
    private User addUserToDatabase(String name, String email, String phone, String password, String address) {
        User user = null;
        final String DB_URL = "";
        final String USERNAME = " ";
        final String PASSWORD = "";

        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
                //CONNECTION TO DATABASE WAS SUCCESSFUL.

            Statement stat = conn.createStatement();
            String sql = "INSERT INTO users (name, email, phone, address, password)" +
                        "VALUES (?,?,?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, phone);
            preparedStatement.setString(4, address);
            preparedStatement.setString(5, password);

            //Insert Rows in table
            int addedRows = preparedStatement.executeUpdate();
            if (addedRows > 0) {
                user = new User();
                user.name = name;
                user.email = email;
                user.phone = phone;
                user.address = address;
                user.password = password;
            }

            preparedStatement.close();
            conn.close();
        } catch (Exception e){
            e.printStackTrace();
        }

        return user;
    }

    public static void main(String[] args) {
      RegistrationForm myForm = new RegistrationForm(null);
      User user = myForm.user;
      if (user != null){
          System.out.println("Successful Registration of: " + user.name);
      }
      else {
          System.out.println("Registration Canceled: ");
      }
    }
}
