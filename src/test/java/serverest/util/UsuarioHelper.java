package serverest.util;

import java.util.UUID;

public class UsuarioHelper {
    public static String gerarEmail() {
        return "user_" + UUID.randomUUID() + "@qa.com";
    }
}
