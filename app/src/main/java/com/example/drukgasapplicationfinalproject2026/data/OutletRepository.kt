package com.example.drukgasapplicationfinalproject2026.data

object OutletRepository {

    val lpgOutlets = listOf(
        Outlet("LPG001", "Motithang LPG Outlet", "Motithang", "BOD", "LPG"),
        Outlet("LPG002", "Chubachu LPG Outlet", "Chubachu", "DPCL", "LPG"),
        Outlet("LPG003", "Langdro LPG Outlet", "Langdro", "DPCL", "LPG"),
        Outlet("LPG004", "Changzamtog LPG Outlet", "Changzamtog", "DPDPL", "LPG")
    )

    val fuelOutlets = listOf(
        Outlet("FUEL006", "Ramtokto Fuel Retail Outlet", "Ramtokto", "STCBL", "Fuel", "Thimphu"),
        Outlet("FUEL008", "Jungshina Fuel Retail Outlet", "Jungshina", "STCBL", "Fuel", "Thimphu"),
        Outlet("mebesa_fro_chukha", "Mebesa FRO", "Mebesa", "STCBL", "Fuel", "Chukha"),
        Outlet("chamkuna_fro_phuentsholing", "Chamkuna FRO", "Phuentsholing", "STCBL", "Fuel", "Chukha"),
        Outlet("samtenling_fro_sarpang", "Samtenling FRO", "Samtenling", "STCBL", "Fuel", "Sarpang"),
        Outlet("damphu_fro_tsirang", "Damphu FRO", "Damphu", "STCBL", "Fuel", "Tsirang"),
        Outlet("dhamdum_fro_samtse", "Dhamdum FRO", "Dhamdum", "STCBL", "Fuel", "Samtse"),
        Outlet("samdrupjongkhar_fro", "Samdrupjongkhar FRO", "Samdrupjongkhar", "STCBL", "Fuel", "Samdrup Jongkhar"),
        Outlet("nobding_fro_wangduephodrang", "Nobding FRO", "Nobding", "STCBL", "Fuel", "Wangdue Phodrang"),
        Outlet("chumey_fro_bumthang", "Chumey FRO", "Chumey", "STCBL", "Fuel", "Bumthang"),
        Outlet("dagana_fro_dagana", "Dagana FRO", "Dagana", "STCBL", "Fuel", "Dagana"),
        Outlet("doksum_fro_trashiyangtse", "Doksum FRO", "Doksum", "STCBL", "Fuel", "Trashiyangtse"),
        Outlet("nganglam_fro_pemagatshel", "Nganglam FRO", "Nganglam", "STCBL", "Fuel", "Pemagatshel"),
        Outlet("lhamoizingkha_fro_dagana", "Lhamoizingkha FRO", "Lhamoizingkha", "STCBL", "Fuel", "Dagana")
    )

    val reports = listOf(

        // LPG prototype reports

        Report(
            id = "R001",
            outletId = "LPG001",
            availability = Availability.AVAILABLE,
            queueLevel = QueueLevel.SHORT,
            reportedAt = System.currentTimeMillis() - (12 * 60 * 1000),
            note = "I reached here at 13:00 in the afternoon and there wasn't alot of people lining up for queue. Got my gas in around 10 minutes.",
            imageUri = "report2",
            userName = "Sonam345"
        ),

        Report(
            id = "R002",
            outletId = "LPG002",
            availability = Availability.AVAILABLE,
            queueLevel = QueueLevel.MODERATE,
            reportedAt = System.currentTimeMillis() - (34 * 60 * 1000),
            note = "Cylinders reported available. Moderate queue.",
            imageUri = "report3",
            userName = "IamMrCar"
        ),

        Report(
            id = "R003",
            outletId = "LPG003",
            availability = Availability.UNKNOWN,
            queueLevel = QueueLevel.UNKNOWN,
            reportedAt = System.currentTimeMillis() - (24 * 60 * 60 * 1000),
            note = "",
            imageUri = "report1",
            userName = "DrukTraveler"
        ),

        Report(
            id = "R004",
            outletId = "LPG004",
            availability = Availability.LOW,
            queueLevel = QueueLevel.LONG,
            reportedAt = System.currentTimeMillis() - (8 * 60 * 1000),
            note = "About 15 people waiting. Remaining stock is uncertain."
        )
    )

    fun getOutlets(type: String): List<Outlet> {
        return when (type) {
            "LPG" -> lpgOutlets
            "Fuel" -> fuelOutlets
            else -> emptyList()
        }
    }

    fun getLatestReport(outletId: String): Report? {
        return reports
            .filter { it.outletId == outletId }
            .firstOrNull()
    }
}