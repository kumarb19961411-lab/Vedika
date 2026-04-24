const admin = require('firebase-admin');

// Initialize Firebase Admin
// Make sure to set GOOGLE_APPLICATION_CREDENTIALS environment variable
// pointing to your service account key JSON file.
// Example: $env:GOOGLE_APPLICATION_CREDENTIALS="C:\path\to\service-account-file.json"

admin.initializeApp({
  credential: admin.credential.applicationDefault()
});

const db = admin.firestore();

async function seedPublicVendors() {
  console.log('Starting seed process for public_vendors...');
  try {
    const vendorsSnapshot = await db.collection('vendors').get();
    
    if (vendorsSnapshot.empty) {
      console.log('No vendors found to seed.');
      return;
    }

    const batch = db.batch();
    let count = 0;

    vendorsSnapshot.forEach(doc => {
      const data = doc.data();
      const publicData = {
        businessName: data.businessName || '',
        ownerName: data.ownerName || '',
        primaryServiceCategory: data.primaryServiceCategory || '',
        location: data.location || '',
        capacity: data.capacity || '',
        pricing: data.pricing || '',
        amenities: data.amenities || [],
        vendorType: data.vendorType || 'VENUE',
        isVerified: data.isVerified || false,
        yearsExperience: data.yearsExperience || '',
        coverImage: data.coverImage || '',
        galleryImages: data.galleryImages || [],
        packageTiers: data.packageTiers || []
      };

      const publicRef = db.collection('public_vendors').document(doc.id);
      batch.set(publicRef, publicData);
      count++;
      console.log(`Prepared public_vendors record for: ${doc.id}`);
    });

    await batch.commit();
    console.log(`Successfully seeded ${count} public_vendors.`);

  } catch (error) {
    console.error('Error seeding public_vendors:', error);
  }
}

seedPublicVendors();
