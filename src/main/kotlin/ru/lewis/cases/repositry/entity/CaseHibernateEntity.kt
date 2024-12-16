package ru.lewis.cases.repositry.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "cases_player_data")
class CaseHibernateEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(name = "player", nullable = false)
    var uuid: UUID? = null

    @Column(name = "case_id", nullable = false)
    var caseId: Int? = null

    @Column(name = "count", nullable = false)
    var count: Int? = null

    constructor(
        uuid: UUID,
        caseId: Int,
        count: Int
    ): this() {
        this.uuid = uuid
        this.caseId = caseId
        this.count = count
    }

}