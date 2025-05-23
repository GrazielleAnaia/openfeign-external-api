package com.grazielleanaia.openfeign_external_api.business;

import com.grazielleanaia.openfeign_external_api.business.converter.UsuarioConverter;
import com.grazielleanaia.openfeign_external_api.business.dto.EnderecoDTO;
import com.grazielleanaia.openfeign_external_api.business.dto.TelefoneDTO;
import com.grazielleanaia.openfeign_external_api.business.dto.UsuarioDTO;
import com.grazielleanaia.openfeign_external_api.infrastructure.entity.Endereco;
import com.grazielleanaia.openfeign_external_api.infrastructure.entity.Telefone;
import com.grazielleanaia.openfeign_external_api.infrastructure.entity.Usuario;
import com.grazielleanaia.openfeign_external_api.infrastructure.exception.ConflictException;
import com.grazielleanaia.openfeign_external_api.infrastructure.exception.ResourceNotFoundException;
import com.grazielleanaia.openfeign_external_api.infrastructure.repository.EnderecoRepository;
import com.grazielleanaia.openfeign_external_api.infrastructure.repository.TelefoneRepository;
import com.grazielleanaia.openfeign_external_api.infrastructure.repository.UsuarioRepository;
import com.grazielleanaia.openfeign_external_api.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EnderecoRepository enderecoRepository;
    private final TelefoneRepository telefoneRepository;


    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO) {

        emailExiste(usuarioDTO.getEmail());
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));

        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));

    }

    public void emailExiste(String email) {
        try {
            boolean existe = verificaEmailExistente(email);
            if (existe) {
                throw new ConflictException("Email ja cadastrado" + email);
            }
        } catch (ConflictException e) {
            throw new ConflictException("Email ja cadastrado.", e.getCause());
        }
    }

    public boolean verificaEmailExistente(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public UsuarioDTO buscarUsuarioPorEmail(String email) {

        try {
            return usuarioConverter.paraUsuarioDTO(usuarioRepository.findByEmail(email).orElseThrow(() ->
                    new ResourceNotFoundException("Email not found." + email)));
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Email not found" + email);
        }
    }

    public void deletaUsuarioPorEmail(String email) {
        usuarioRepository.deleteByEmail(email);
    }

    public UsuarioDTO atualizaDadosUsuario(String token, UsuarioDTO dto) {
        String email = jwtUtil.extractUsername(token.substring(7));

        dto.setSenha(dto.getSenha() != null ? passwordEncoder.encode(dto.getSenha()) : null);

        Usuario usuarioEntity = usuarioRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("Email not found."));
        Usuario usuario = usuarioConverter.updateUsuario(dto, usuarioEntity);
        //usuario.setSenha(passwordEncoder.encode(usuario.getPassword()));

        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }

    public EnderecoDTO atualizaEndereco(Long idEndereco, EnderecoDTO enderecoDTO) {
        Endereco enderecoEntity = enderecoRepository.findById(idEndereco).orElseThrow(() ->
                new ResourceNotFoundException("Id not found." + idEndereco));
        Endereco endereco = usuarioConverter.updateEndereco(enderecoDTO, enderecoEntity);
        return usuarioConverter.paraEnderecoDTO(enderecoRepository.save(endereco));
    }

    public TelefoneDTO atualizaTelefone(Long idTelefone, TelefoneDTO telefoneDTO) {
        Telefone telefoneEntity = telefoneRepository.findById(idTelefone).orElseThrow(
                () -> new ResourceNotFoundException("Id not found" + idTelefone));
        Telefone telefone = usuarioConverter.updateTelefone(telefoneDTO, telefoneEntity);
        return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(telefone));
    }

    //Metodos cadastram Endereco e Telefone de um usuario ja existente

    public EnderecoDTO cadastraEndereco(String token, EnderecoDTO dto) {
        String email = jwtUtil.extractUsername(token.substring(7));
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("Email not found." + email));
        Endereco endereco = usuarioConverter.paraEnderecoEntity(dto, usuario.getId());
        Endereco enderecoEntity = enderecoRepository.save(endereco);
        return usuarioConverter.paraEnderecoDTO(enderecoEntity);
    }

    public TelefoneDTO cadastraTelefone(String token, TelefoneDTO dto) {
        String email = jwtUtil.extractUsername(token.substring(7));
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("Email not found." + email));

        Telefone telefone = usuarioConverter.paraTelefoneEntity(dto, usuario.getId());
        Telefone telefoneEntity = telefoneRepository.save(telefone);
        return usuarioConverter.paraTelefoneDTO(telefoneEntity);
    }

}
