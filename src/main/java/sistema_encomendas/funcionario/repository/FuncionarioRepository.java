package sistema_encomendas.funcionario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sistema_encomendas.funcionario.entity.Funcionario;
import sistema_encomendas.shared.enums.SetorFuncionario;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    List<Funcionario> findByAtivo(boolean ativo);

    List<Funcionario> findBySetor(SetorFuncionario setor);

    List<Funcionario> findBySetorAndAtivo(SetorFuncionario setor, boolean ativo);

    List<Funcionario> findByNomeContainingIgnoreCase(String nome);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);
}