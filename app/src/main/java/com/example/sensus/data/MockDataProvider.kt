package com.example.sensus.data

object MockDataProvider {

    val zones = listOf(
        LocationZone("gps", "Mi Ubicación GPS (En vivo)", "Precisión alta • 15 comercios cerca"),
        LocationZone("condesa", "Condesa, CDMX", "Parque México y alrededores"),
        LocationZone("roma", "Roma Norte, CDMX", "Álvaro Obregón y Colima"),
        LocationZone("polanco", "Polanco, CDMX", "Av. Horacio y Masaryk"),
        LocationZone("coyoacan", "Coyoacán Centro, CDMX", "Jardín Hidalgo y Centenario")
    )

    val establishments = listOf(
        Establishment(
            id = "est-1",
            name = "Abarrotes & Cremería Don Lupe",
            category = StoreCategory.GROCERY,
            discountPercent = 25,
            discountTitle = "25% en lácteos y embutidos",
            discountDescription = "Válido en compras mínimas de \$150 MXN pagando en efectivo o transferencia.",
            couponCode = "DONLUPE25",
            distanceMeters = 150,
            rating = 4.9,
            reviewCount = 184,
            address = "Calle Amsterdam 142, Col. Condesa",
            schedule = "Abierto • Cierra 22:00 hrs",
            isFeatured = true
        ),
        Establishment(
            id = "est-2",
            name = "Taquería El Pastor Feliz",
            category = StoreCategory.FOOD,
            discountPercent = 35,
            discountTitle = "3x2 en tacos al pastor",
            discountDescription = "Aplica en todos los tacos al pastor con queso y refresco de 18:00 a 22:00 hrs.",
            couponCode = "PASTOR3X2",
            distanceMeters = 280,
            rating = 4.8,
            reviewCount = 312,
            address = "Av. Michoacán 78, Col. Condesa",
            schedule = "Abierto • Cierra 01:00 hrs",
            isFeatured = true
        ),
        Establishment(
            id = "est-3",
            name = "Café & Tostaduría Don Gato",
            category = StoreCategory.CAFE,
            discountPercent = 30,
            discountTitle = "30% en Flat White y Repostería",
            discountDescription = "Presenta el cupón Sensus y obtén 30% en cualquier café de especialidad y pastel.",
            couponCode = "GATOCAFE30",
            distanceMeters = 340,
            rating = 4.9,
            reviewCount = 98,
            address = "Calle Tamaulipas 54, Col. Condesa",
            schedule = "Abierto • Cierra 20:30 hrs",
            isFeatured = true
        ),
        Establishment(
            id = "est-4",
            name = "Panadería Artesanal Masa Madre",
            category = StoreCategory.BAKERY,
            discountPercent = 20,
            discountTitle = "20% en pan dulce después de las 6 PM",
            discountDescription = "Llévate conchas, roles de canela y hogazas con descuento nocturno especial.",
            couponCode = "MASA20",
            distanceMeters = 420,
            rating = 4.7,
            reviewCount = 145,
            address = "Av. Mazatlán 110, Col. Condesa",
            schedule = "Abierto • Cierra 21:00 hrs",
            isFeatured = false
        ),
        Establishment(
            id = "est-5",
            name = "Super Express Las Torres",
            category = StoreCategory.GROCERY,
            discountPercent = 15,
            discountTitle = "15% en canasta básica y botanas",
            discountDescription = "Aplica en abarrotes, bebidas y productos de limpieza seleccionados.",
            couponCode = "EXPRESS15",
            distanceMeters = 560,
            rating = 4.5,
            reviewCount = 76,
            address = "Av. Vicente Suárez 92, Col. Condesa",
            schedule = "Abierto 24 Horas",
            isFeatured = false
        ),
        Establishment(
            id = "est-6",
            name = "Farmacia & Perfumería Central",
            category = StoreCategory.PHARMACY,
            discountPercent = 40,
            discountTitle = "40% en higiene y cuidado personal",
            discountDescription = "Descuento directo en bloqueadores solares, cremas corporales y vitaminas.",
            couponCode = "FARMACENTRAL40",
            distanceMeters = 610,
            rating = 4.6,
            reviewCount = 89,
            address = "Calle Campeche 210, Col. Condesa",
            schedule = "Abierto • Cierra 23:00 hrs",
            isFeatured = false
        ),
        Establishment(
            id = "est-7",
            name = "Pizzería Nápoles al Horno",
            category = StoreCategory.FOOD,
            discountPercent = 50,
            discountTitle = "Segunda pizza al 50%",
            discountDescription = "En pizzas medianas y familiares de especialidad para llevar o en mesa.",
            couponCode = "PIZZA50",
            distanceMeters = 750,
            rating = 4.8,
            reviewCount = 230,
            address = "Calle Alfonso Reyes 188, Col. Condesa",
            schedule = "Abierto • Cierra 23:30 hrs",
            isFeatured = true
        )
    )

    val sampleSavedCoupons = listOf(
        UserCoupon(
            id = "c-1",
            establishmentId = "est-3",
            establishmentName = "Café & Tostaduría Don Gato",
            discountTitle = "30% en Flat White y Repostería",
            couponCode = "GATOCAFE30",
            expiresAt = "Expira en 3 días",
            isRedeemed = false
        ),
        UserCoupon(
            id = "c-2",
            establishmentId = "est-1",
            establishmentName = "Abarrotes & Cremería Don Lupe",
            discountTitle = "25% en lácteos y embutidos",
            couponCode = "DONLUPE25",
            expiresAt = "Expira hoy",
            isRedeemed = false
        ),
        UserCoupon(
            id = "c-3",
            establishmentId = "est-2",
            establishmentName = "Taquería El Pastor Feliz",
            discountTitle = "3x2 en tacos al pastor",
            couponCode = "PASTOR3X2",
            expiresAt = "Canjeado el 25 Sep",
            isRedeemed = true
        )
    )
}
