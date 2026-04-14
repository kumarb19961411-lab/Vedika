package com.example.vedika.feature.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vedika.core.data.model.VendorType
import com.example.vedika.core.data.repository.VendorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GalleryUiState(
    val vendorType: VendorType = VendorType.VENUE,
    val galleryImages: List<String> = emptyList(),
    val albums: List<GalleryAlbum> = emptyList(),
    val isLoading: Boolean = true
)

data class GalleryAlbum(
    val id: String,
    val title: String,
    val iconName: String, // e.g., "TempleHindu", "Face", "WaterDrop", "Home", "Restaurant"
    val images: List<String>
)

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val vendorRepository: VendorRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GalleryUiState())
    val uiState: StateFlow<GalleryUiState> = _uiState.asStateFlow()

    init {
        loadGallery()
    }

    private fun loadGallery() {
        viewModelScope.launch {
            vendorRepository.getMockVendor().collect { mockState ->
                if (mockState != null) {
                    val defaultAlbums = if (mockState.vendorType == VendorType.VENUE) {
                        listOf(
                            GalleryAlbum("1", "Main Hall", "Home", listOf("https://images.unsplash.com/photo-1519167758481-83f550bb49b3")),
                            GalleryAlbum("2", "Lawn & Garden", "Spa", listOf("https://images.unsplash.com/photo-1469334031218-e382a71b716b")),
                            GalleryAlbum("3", "Amenities", "Checkroom", listOf("https://images.unsplash.com/photo-1519741497674-611481863552"))
                        )
                    } else {
                        listOf(
                            GalleryAlbum("1", "Marriage Themes", "TempleHindu", listOf("https://images.unsplash.com/photo-1544911845-1f34a3eb46b1")),
                            GalleryAlbum("2", "Stage Decor", "Face", listOf("https://images.unsplash.com/photo-1511795409834-ef04bbd61622")),
                            GalleryAlbum("3", "Flower Work", "Spa", listOf("https://images.unsplash.com/photo-1502602898657-3e91760cbb34"))
                        )
                    }

                    _uiState.value = GalleryUiState(
                        vendorType = mockState.vendorType,
                        galleryImages = mockState.galleryImages,
                        albums = defaultAlbums,
                        isLoading = false
                    )
                } else {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
            }
        }
    }
}
