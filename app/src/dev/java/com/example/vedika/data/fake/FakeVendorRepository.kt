package com.example.vedika.data.fake

import com.example.vedika.core.data.model.VendorProfile
import com.example.vedika.core.data.model.VendorType
import com.example.vedika.core.data.repository.VendorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeVendorRepository @Inject constructor() : VendorRepository {
    
    private val mockVendors = listOf(
        VendorProfile(
            id = "v1",
            businessName = "Royal Orchid Garden",
            ownerName = "Rajesh Kumar",
            location = "Civil Lines, Kanpur",
            pricing = "₹1,50,000 onwards",
            vendorType = VendorType.VENUE,
            primaryCategory = "Marriage Garden",
            isVerified = true,
            capacity = "500-2000 guests",
            amenities = listOf("Parking", "Catering", "AC Rooms"),
            rating = "4.8",
            coverImage = "https://images.unsplash.com/photo-1519167758481-83f550bb49b3?auto=format&fit=crop&q=80&w=800",
            packageTiers = listOf(
                PackageTier("Silver", "₹1.5L", "Venue Only"),
                PackageTier("Gold", "₹2.5L", "Venue + Basic Decor"),
                PackageTier("Platinum", "₹4.0L", "Venue + Decor + Catering")
            ),
            description = "Royal Orchid Garden is a sprawling 2-acre estate featuring lush green lawns and a majestic glass-walled banquet hall. Perfect for grand weddings and gala events."
        ),
        VendorProfile(
            id = "v2",
            businessName = "Skyline Ballroom",
            ownerName = "Anita Singh",
            location = "Mall Road, Kanpur",
            pricing = "₹80,000 onwards",
            vendorType = VendorType.VENUE,
            primaryCategory = "Banquet Hall",
            isVerified = true,
            capacity = "100-300 guests",
            amenities = listOf("AC", "Valet Parking", "In-house Music"),
            rating = "4.5",
            coverImage = "https://images.unsplash.com/photo-1519741497674-611481863552?auto=format&fit=crop&q=80&w=800",
            description = "An elegant rooftop ballroom offering panoramic city views. Ideal for corporate events, engagements, and intimate wedding celebrations."
        ),
        VendorProfile(
            id = "v3",
            businessName = "Elite Decorators",
            ownerName = "Amit Verma",
            location = "Swaroop Nagar, Kanpur",
            pricing = "₹50,000 onwards",
            vendorType = VendorType.DECORATOR,
            primaryCategory = "Event Decor",
            isVerified = true,
            yearsExperience = "10+ Years",
            rating = "4.9",
            coverImage = "https://images.unsplash.com/photo-1469334031218-e382a71b716b?auto=format&fit=crop&q=80&w=800",
            amenities = listOf("Themed Decor", "Floral Setup", "Lighting"),
            description = "Award-winning decorators specializing in contemporary and traditional floral arrangements. We bring your vision to life with exquisite detail."
        ),
        VendorProfile(
            id = "v4",
            businessName = "Graceful Glow Salon",
            ownerName = "Priya Sharma",
            location = "Indira Nagar, Kanpur",
            pricing = "₹15,000 onwards",
            vendorType = VendorType.DECORATOR, // Using Decorator as fallback for now
            primaryCategory = "Salon",
            isVerified = true,
            rating = "4.7",
            coverImage = "https://images.unsplash.com/photo-1562322140-8baeececf3df?auto=format&fit=crop&q=80&w=800",
            description = "Specialized bridal makeup and hair styling. We use premium international brands to ensure you look your best on your special day."
        ),
        VendorProfile(
            id = "v5",
            businessName = "Taste of Tradition Catering",
            ownerName = "Suresh Khanna",
            location = "Gumti No. 5, Kanpur",
            pricing = "₹800 per plate",
            vendorType = VendorType.VENUE, // Using Venue as fallback for now
            primaryCategory = "Catering",
            isVerified = true,
            rating = "4.6",
            coverImage = "https://images.unsplash.com/photo-1555244162-803834f70033?auto=format&fit=crop&q=80&w=800",
            description = "Authentic North Indian and Continental cuisine. We specialize in large wedding buffets and corporate luncheons with impeccable service."
        ),
        VendorProfile(
            id = "v6",
            businessName = "Lens & Light Studio",
            ownerName = "Rahul Saxena",
            location = "Kakadeo, Kanpur",
            pricing = "₹40,000 onwards",
            vendorType = VendorType.DECORATOR, // Using Decorator as fallback for now
            primaryCategory = "Photography",
            isVerified = true,
            rating = "4.9",
            coverImage = "https://images.unsplash.com/photo-1516035069371-29a1b244cc32?auto=format&fit=crop&q=80&w=800",
            description = "Professional wedding and cinematic photography. We capture your precious moments with creativity and technical excellence."
        )
    )

    private val _vendorProfileState = MutableStateFlow<VendorProfile?>(null)

    override fun getVendorProfileStream(vendorId: String): Flow<VendorProfile?> = 
        _vendorProfileState.map { state ->
            state ?: mockVendors.find { it.id == vendorId }
        }

    override suspend fun saveVendorProfile(profile: VendorProfile): Result<Unit> {
        _vendorProfileState.value = profile
        return Result.success(Unit)
    }
    
    override suspend fun getVendorProfile(vendorId: String): Result<VendorProfile> {
        val current = _vendorProfileState.value
        val mock = mockVendors.find { it.id == vendorId }
        return when {
            current != null && current.id == vendorId -> Result.success(current)
            mock != null -> Result.success(mock)
            else -> Result.failure(Exception("VENDOR_NOT_FOUND"))
        }
    }

    override suspend fun updateBusinessName(vendorId: String, newName: String): Result<Unit> {
        return Result.success(Unit)
    }

    override fun getVendorsByCategory(category: String): Flow<List<VendorProfile>> {
        return kotlinx.coroutines.flow.flowOf(
            mockVendors.filter { 
                it.primaryCategory.equals(category, ignoreCase = true) || 
                it.vendorType.name.equals(category, ignoreCase = true) 
            }
        )
    }

    override fun getFeaturedVendors(): Flow<List<VendorProfile>> {
        return kotlinx.coroutines.flow.flowOf(mockVendors)
    }
}
