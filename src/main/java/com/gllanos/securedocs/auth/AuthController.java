package com.gllanos.securedocs.auth;

import com.gllanos.securedocs.model.Usuario;
import com.gllanos.securedocs.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        Usuario usuario = usuarioRepository.findByCorreo(request.getCorreo())
                .orElse(null);

        if (usuario == null || !passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Credenciales inválidas");
        }

        if (usuario.getEstado() != Usuario.EstadoUsuario.ACTIVO) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Usuario inactivo o suspendido");
        }

        String token = jwtUtil.generateToken(
                usuario.getCorreo(),
                usuario.getRol().getNombre().name()
        );

        return ResponseEntity.ok(new AuthResponse(
                token,
                usuario.getCorreo(),
                usuario.getRol().getNombre().name()
        ));
    }
}