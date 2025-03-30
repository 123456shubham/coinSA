package com.example.coinsa.model

import java.io.Serializable

data class CryptoCurrency(
    val auditInfoList: List<AuditInfo?>?,
    val badges: List<String?>?,
    val circulatingSupply: String?,
    val cmcRank: String?,
    val dateAdded: String?,
    val id: String?,
    val isActive: String?,
    val isAudited: Boolean?,
    val lastUpdated: String?,
    val marketPairCount: String?,
    val maxSupply: String?,
    val name: String?,
    val platform: Platform?,
    val quotes: List<Quote?>?,
    val selfReportedCirculatingSupply: Double?,
    val slug: String?,
    val symbol: String?,
    val tags: List<String?>?,
    val totalSupply: String?
):Serializable{
    override fun toString(): String {
        return super.toString()
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }
}