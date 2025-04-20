package dev.alireza.fms.market

import org.springframework.data.repository.ListCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AssetRepositry: ListCrudRepository<Asset, String> {
}