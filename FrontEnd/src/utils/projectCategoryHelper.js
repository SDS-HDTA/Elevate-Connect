export const getProjectCategoryText = (category) => {
  return projectCategories[category] || 'Unknown';
};

export const projectCategories = {
  0: 'Education',
  1: 'Water, Sanitation and Hygiene',
  2: 'Health',
  3: 'Nutrition',
  4: 'Livelihoods',
  5: 'Gender Equality',
  6: 'Environment and Climate',
  7: 'Shelter and Housing',
  8: 'Protection',
  9: 'Governance and Advocacy',
  10: 'Disaster Risk Reduction',
};
