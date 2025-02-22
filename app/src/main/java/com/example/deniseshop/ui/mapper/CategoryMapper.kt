package com.example.deniseshop.ui.mapper

import com.example.deniseshop.data.models.ApiBrand
import com.example.deniseshop.data.models.ApiCategory
import com.example.deniseshop.ui.models.UiBrand
import com.example.deniseshop.ui.models.UiCategory
import javax.inject.Inject

class CategoryApiToUiMapper@Inject constructor(
	private val brandListApiToUiMapper: BaseListMapper<ApiBrand, UiBrand>
):BaseMapper<ApiCategory, UiCategory> {
	 override fun map(input: ApiCategory): UiCategory {
		 return UiCategory(
			 id = input.id,
			 parentId = input.parentId,
			 name = input.name,
			 icon = input.icon,
			 image = input.image,
			 categories = input.categories?.map { map(it) },
			 brands = input.brands?.let { brandListApiToUiMapper.map(it) }
		 )
	 }
}

class CategoryListApiToUiMapper @Inject constructor(
	private val brandListApiToUiMapper: BaseListMapper<ApiBrand, UiBrand>
): BaseListMapper<ApiCategory, UiCategory>{
	override fun map(input: List<ApiCategory>): List<UiCategory> {
		return input.map { apiCategory ->
			UiCategory(
				id = apiCategory.id,
				parentId =apiCategory.parentId,
				name = apiCategory.name,
				icon = apiCategory.icon,
				image = apiCategory.image,
				categories = apiCategory.categories?.let { map(it) },
				brands = apiCategory.brands?.let { brandListApiToUiMapper.map(it) }
			)
		}
	}

}