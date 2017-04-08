package acmeinvoice.unit.service;

import acmeinvoice.repository.InvoiceRepository;
import acmeinvoice.service.InvoiceService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class InvoiceServiceTest {
    @Mock
    private InvoiceRepository repository;
    private InvoiceService service;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        service = new InvoiceService(repository);
    }

    @Test
    public void findBy() throws Exception {
        service.findBy(01L);
        verify(repository).findByCustomerId(01L);
    }
}