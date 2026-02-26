package poly.edu.java5_asm.DevNgoc;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import poly.edu.java5_asm.common.exception.AddressException;
import poly.edu.java5_asm.common.exception.AddressNotFoundException;
import poly.edu.java5_asm.module.address.dto.request.CreateAddressRequest;
import poly.edu.java5_asm.module.address.dto.response.AddressResponse;
import poly.edu.java5_asm.module.address.entity.Address;
import poly.edu.java5_asm.module.address.repository.AddressRepository;
import poly.edu.java5_asm.module.address.service.AddressServiceImpl;
import poly.edu.java5_asm.module.user.entity.User;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test cases cho AddressService - Thiên Ngọc
 * Test cases: ADDR_001 đến ADDR_008
 */
@RunWith(MockitoJUnitRunner.class)
public class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressServiceImpl addressService;

    private User testUser;
    private Address testAddress;
    private CreateAddressRequest createAddressRequest;

    @Before
    public void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .fullName("Test User")
                .build();

        testAddress = Address.builder()
                .id(1L)
                .user(testUser)
                .recipientName("Test User")
                .phone("0123456789")
                .addressLine1("123 Main St")
                .city("Hanoi")
                .isDefault(true)
                .build();

        createAddressRequest = CreateAddressRequest.builder()
                .recipientName("Test User")
                .phone("0123456789")
                .addressLine1("123 Main St")
                .city("Hanoi")
                .state("Hanoi")
                .postalCode("100000")
                .country("Vietnam")
                .isDefault(false)
                .build();
    }

    /**
     * ADDR_001: testCreateAddress_Success
     * Tạo địa chỉ mới thành công
     */
    @Test
    public void testCreateAddress_Success() {
        // Given
        when(addressRepository.countByUser(testUser)).thenReturn(0L);
        when(addressRepository.save(any(Address.class))).thenReturn(testAddress);

        // When
        AddressResponse result = addressService.createAddress(testUser, createAddressRequest);

        // Then
        assertNotNull("AddressResponse should not be null", result);
        assertEquals("Recipient name should match", "Test User", result.getRecipientName());
        assertEquals("City should match", "Hanoi", result.getCity());
        verify(addressRepository, times(1)).countByUser(testUser);
        verify(addressRepository, times(1)).save(any(Address.class));
    }

    /**
     * ADDR_002: testCreateAddress_InvalidData
     * Tạo địa chỉ với dữ liệu không hợp lệ
     */
    @Test(expected = AddressException.class)
    public void testCreateAddress_InvalidData() {
        // Given
        when(addressRepository.countByUser(testUser)).thenReturn(5L);

        // When
        addressService.createAddress(testUser, createAddressRequest);

        // Then - expect AddressException
    }

    /**
     * ADDR_003: testUpdateAddress_Success
     * Cập nhật địa chỉ thành công
     */
    @Test
    public void testUpdateAddress_Success() {
        // Given
        CreateAddressRequest updateRequest = CreateAddressRequest.builder()
                .recipientName("Updated Name")
                .phone("0987654321")
                .addressLine1("456 New St")
                .city("Ho Chi Minh")
                .state("HCM")
                .postalCode("700000")
                .country("Vietnam")
                .isDefault(false)
                .build();

        when(addressRepository.findByIdAndUser(1L, testUser)).thenReturn(Optional.of(testAddress));
        when(addressRepository.save(any(Address.class))).thenReturn(testAddress);

        // When
        AddressResponse result = addressService.updateAddress(testUser, 1L, updateRequest);

        // Then
        assertNotNull("AddressResponse should not be null", result);
        verify(addressRepository, times(1)).findByIdAndUser(1L, testUser);
        verify(addressRepository, times(1)).save(any(Address.class));
    }

    /**
     * ADDR_004: testDeleteAddress_Success
     * Xóa địa chỉ thành công
     */
    @Test
    public void testDeleteAddress_Success() {
        // Given
        Address nonDefaultAddress = Address.builder()
                .id(2L)
                .user(testUser)
                .recipientName("Test User")
                .phone("0123456789")
                .addressLine1("456 Second St")
                .city("Hanoi")
                .isDefault(false)
                .build();

        when(addressRepository.findByIdAndUser(2L, testUser)).thenReturn(Optional.of(nonDefaultAddress));
        doNothing().when(addressRepository).delete(nonDefaultAddress);

        // When
        addressService.deleteAddress(testUser, 2L);

        // Then
        verify(addressRepository, times(1)).findByIdAndUser(2L, testUser);
        verify(addressRepository, times(1)).delete(nonDefaultAddress);
    }

    /**
     * ADDR_005: testDeleteAddress_DefaultAddress
     * Xóa địa chỉ mặc định
     */
    @Test
    public void testDeleteAddress_DefaultAddress() {
        // Given
        Address secondAddress = Address.builder()
                .id(2L)
                .user(testUser)
                .recipientName("Test User")
                .phone("0123456789")
                .addressLine1("456 Second St")
                .city("Hanoi")
                .isDefault(false)
                .build();

        when(addressRepository.findByIdAndUser(1L, testUser)).thenReturn(Optional.of(testAddress));
        when(addressRepository.findByUserOrderByIsDefaultDescCreatedAtDesc(testUser))
                .thenReturn(Arrays.asList(secondAddress));
        when(addressRepository.save(any(Address.class))).thenReturn(secondAddress);
        doNothing().when(addressRepository).delete(testAddress);

        // When
        addressService.deleteAddress(testUser, 1L);

        // Then
        verify(addressRepository, times(1)).findByIdAndUser(1L, testUser);
        verify(addressRepository, times(1)).delete(testAddress);
        verify(addressRepository, times(1)).save(any(Address.class));
    }

    /**
     * ADDR_006: testSetDefaultAddress_Success
     * Đặt địa chỉ mặc định
     */
    @Test
    public void testSetDefaultAddress_Success() {
        // Given
        Address secondAddress = Address.builder()
                .id(2L)
                .user(testUser)
                .recipientName("Test User")
                .phone("0123456789")
                .addressLine1("456 Second St")
                .city("Hanoi")
                .isDefault(false)
                .build();

        when(addressRepository.findByIdAndUser(2L, testUser)).thenReturn(Optional.of(secondAddress));
        when(addressRepository.findByUserAndIsDefaultTrue(testUser)).thenReturn(Optional.of(testAddress));
        when(addressRepository.save(any(Address.class))).thenReturn(secondAddress);

        // When
        AddressResponse result = addressService.setDefaultAddress(testUser, 2L);

        // Then
        assertNotNull("AddressResponse should not be null", result);
        verify(addressRepository, times(1)).findByIdAndUser(2L, testUser);
        verify(addressRepository, times(2)).save(any(Address.class));
    }

    /**
     * ADDR_007: testGetUserAddresses_Success
     * Lấy danh sách địa chỉ của user
     */
    @Test
    public void testGetUserAddresses_Success() {
        // Given
        Address address2 = Address.builder()
                .id(2L)
                .user(testUser)
                .recipientName("Test User 2")
                .phone("0987654321")
                .addressLine1("456 Second St")
                .city("HCM")
                .isDefault(false)
                .build();

        Address address3 = Address.builder()
                .id(3L)
                .user(testUser)
                .recipientName("Test User 3")
                .phone("0111222333")
                .addressLine1("789 Third St")
                .city("Da Nang")
                .isDefault(false)
                .build();

        List<Address> addresses = Arrays.asList(testAddress, address2, address3);
        when(addressRepository.findByUserOrderByIsDefaultDescCreatedAtDesc(testUser)).thenReturn(addresses);

        // When
        List<AddressResponse> result = addressService.getUserAddresses(testUser);

        // Then
        assertNotNull("Result should not be null", result);
        assertEquals("Should return 3 addresses", 3, result.size());
        verify(addressRepository, times(1)).findByUserOrderByIsDefaultDescCreatedAtDesc(testUser);
    }

    /**
     * ADDR_008: testGetDefaultAddress_Success
     * Lấy địa chỉ mặc định
     */
    @Test
    public void testGetDefaultAddress_Success() {
        // Given
        when(addressRepository.findByUserAndIsDefaultTrue(testUser)).thenReturn(Optional.of(testAddress));

        // When
        AddressResponse result = addressService.getDefaultAddress(testUser);

        // Then
        assertNotNull("AddressResponse should not be null", result);
        assertTrue("Should be default address", result.getIsDefault());
        assertEquals("Address ID should be 1", Long.valueOf(1L), result.getId());
        verify(addressRepository, times(1)).findByUserAndIsDefaultTrue(testUser);
    }
}
