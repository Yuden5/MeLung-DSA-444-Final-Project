package com.example.drukgasapplicationfinalproject2026.data

object DrukGasPromptBuilder {

    fun buildPrompt(
        reports: List<Report>,
        userQuestion: String
    ): String {

        val reportText = reports.joinToString("\n") { report ->

            val outlet = OutletRepository
                .getOutlets(
                    if (report.outletId.startsWith("LPG")) {
                        "LPG"
                    } else {
                        "Fuel"
                    }
                )
                .find { it.id == report.outletId }

            val outletName =
                outlet?.name ?: "Unknown outlet"

            val area =
                outlet?.area ?: "Unknown area"

            val operator =
                outlet?.operator ?: "Unknown operator"

            """
            Outlet: $outletName
            Area: $area
            Operator: $operator
            Availability: ${report.availability}
            Queue: ${report.queueLevel}
            Reported: ${formatReportTime(report.reportedAt)}
            Note: ${report.note.ifBlank { "No additional note." }}
            """.trimIndent()
        }

        return """
            You are DrukGas, a community information assistant
            for LPG and fuel availability in Thimphu, Bhutan.

            Your job is to help users understand RECENT COMMUNITY
            REPORTS about LPG and fuel outlets.

            IMPORTANT RULES:
            - Use ONLY the reports provided below.
            - Do not invent availability, queues, prices, or outlets.
            - Do not claim that information is official.
            - Community reports can become outdated quickly.
            - If a report is old, clearly mention that.
            - UNKNOWN means there is no reliable recent information.
            - Never interpret UNKNOWN as out of stock.
            - Do not predict future stock.
            - Keep answers concise and practical.
            - When useful, mention the outlet name and area.
            - If the reports cannot answer the question, say so honestly.

            CURRENT COMMUNITY REPORTS:

            $reportText

            USER QUESTION:
            $userQuestion

            Answer the user's question based only on the
            information above.
        """.trimIndent()
    }
}