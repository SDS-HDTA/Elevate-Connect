export const getMarkerTypeText = (type) => {
  return markerTypes[type] || 'Unknown';
};

export const markerTypes = {
  0: 'Health Clinic',
  1: 'Community Homestay',
  2: 'Heritage Site',
  3: 'Religious Site',
  4: 'Waste Management Site',
  5: 'Water Source Distribution Point',
  6: 'Power Facility',
  7: 'Telecom Facility',
  8: 'Residential Home',
  9: 'Community Center',
  10: 'Education Site',
  11: 'Marketplace',
  12: 'Food Distribution',
  13: 'Transport Hub',
  14: 'Hazard',
};

export const getMarkerImage = (type) => {
  if (type in markerTypes) {
    return `/pins/${type + 1}.png`; // Adjusted for 1-based index in public folder
  }

  return '';
};
