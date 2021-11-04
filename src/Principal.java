import java.sql.*;

public class Principal {
    public static void main(String[] args) {
        String url = "jdbc:sqlserver://localhost;databaseName=Chat;integratedSecurity=true";
        try (Connection cnx = DriverManager.getConnection(url)) {
            if (!cnx.isClosed()) {
                try {
                    // Desactivar confirmación automática
                    cnx.setAutoCommit(false);

                    CallableStatement cstm = cnx.prepareCall("{?=call userRegistro_Sp(?, ?, ?, ?)}");
                    cstm.registerOutParameter(1, Types.INTEGER);
                    cstm.setString(2, "pruebaTransaccion");
                    cstm.setString(3, "pruebaTransaccion");
                    cstm.setString(4, "pruebaTransaccion@correo.es");
                    cstm.setString(5, "PruebaTransaccion");
                    cstm.execute();
                    int emisor = cstm.getInt(1);
                    String sentencia = "INSERT INTO Mensajes(emisor, receptor, asunto, texto) VALUES (1, ?, 'Bienvenido','Mensaje de bienvenida')";
                    PreparedStatement pstmt = cnx.prepareStatement(sentencia);
                    pstmt.setInt(1, emisor);
                    int filas = pstmt.executeUpdate();
                    if (filas > 0)
                        System.out.println("Mensaje de bienvenida enviado correctamente");
                    else
                        System.out.println("No se ha enviado el mensaje de bienvenida");

                    cnx.commit();
                }catch (SQLException ex){
                    System.out.println(ex.getMessage());
                    cnx.rollback();
                    System.out.println("Transacción abortada");
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
