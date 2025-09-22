// TODO: Replace placeholder categories with real ones
export const getProjectCategoryText = (category) => {
  const texts = {
    0: 'Category0',
    1: 'Category1',
    2: 'Category2',
    3: 'Category3',
    4: 'Category4',
    5: 'Category5',
  };
  return texts[category] || 'Unknown';
};
