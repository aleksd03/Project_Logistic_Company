package org.informatics.service;

import org.informatics.entity.Company;
import org.informatics.entity.Office;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OfficeServiceTest {

    private OfficeService officeService;
    private Company company;

    @BeforeEach
    void setUp() {
        officeService = new OfficeService();

        company = new Company();
        company.setId(1L);
        company.setName("ALVAS Logistics");
    }

    // 1️⃣ Create office successfully
    @Test
    void createOffice_shouldCreateOfficeSuccessfully() {
        Office office = officeService.createOffice(
                "Sofia, Vitosha 15",
                company
        );

        assertNotNull(office);
        assertEquals("Sofia, Vitosha 15", office.getAddress());
        assertEquals(company, office.getCompany());
    }

    // 2️⃣ Validation: null address
    @Test
    void createOffice_shouldThrowException_whenAddressIsNull() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> officeService.createOffice(null, company)
        );

        assertTrue(ex.getMessage().contains("address"));
    }

    // 3️⃣ Validation: empty address
    @Test
    void createOffice_shouldThrowException_whenAddressIsEmpty() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> officeService.createOffice("   ", company)
        );

        assertTrue(ex.getMessage().contains("address"));
    }

    // 4️⃣ Validation: null company
    @Test
    void createOffice_shouldThrowException_whenCompanyIsNull() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> officeService.createOffice("Plovdiv Center", null)
        );

        assertTrue(ex.getMessage().contains("company"));
    }

    // 5️⃣ Fetch office by ID
    @Test
    void getOfficeById_shouldReturnOffice_whenExists() {
        Office office = officeService.createOffice(
                "Varna Main Office",
                company
        );

        Office found = officeService.getOfficeById(office.getId());

        assertNotNull(found);
        assertEquals(office.getId(), found.getId());
    }

    // 6️⃣ Fetch non-existing office
    @Test
    void getOfficeById_shouldReturnNull_whenNotExists() {
        Office found = officeService.getOfficeById(999L);

        assertNull(found);
    }

    // 7️⃣ Get all offices
    @Test
    void getAllOffices_shouldReturnList() {
        officeService.createOffice("Burgas Office", company);

        assertFalse(officeService.getAllOffices().isEmpty());
    }
}
