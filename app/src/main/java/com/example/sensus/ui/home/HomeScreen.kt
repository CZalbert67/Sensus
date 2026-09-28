package com.example.sensus.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sensus.R
import com.example.sensus.data.Establishment
import com.example.sensus.data.MockDataProvider
import com.example.sensus.data.StoreCategory
import com.example.sensus.theme.SensusAmber
import com.example.sensus.theme.SensusAmberDark
import com.example.sensus.theme.SensusBackground
import com.example.sensus.theme.SensusBeige
import com.example.sensus.theme.SensusCardBorder
import com.example.sensus.theme.SensusDiscountTag
import com.example.sensus.theme.SensusPurple
import com.example.sensus.theme.SensusPurpleDark
import com.example.sensus.theme.SensusPurpleLight
import com.example.sensus.theme.SensusRating
import com.example.sensus.theme.SensusTextMuted
import com.example.sensus.theme.SensusTextPrimary
import com.example.sensus.theme.SensusTextSecondary
import com.example.sensus.ui.common.getCategoryIcon

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onEstablishmentClick: (Establishment) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(StoreCategory.ALL) }
    var currentZone by remember { mutableStateOf(MockDataProvider.zones.first()) }
    var showLocationDialog by remember { mutableStateOf(false) }

    val filteredEstablishments = remember(selectedCategory, searchQuery) {
        MockDataProvider.establishments.filter { est ->
            val matchesCategory = (selectedCategory == StoreCategory.ALL) || (est.category == selectedCategory)
            val matchesQuery = searchQuery.isBlank() ||
                    est.name.contains(searchQuery, ignoreCase = true) ||
                    est.discountTitle.contains(searchQuery, ignoreCase = true) ||
                    est.address.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }

    if (showLocationDialog) {
        LocationDialog(
            selectedZone = currentZone,
            onZoneSelected = {
                currentZone = it
                showLocationDialog = false
            },
            onDismiss = { showLocationDialog = false }
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SensusBackground),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // TOP HEADER BAR
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Location Selector Pill
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White,
                    shadowElevation = 2.dp,
                    modifier = Modifier.clickable { showLocationDialog = true }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Ubicación",
                            tint = SensusPurple,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = currentZone.name,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = SensusPurple
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Cambiar",
                            tint = SensusTextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                // Sensus mini badge / icon
                Surface(
                    shape = CircleShape,
                    color = SensusAmber,
                    modifier = Modifier.size(38.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(id = R.drawable.sensus_logo),
                            contentDescription = "Sensus",
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            }
        }

        // SEARCH BAR
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        text = "Buscar abarrotes, comida, farmacia...",
                        fontSize = 14.sp,
                        color = SensusTextMuted
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = SensusPurple,
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Limpiar",
                            tint = SensusTextMuted,
                            modifier = Modifier
                                .size(20.dp)
                                .clickable { searchQuery = "" }
                        )
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = SensusPurple,
                    unfocusedBorderColor = SensusCardBorder
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            )
        }

        // PROMOTIONAL HERO BANNER
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(SensusAmber, SensusAmberDark)
                            )
                        )
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = SensusPurple
                            ) {
                                Text(
                                    text = "OFERTAS DEL DÍA",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Ahorra hasta 50% en tu colonia",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = SensusPurpleDark,
                                lineHeight = 22.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Descubre comercios locales con descuentos activos cerca de ti.",
                                fontSize = 12.sp,
                                color = SensusPurpleDark.copy(alpha = 0.85f)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Image(
                            painter = painterResource(id = R.drawable.sensus_logo),
                            contentDescription = "Logo Sensus",
                            modifier = Modifier.size(76.dp)
                        )
                    }
                }
            }
        }

        // CATEGORY CHIPS WITH VECTOR ICONS
        item {
            Text(
                text = "Categorías",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = SensusTextPrimary,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(StoreCategory.values()) { category ->
                    val isSelected = category == selectedCategory
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = category },
                        leadingIcon = {
                            Icon(
                                imageVector = getCategoryIcon(category),
                                contentDescription = category.displayName,
                                modifier = Modifier.size(16.dp),
                                tint = if (isSelected) Color.White else SensusPurple
                            )
                        },
                        label = {
                            Text(
                                text = category.displayName,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SensusPurple,
                            selectedLabelColor = Color.White,
                            containerColor = Color.White,
                            labelColor = SensusTextPrimary
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = if (isSelected) SensusPurple else SensusCardBorder,
                            selectedBorderColor = SensusPurple
                        )
                    )
                }
            }
        }

        // ESTABLISHMENTS SECTION HEADER
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 18.dp, bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Descuentos Cerca de Ti (${filteredEstablishments.size})",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = SensusTextPrimary
                )
                Text(
                    text = "Orden: Cercanía",
                    fontSize = 12.sp,
                    color = SensusTextSecondary,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // ESTABLISHMENTS LIST
        if (filteredEstablishments.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.SearchOff,
                            contentDescription = "Sin resultados",
                            tint = SensusTextMuted,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No se encontraron ofertas en esta categoría",
                            fontWeight = FontWeight.SemiBold,
                            color = SensusTextSecondary,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        } else {
            items(filteredEstablishments) { establishment ->
                EstablishmentCard(
                    establishment = establishment,
                    onClick = { onEstablishmentClick(establishment) }
                )
            }
        }
    }
}

@Composable
fun EstablishmentCard(
    establishment: Establishment,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 7.dp)
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Category Vector Icon, Distance and Discount Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(SensusBeige.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = getCategoryIcon(establishment.category),
                            contentDescription = establishment.category.displayName,
                            tint = SensusPurple,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = establishment.category.displayName,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SensusPurple
                        )
                        Text(
                            text = "A ${establishment.formattedDistance}",
                            fontSize = 11.sp,
                            color = SensusTextSecondary
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SensusDiscountTag
                ) {
                    Text(
                        text = "-${establishment.discountPercent}% OFF",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Establishment Name & Discount Title
            Text(
                text = establishment.name,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = SensusTextPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = establishment.discountTitle,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = SensusPurpleLight
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Address
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = SensusTextMuted,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = establishment.address,
                    fontSize = 12.sp,
                    color = SensusTextSecondary
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 10.dp),
                color = SensusBackground
            )

            // Footer: Rating and Action button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = SensusRating,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "${establishment.rating} (${establishment.reviewCount})",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SensusTextPrimary
                    )
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = SensusPurple.copy(alpha = 0.08f),
                    modifier = Modifier.clickable { onClick() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Ver Descuento",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = SensusPurple
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = SensusPurple,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}
