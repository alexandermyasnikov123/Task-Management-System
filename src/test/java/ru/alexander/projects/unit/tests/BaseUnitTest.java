package ru.alexander.projects.unit.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;
import org.mockito.verification.VerificationMode;

import java.util.function.Function;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

public class BaseUnitTest {
    protected final Function<Boolean, VerificationMode> verification = condition -> condition ? times(1) : never();

    private AutoCloseable closeable;

    @BeforeEach
    public void openMocks() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void releaseMocks() throws Exception {
        closeable.close();
    }
}
