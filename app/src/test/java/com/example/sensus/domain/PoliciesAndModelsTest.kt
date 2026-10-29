package com.example.sensus.domain

import com.example.sensus.domain.model.GuestRadiusPolicy
import com.example.sensus.domain.model.HorarioDia
import com.example.sensus.domain.model.MerchantProfile
import com.example.sensus.domain.model.PasswordPolicy
import com.example.sensus.domain.model.UserProfile
import com.example.sensus.ui.common.formatRadiusText
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PoliciesAndModelsTest {

    // ==========================================
    // PasswordPolicy Tests
    // ==========================================

    @Test
    fun passwordPolicy_shouldAccept_strongPasswords() {
        assertTrue(PasswordPolicy.isValid("ValidPassword123#"))
        assertTrue(PasswordPolicy.isValid("Secure2026Pass!"))
        assertTrue(PasswordPolicy.isValid("Abcdefg8"))
    }

    @Test
    fun passwordPolicy_shouldReject_weakPasswords() {
        assertFalse("Menos de 8 caracteres", PasswordPolicy.isValid("Short1#"))
        assertFalse("Sin mayúsculas", PasswordPolicy.isValid("lowercase123#"))
        assertFalse("Sin minúsculas", PasswordPolicy.isValid("UPPERCASE123#"))
        assertFalse("Sin dígitos", PasswordPolicy.isValid("OnlyLettersHere#"))
        assertFalse("Cadena vacía", PasswordPolicy.isValid(""))
    }

    // ==========================================
    // GuestRadiusPolicy Tests
    // ==========================================

    @Test
    fun guestRadiusPolicy_shouldEnforceStrict2KmMax() {
        assertTrue(GuestRadiusPolicy.isRadiusAllowedForGuest(0.5f))
        assertTrue(GuestRadiusPolicy.isRadiusAllowedForGuest(1.0f))
        assertTrue(GuestRadiusPolicy.isRadiusAllowedForGuest(2.0f))

        assertFalse(GuestRadiusPolicy.isRadiusAllowedForGuest(2.1f))
        assertFalse(GuestRadiusPolicy.isRadiusAllowedForGuest(3.0f))
        assertFalse(GuestRadiusPolicy.isRadiusAllowedForGuest(5.0f))
    }

    @Test
    fun guestRadiusPolicy_clampRadius_shouldClampOnlyForGuest() {
        // Para usuario invitado: recorta a 2.0 km si excede
        assertEquals(2.0f, GuestRadiusPolicy.clampRadius(5.0f, isGuest = true), 0.001f)
        assertEquals(2.0f, GuestRadiusPolicy.clampRadius(3.5f, isGuest = true), 0.001f)
        assertEquals(1.5f, GuestRadiusPolicy.clampRadius(1.5f, isGuest = true), 0.001f)

        // Para usuario registrado: no recorta
        assertEquals(5.0f, GuestRadiusPolicy.clampRadius(5.0f, isGuest = false), 0.001f)
        assertEquals(3.5f, GuestRadiusPolicy.clampRadius(3.5f, isGuest = false), 0.001f)
    }

    // ==========================================
    // formatRadiusText Tests
    // ==========================================

    @Test
    fun formatRadiusText_shouldFormatCorrectly() {
        assertEquals("500 m", formatRadiusText(0.5f))
        assertEquals("1 km", formatRadiusText(1.0f))
        assertEquals("1.5 km", formatRadiusText(1.5f))
        assertEquals("2 km", formatRadiusText(2.0f))
        assertEquals("5 km", formatRadiusText(5.0f))
    }

    // ==========================================
    // Domain Models Tests
    // ==========================================

    @Test
    fun userProfile_shouldInstantiateWithDefaults() {
        val profile = UserProfile(nombreCompleto = "Ana Torres", correo = "ana@sensus.com")
        assertEquals("Ana Torres", profile.nombreCompleto)
        assertEquals("ana@sensus.com", profile.correo)
        assertEquals(null, profile.fotoPerfilUrl)
        assertTrue(profile.roles.contains("Consumidor"))
    }

    @Test
    fun merchantProfile_shouldSupportHorarios() {
        val horarios = listOf(
            HorarioDia("Lunes", "08:00", "20:00", false),
            HorarioDia("Domingo", null, null, true)
        )
        val merchant = MerchantProfile(
            nombreComercial = "Panadería",
            direccion = "Calle 1",
            codigoPostal = "06000",
            horarios = horarios
        )
        assertEquals(2, merchant.horarios.size)
        assertTrue(merchant.horarios[1].estaCerrado)
    }
}
