package br.pismo.techcase.flowbank.adapters.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import br.pismo.techcase.flowbank.adapters.out.persistence.jpa.OperationTypeRepository;
import br.pismo.techcase.flowbank.infrastructure.persistence.model.OperationTypeEntity;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OperationTypePersistenceAdapterTest {
    @Mock
    private OperationTypeRepository repository;

    @InjectMocks
    private OperationTypePersistenceAdapter adapter;

    @Test
    @DisplayName(value = "FindById returns operation type when found")
    void findByIdReturnsOperationTypeWhenFound() {
        var id = 1L;
        var entity = new OperationTypeEntity();
        entity.setId(id);
        when(repository.findById(id)).thenReturn(Optional.of(entity));

        var result = adapter.findById(id);
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(id);
    }

    @Test
    @DisplayName(value = "FindById returns empty when not found")
    void findByIdReturnsEmptyWhenNotFound() {
        var id = 2L;
        when(repository.findById(id)).thenReturn(Optional.empty());
        var result = adapter.findById(id);
        assertThat(result).isEmpty();
    }
}

