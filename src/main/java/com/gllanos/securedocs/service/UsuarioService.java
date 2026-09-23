package com.gllanos.securedocs.service;

import com.gllanos.securedocs.model.Usuario;
import com.gllanos.securedocs.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Usuario obtener(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

    public Usuario crear(Usuario usuario) {
        if (usuarioRepository.findByCorreo(usuario.getCorreo()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "El correo ya está registrado");
        }
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }

    public Usuario modificar(Long id, Usuario datos) {
        Usuario usuario = obtener(id);
        usuario.setNombre(datos.getNombre());
        usuario.setRol(datos.getRol());
        usuario.setDepartamento(datos.getDepartamento());
        usuario.setNivelSeguridad(datos.getNivelSeguridad());
        usuario.setPais(datos.getPais());
        usuario.setTipoContrato(datos.getTipoContrato());
        usuario.setEstado(datos.getEstado());
        if (datos.getPassword() != null && !datos.getPassword().isBlank()) {
            usuario.setPassword(passwordEncoder.encode(datos.getPassword()));
        }
        return usuarioRepository.save(usuario);
    }

    public void activar(Long id) {
        Usuario usuario = obtener(id);
        usuario.setEstado(Usuario.EstadoUsuario.ACTIVO);
        usuarioRepository.save(usuario);
    }

    public void desactivar(Long id) {
        Usuario usuario = obtener(id);
        usuario.setEstado(Usuario.EstadoUsuario.INACTIVO);
        usuarioRepository.save(usuario);
    }
}