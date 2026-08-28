
package org.lsa.utils;

import org.lsa.model.Usuario;

/**
 *
 * @author Gregory Jerónimo
 */
public class ControlAcceso {
    public static boolean tienePermiso(Usuario usuario, String vistaDestino) {
        if (usuario == null || !usuario.isActivo()) {
            return false;
        }

        String rol = usuario.getRol().toLowerCase();

        switch (vistaDestino) {
            case "DashboardAdminController.fxml":
                return rol.equals("admin");
            case "DashboardBodegaController.fxml":
                return rol.equals("admin") || rol.equals("bodega");
            case "DashboardCajeroController.fxml":
                return rol.equals("admin") || rol.equals("cajero");
            default:
                return false;
        }
    }
}
